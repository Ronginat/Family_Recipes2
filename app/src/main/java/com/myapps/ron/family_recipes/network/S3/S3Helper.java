package com.myapps.ron.family_recipes.network.S3;

import android.content.Context;
import android.net.Uri;
import android.os.StrictMode;
import androidx.annotation.NonNull;
import android.util.Log;

import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferNetworkLossHandler;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.myapps.ron.family_recipes.dal.storage.ExternalStorageHelper;
import com.myapps.ron.family_recipes.utils.MyCallback;

import java.io.File;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

class S3Helper {
    private static final String TAG = S3Helper.class.getSimpleName();

    private static final int STATUS_OK = 200;
    static final String CONTENT_IMAGE = "image/jpeg";
    static final String CONTENT_TEXT = "text/html";

    private static final String BASE_URL = "http://www.dummy.com/";

    private TransferUtility transferUtility;
    private static S3Helper singleton;

    static S3Helper getInstance(Context context) {
        if(singleton == null)
            singleton = new S3Helper(context);

        return singleton;
    }
    private S3Helper(Context context) {
        Util util = new Util();
        TransferNetworkLossHandler.getInstance(context);
        transferUtility = util.getTransferUtility(context);

        /*transferUtility = TransferUtility.builder()
                        .context(context)
                        .awsConfiguration(AWSMobileClient.getInstance().getConfiguration())
                        .s3Client(new AmazonS3Client(AWSMobileClient.getInstance().getCredentialsProvider()))
                        .build();*/
    }

    static void uploadFile(String url, String localPath, String contentType, final MyCallback<Boolean> callback) {
        Log.e(TAG, "uploadFile, " + url);
        File file = new File(localPath);    // create new file on device
        //RequestBody requestFile = RequestBody.create(MediaType.parse(contentType), file);

        RequestBody requestBody = RequestBody.create(MediaType.parse(contentType),file);
        //MultipartBody.Part filePart = MultipartBody.Part.createFormData("file", file.getName(), requestBody);

        /* since the pre-signed URL from S3 contains a host, this dummy URL will
         * be replaced completely by the pre-signed URL.  (I'm using baseURl(String) here
         * but see baseUrl(okHttp3.HttpUrl) in Javadoc for how base URLs are handled
         */
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .build();

        S3Interface s3Interface = retrofit.create(S3Interface.class);
        // imageUrl is the String as received from AWS S3
        Call<Void> call = s3Interface.uploadFile(url, requestBody);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                Log.e(TAG, "upload code = " + response.code());
                Log.e(TAG, "upload message = " + response.message());
                callback.onFinished(true);
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                callback.onFinished(false);
            }
        });
    }

    static boolean uploadFileSync(String url, String localPath, String contentType) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Log.e(TAG, "uploadFile, " + url);
        File file = new File(localPath);    // create new file on device
        //RequestBody requestFile = RequestBody.create(MediaType.parse(contentType), file);

        RequestBody requestBody = RequestBody.create(MediaType.parse(contentType),file);
        //MultipartBody.Part filePart = MultipartBody.Part.createFormData("file", file.getName(), requestBody);

        /* since the pre-signed URL from S3 contains a host, this dummy URL will
         * be replaced completely by the pre-signed URL.  (I'm using baseURl(String) here
         * but see baseUrl(okHttp3.HttpUrl) in Javadoc for how base URLs are handled
         */
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .build();

        S3Interface s3Interface = retrofit.create(S3Interface.class);
        // imageUrl is the String as received from AWS S3
        Call<Void> call = s3Interface.uploadFile(url, requestBody);
        Response<Void> response;
        try {
            response = call.execute();
            Log.e(TAG, "upload code = " + response.code());
            Log.e(TAG, "upload message = " + response.message());
            return response.code() == STATUS_OK;
        } catch (IOException e) {
            Log.e(TAG, "error in " + localPath + ", " + e.getMessage());
        }
        return false;
    }


    /**
     * the file is located in bucket/{@param key}
     * @param key - file path in bucket and dir/fileName in local storage
     * rootPath - root path of current application
     * @param dir - directory in bucket and in local storage
     * @param callback - passed callback
     */
    void downloadFile(Context context, String key, String dir, final MyCallback<Uri> callback) {
        //Log.e(TAG, "downloadFile, " + key);
        /*TransferObserver downloadObserver =
                transferUtility.download(
                        "public/s3Key.txt",
                        new File("/path/to/file/localFile.txt"));*/

        String s3Key = dir + "/" + key;
        Log.e(TAG, "downloadFile, " + s3Key);
        final File file = ExternalStorageHelper.getFileForOnlineDownload(context, dir, key);
        if (file == null) {
            callback.onFinished(null);
            return;
        }

        //Log.e(TAG, "before downloading, key = " + key + " local path = " + path);
        TransferObserver downloadObserver = transferUtility.download(s3Key, file);

        // Attach a listener to the observer to get state update and progress notifications
        downloadObserver.setTransferListener(new TransferListener() {

            @Override
            public void onStateChanged(int id, TransferState state) {
                Log.e(TAG, "state changed, " + state.name());
                if (TransferState.COMPLETED == state) {
                    // Handle a completed upload.
                    callback.onFinished(ExternalStorageHelper.getFileAbsolutePath(context, dir, key));
                }
                if (TransferState.FAILED == state)
                    callback.onFinished(null);
            }

            @Override
            public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {
                float percentDonef = ((float)bytesCurrent/(float)bytesTotal) * 100;
                int percentDone = (int)percentDonef;

                Log.d(TAG, "   ID:" + id + "   bytesCurrent: " + bytesCurrent + "   bytesTotal: " + bytesTotal + " " + percentDone + "%");
            }

            @Override
            public void onError(int id, Exception ex) {
                // Handle errors
                Log.e(TAG, "error downloading file " + id + "\n" + ex.getMessage());
                ex.printStackTrace();
                callback.onFinished(null);
            }

        });

        /*// If you prefer to poll for the data, instead of attaching a
        // listener, check for the state and progress in the observer.
        if (TransferState.COMPLETED == downloadObserver.getState()) {
            // Handle a completed upload.
        }

        Log.d(TAG, "Bytes Transferred: " + downloadObserver.getBytesTransferred());
        Log.d(TAG, "Bytes Total: " + downloadObserver.getBytesTotal());*/
    }
}
