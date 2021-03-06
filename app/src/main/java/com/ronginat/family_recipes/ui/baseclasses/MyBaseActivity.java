package com.ronginat.family_recipes.ui.baseclasses;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.ronginat.family_recipes.MyApplication;
import com.ronginat.family_recipes.R;
import com.ronginat.family_recipes.utils.logic.SharedPreferencesHandler;
import com.ronginat.localehelper.LocaleAwareCompatActivity;

/**
 * Created by ronginat on 13/12/2018.
 */
public abstract class MyBaseActivity extends LocaleAwareCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    /*
     * this method is useful when changing language at runtime
     * @param newBase new base context for the activity
     */
    /*@Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase));
    }*/

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Log.e(getClass().getSimpleName(), "on create");
        ((MyApplication)getApplication()).applyTheme(this);
        //getSharedPreferences(getString(R.string.sharedPreferences), MODE_PRIVATE).registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        SharedPreferencesHandler.getSharedPreferences(this).registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //Log.e(getClass().getSimpleName(), "on destroy");
        SharedPreferencesHandler.getSharedPreferences(this).unregisterOnSharedPreferenceChangeListener(this);
        //getSharedPreferences(getString(R.string.sharedPreferences), MODE_PRIVATE).unregisterOnSharedPreferenceChangeListener(this);
    }

    /**
     * Listens to changing in sharedPreferences and react when dark theme value had changed
     * @param sharedPreferences sharedPreferences file (name - R.string.sharedPreferences)
     *                          had changed
     * @param key the key that changed in the file
     */
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key == null)
            return;
        //Log.e(getClass().getSimpleName(), "value, " + sharedPreferences.getString(key, "default"));
        if (key.equals(getString(R.string.preference_key_dark_theme))) {
            recreate();
        }

        /*if (key.equals(getString(R.string.preference_key_dark_theme))) {
            Log.e(getClass().getSimpleName(), "value, " + sharedPreferences.getBoolean(key, false));
            setTheme(sharedPreferences.getBoolean(key, false) ? R.style.AppTheme_Dark : R.style.AppTheme_Light);
            //toolbar.setPopupTheme(sharedPreferences.getBoolean(key, false) ? R.style.AppTheme_PopupOverlay_Dark : R.style.AppTheme_PopupOverlay_Light);
            recreate();
        }*/
    }
}
