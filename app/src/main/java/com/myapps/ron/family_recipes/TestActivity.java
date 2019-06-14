package com.myapps.ron.family_recipes;

import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.myapps.ron.family_recipes.layout.APICallsHandler;
import com.myapps.ron.family_recipes.logic.storage.ExternalStorageHelper;
import com.myapps.ron.family_recipes.logic.storage.StorageWrapper;

import java.io.File;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class TestActivity extends AppCompatActivity {

    private static final String TAG = TestActivity.class.getSimpleName();
    ImageView imageView;
    String fileName;
    ColorMatrixColorFilter filter;
    private CompositeDisposable compositeDisposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        compositeDisposable = new CompositeDisposable();
        String html = "<div dir=rtl lang=he class=recipeStyle> \n" +
                "    <h2>מרכיבים:</h2>\n" +
                "    <ul>\n" +
                "        <li>טחינה גולמית</li>\n" +
                "        <li>חלב מרוכז</li>\n" +
                "        <li>2 שמנת מתוקה</li>\n" +
                "    </ul> \n" +
                "    <hr>\n" +
                "\n" +
                "    <h2>אופן הכנה:</h2>\n" +
                "\n" +
                "    <ol>\n" +
                "        <li>לשפוך הכל לקערה</li>\n" +
                "        <li>לערבב טוב טוב</li>\n" +
                "        <li>להכניס למקפיא ל4 שעות</li>\n" +
                "    </ol>\n" +
                "    <h5>שיהיה בתיאבון!</h5>\n" +
                "</div>";
        WebView webView = findViewById(R.id.webView);
        webView.loadData(html, "text/html", "UTF-8");

        /*fileName = "chickenfood1.jpg";
        imageView = findViewById(R.id.test_image);

        ColorMatrix colorMatrix = new ColorMatrix();
        colorMatrix.setSaturation(0);
        filter = new ColorMatrixColorFilter(colorMatrix);*/


        if (false) {
            compositeDisposable.add(APICallsHandler.getTestObservable()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(objectResponse -> {
                        Log.e(TAG, Boolean.toString(objectResponse.isSuccessful()));
                        Log.e(TAG, Integer.toString(objectResponse.code()));
                        if (objectResponse.isSuccessful()) {
                            //Log.e(TAG, "message: " + objectResponse.message());
                            Log.e(TAG, "body: " + objectResponse.body());
                        }
                        if (objectResponse.errorBody() != null) {
                            //Log.e(TAG, "message: " + objectResponse.message());
                            Gson gson = new Gson();
                            Map<String, String> error = gson.fromJson(objectResponse.errorBody().charStream(), new TypeToken<Map<String, String>>() {
                            }.getType());
                            Log.e(TAG, error.toString());
                        }
                    })
            );
        }
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }

    public void download(View view) {
        StorageWrapper.getThumbFile(this, fileName)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableSingleObserver<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Log.e(TAG, "download callback, " + uri.getPath());
                        //imageView.setImageURI(uri);
                        Glide.with(getApplicationContext())
                                .load(uri)
                                .diskCacheStrategy(DiskCacheStrategy.NONE)
                                .into(imageView);

                        imageView.setColorFilter(filter);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        throwable.printStackTrace();
                        Log.e(TAG, throwable.getMessage());
                    }
                });
    }

    public void delete(View view) {
        imageView.setImageResource(R.mipmap.ic_logo_foreground);
        imageView.setColorFilter(filter);
        Uri uri = ExternalStorageHelper.getFileAbsolutePath(this, "thumbnails", fileName);
        if (uri != null) {
            Log.e(TAG, "deleting file, " + new File(uri.getPath()).delete());
        }
    }

}
