package com.example.mhealth4t2d.Utils;

import android.content.Context;
import android.content.SharedPreferences;

public class Preferences {

    private static final String sharedPrefFile = "com.example.mhealth4t2d";

    private static SharedPreferences mPreferences = null;

    private static void getPref(Context context){

        if(mPreferences == null)

            mPreferences = context.getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE);

    }

    public static void editIntPref(Context context, String key, int value){

        getPref(context);

        SharedPreferences.Editor preferencesEditor = mPreferences.edit();
        preferencesEditor.putInt(key, value);
        preferencesEditor.apply();


    }

    public static void editStringPref(Context context, String key, String value){

        getPref(context);

        SharedPreferences.Editor preferencesEditor = mPreferences.edit();
        preferencesEditor.putString(key, value);
        preferencesEditor.apply();


    }

    public static int getIntPref(Context context, String key){

        getPref(context);
        return mPreferences.getInt(key, -1);

    }

    public static String getStringPref(Context context, String key){

        getPref(context);
        return mPreferences.getString(key, "");

    }

    public static void clearPref(Context context){

        getPref(context);

        SharedPreferences.Editor preferencesEditor = mPreferences.edit();

        preferencesEditor.clear();
        preferencesEditor.apply();

    }
}
