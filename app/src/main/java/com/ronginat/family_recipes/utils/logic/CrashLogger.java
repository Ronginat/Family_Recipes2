package com.ronginat.family_recipes.utils.logic;

import android.content.Context;
import android.util.Log;

import com.crashlytics.android.Crashlytics;
import com.ronginat.family_recipes.BuildConfig;

import io.fabric.sdk.android.Fabric;

/**
 * Created by ronginat on 30/06/2019
 */
public class CrashLogger {

    /**
     * Instantiate Crashlytics
     * @param context application context
     */
    public static void init(Context context) {
        if (!BuildConfig.DEBUG)
            Fabric.with(context, new Crashlytics());
    }

    /**
     * Set user identifier for error logs
     * @param name username of AWS Cognito account
     */
    public static void setName(String name) {
        if (!BuildConfig.DEBUG)
            Crashlytics.setUserName(name);
    }

    /**
     * Log an Exception to be viewed and analyzed later
     * @param throwable Throwable to be logged
     */
    public static void logException(Throwable throwable) {
        if (!BuildConfig.DEBUG)
            Crashlytics.logException(throwable);
        else
            e(CrashLogger.class.getSimpleName(), throwable.getMessage(), throwable);
    }

    public static void e (String TAG, String message, Throwable t) {
        if ((BuildConfig.DEBUG))
            Log.e(TAG, message, t);
    }

    public static void e (String TAG, String message) {
        if ((BuildConfig.DEBUG))
            Log.e(TAG, message);
    }

    public static void d (String TAG, String message) {
        if ((BuildConfig.DEBUG))
            Log.d(TAG, message);
    }

    public static void i (String TAG, String message) {
        if ((BuildConfig.DEBUG))
            Log.i(TAG, message);
    }
}
