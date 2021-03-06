package com.ronginat.family_recipes;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.PowerManager;
import android.provider.Settings;

import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.WorkManager;

import com.ronginat.family_recipes.background.workers.DeleteOldFilesWorker;
import com.ronginat.family_recipes.utils.Constants;
import com.ronginat.family_recipes.utils.logic.CrashLogger;
import com.ronginat.family_recipes.utils.logic.SharedPreferencesHandler;
import com.ronginat.localehelper.LocaleAwareApplication;

import java.util.Calendar;


public class MyApplication extends LocaleAwareApplication {

    public static final String TAG = MyApplication.class.getSimpleName();

    private static MyApplication mContext;

    private boolean darkTheme;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;

        CrashLogger.init(this);
        enqueueFilesWorker();
        //AppHelper.initTokenObserver(getApplicationContext());
    }

    private void enqueueFilesWorker() {
        if (SharedPreferencesHandler.getBoolean(this, Constants.FIRST_APP_LAUNCH, true)) {
            SharedPreferencesHandler.writeBoolean(this, Constants.FIRST_APP_LAUNCH, false);

            WorkManager.getInstance(this).enqueueUniquePeriodicWork(
                    DeleteOldFilesWorker.class.getSimpleName(),
                    ExistingPeriodicWorkPolicy.KEEP,
                    DeleteOldFilesWorker.createPostRecipesWorker());
        }
    }

    public static boolean isDarkTheme() {
        return mContext.darkTheme;
    }

    private void setDarkTheme(boolean darkTheme) {
        this.darkTheme = darkTheme;
    }

    public void applyTheme(Activity activity) {
        SharedPreferences sPref = SharedPreferencesHandler.getSharedPreferences(this);
        String themePreference = sPref.getString(getString(R.string.preference_key_dark_theme), Constants.DARK_THEME_NEVER);
        switch (themePreference) {
            case Constants.DARK_THEME_ALWAYS: // always dark theme
                activity.setTheme(R.style.AppTheme_Dark);
                setDarkTheme(true);
                break;
            case Constants.DARK_THEME_NEVER:
                activity.setTheme(R.style.AppTheme_Light);
                setDarkTheme(false);
                break;
            case Constants.DARK_THEME_NIGHT_BATTERY_SAVER:
                //check if night
                if(isPowerSaverOn() || isNightHours()) {
                    activity.setTheme(R.style.AppTheme_Dark);
                    setDarkTheme(true);
                }
                else {
                    activity.setTheme(R.style.AppTheme_Light);
                    setDarkTheme(false);
                }
                break;
            case Constants.DARK_THEME_BATTERY_SAVER:
                if(isPowerSaverOn()) {
                    activity.setTheme(R.style.AppTheme_Dark);
                    setDarkTheme(true);
                }
                else {
                    activity.setTheme(R.style.AppTheme_Light);
                    setDarkTheme(false);
                }
                break;
            default:
                activity.setTheme(R.style.AppTheme_Light);
                setDarkTheme(false);
                break;
        }
        //activity.setTheme(sPref.getBoolean(getString(R.string.preference_key_dark_theme), false) ? R.style.AppTheme_Dark : R.style.AppTheme_Light);
    }

    public static Context getContext() {
        return mContext;
    }

    private boolean isPowerSaverOn() {
        PowerManager powerManager = (PowerManager)
                getSystemService(Context.POWER_SERVICE);
        return powerManager != null && powerManager.isPowerSaveMode();
    }

    private boolean isNightHours() {
        Calendar cal = Calendar.getInstance();
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        return hour < 6 || hour > 18;
    }

    public static String getLocale() {
        return SharedPreferencesHandler.getString(
                mContext,
                mContext.getString(R.string.preference_key_language),
                mContext.getResources().getStringArray(R.array.pref_languages_list_values)[1]
        );
    }

    @SuppressLint("HardwareIds")
    public static String getDeviceId() {
        //return FirebaseInstanceId.getInstance().getId();
        return Settings.Secure.getString(mContext.getContentResolver(), Settings.Secure.ANDROID_ID);
    }
}