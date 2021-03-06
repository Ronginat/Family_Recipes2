package com.ronginat.family_recipes.ui.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.ronginat.family_recipes.R;
import com.ronginat.family_recipes.utils.Constants;
import com.ronginat.family_recipes.utils.logic.CrashLogger;
import com.ronginat.family_recipes.utils.logic.DateUtil;

public class SharedActivity extends AppCompatActivity {

    private final String TAG = getClass().getSimpleName();
    private final int OPEN_RECIPE_REQUEST = 1;


    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shared);

        Intent receivedIntent = getIntent();
        if (Intent.ACTION_VIEW.equals(receivedIntent.getAction()) && receivedIntent.getData() != null) {
            //Log.e(TAG, receivedIntent.getType());
            Uri data = receivedIntent.getData();
            CrashLogger.e(TAG, data.toString());
            if (getString(R.string.scheme).equals(data.getScheme()) &&
                    getString(R.string.host).equals(data.getHost())) {
                //Log.e(TAG, data.getLastPathSegment());
                CrashLogger.e(TAG, data.getPathSegments().toString());
                String recipeId = data.getLastPathSegment();
                String recipeDate = data.getQueryParameter(Constants.SHARE_DATE_QUERY);
                if (recipeId != null && !"".equals(recipeId) && recipeDate != null && !"".equals(recipeDate)) {
                    CrashLogger.e(TAG, "id = " + Uri.decode(recipeId));
                    CrashLogger.e(TAG, "date = " + Uri.decode(recipeDate));
                    CrashLogger.e(TAG, "date = " + DateUtil.getDateStringFromLong(Long.valueOf(Uri.decode(recipeDate))));
                    Intent splashIntent = new Intent(SharedActivity.this, SplashActivity.class);
                    splashIntent.putExtra(Constants.RECIPE_ID, Uri.decode(recipeId));
                    splashIntent.putExtra(Constants.LAST_MODIFIED, DateUtil.getDateStringFromLong(Long.valueOf(Uri.decode(recipeDate))));
                    splashIntent.putExtra(Constants.SPLASH_ACTIVITY_CODE, Constants.SPLASH_ACTIVITY_CODE_RECIPE);
                    startActivityForResult(splashIntent, OPEN_RECIPE_REQUEST);
                } else {
                    setResult(RESULT_CANCELED);
                    finish();
                }
            }
            else {
                setResult(RESULT_CANCELED);
                finish();
            }
        } else {
            setResult(RESULT_CANCELED);
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        CrashLogger.e(TAG, "onDestroy");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == OPEN_RECIPE_REQUEST) {
            setResult(resultCode == RESULT_OK ? RESULT_OK : RESULT_CANCELED);
            finish();
        }
    }
}
