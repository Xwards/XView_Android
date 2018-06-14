package com.xwards.xview.sp;

import android.content.Context;
import android.content.SharedPreferences;


/**
 * Nithinjithing Dept.
 * ABSTRACTION FOR SP
 * COMMON SP DATA HANDLER
 * Shared Preference Common Class
 * All the Shared Preference Data will handle from here
 */
public abstract class SPDataHandler {

    private Context mContext;
    private SharedPreferences mPreference;

    protected SPDataHandler(Context context) {
        this.mContext = context;
        this.mPreference = mContext.getSharedPreferences(SPConstantInterface.SP_NAME, Context.MODE_PRIVATE);
    }

    /**
     * COMMON SETTER FOR STRING SP DATA
     */
    protected void saveStringToSp(String key, String value) {

        SharedPreferences.Editor editor = mPreference.edit();
        editor.putString(key, value);
        editor.commit();
    }


    /**
     * COMMON SETTER FOR BOOLEAN SP DATA
     */
    protected void saveBooleanToSp(String key, boolean value) {
        SharedPreferences.Editor editor = mPreference.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    /**
     * COMMON GETTER FOR STRING SP DATA
     */
    protected String getStringSpData(String key, String defaultVaule) {
        return this.mPreference.getString(key, defaultVaule);
    }


    /**
     * COMMON GETTER FOR BOOLEAN SP DATA
     */
    protected boolean getBooleanSpData(String key, boolean defaultValue) {
        return this.mPreference.getBoolean(key, defaultValue);
    }


    /**
     * COMMON GETTER FOR INTEGER SP DATA
     */
    protected int getIntegerSpData(String key, int defaultValue) {

        return this.mPreference.getInt(key, defaultValue);
    }


    protected long getLongSpData(String key, long defaultValue) {
        return this.mPreference.getLong(key, defaultValue);
    }

    protected void setLomgSpData(String key, long value) {
        SharedPreferences.Editor editor = mPreference.edit();
        editor.putLong(key, value);
        editor.commit();
    }

    /**
     * COMMON SETTER FOR INTEGER DATA
     */
    protected void setIntegerData(String key, int value) {
        SharedPreferences.Editor editor = mPreference.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    protected void removeSpData(String key) {
        SharedPreferences.Editor editor = mPreference.edit();
        editor.remove(key).commit();
    }

}
