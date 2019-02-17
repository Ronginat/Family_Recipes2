package com.myapps.ron.family_recipes;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.PowerManager;

import com.myapps.ron.family_recipes.utils.Constants;
import com.myapps.ron.family_recipes.utils.LocaleHelper;
import com.myapps.ron.family_recipes.utils.SharedPreferencesHandler;

import java.util.Calendar;


public class MyApplication extends Application {

    public static final String TAG = MyApplication.class.getSimpleName();

    private static MyApplication mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
    }

    public boolean checkInternetConnection(){
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            //we are connected to a network
            return connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                    connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED;
        }
        return false;
    }

    public void applyTheme(Activity activity) {
        SharedPreferences sPref = SharedPreferencesHandler.getSharedPreferences(this);
        String themePreference = sPref.getString(getString(R.string.preference_key_dark_theme), Constants.DARK_THEME_NEVER);
        if (themePreference != null) {
            switch (themePreference) {
                case Constants.DARK_THEME_ALWAYS: // always dark theme
                    activity.setTheme(R.style.AppTheme_Dark);
                    break;
                case Constants.DARK_THEME_NEVER:
                    activity.setTheme(R.style.AppTheme_Light);
                    break;
                case Constants.DARK_THEME_NIGHT_BATTERY_SAVER:
                    //check if night
                    if(isPowerSaverOn() || isNightHours())
                        activity.setTheme(R.style.AppTheme_Dark);
                    else
                        activity.setTheme(R.style.AppTheme_Light);
                    break;
                case Constants.DARK_THEME_BATTERY_SAVER:
                    if(isPowerSaverOn())
                        activity.setTheme(R.style.AppTheme_Dark);
                    else
                        activity.setTheme(R.style.AppTheme_Light);
                    break;
                default:
                    activity.setTheme(R.style.AppTheme_Light);
                    break;
            }
        } else
            activity.setTheme(R.style.AppTheme_Light);
        //activity.setTheme(sPref.getBoolean(getString(R.string.preference_key_dark_theme), false) ? R.style.AppTheme_Dark : R.style.AppTheme_Light);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleHelper.onAttach(base, "en"));
    }

    public static Context getContext() {
        return mContext;
    }

    private boolean isPowerSaverOn() {
        PowerManager powerManager = (PowerManager)
                getSystemService(Context.POWER_SERVICE);
        return powerManager.isPowerSaveMode();
    }

    private boolean isNightHours() {
        Calendar cal = Calendar.getInstance();
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        return hour < 6 || hour > 18;
    }
}