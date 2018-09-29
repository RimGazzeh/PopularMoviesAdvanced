package com.geekgirl.android.popularmovies.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.geekgirl.android.popularmovies.R;


public class Prefs {

    public static final String SORT_OPTION = "SORT_OPTION";
    public static final int FAVORITES_VALUE = 0;
    public static final int MOST_POPULAR_VALUE = 1;
    public static final int TOP_RATED_VALUE = 2;

    public static boolean hasPref(String key, Context ctx) {
        return ctx.getSharedPreferences(ctx.getResources().getString(R.string.app_name), Context.MODE_PRIVATE).contains(key);
    }

    public static void removePref(String key, Context ctx) {
        Logger.d("removePref[" + key + "]  ");
        ctx.getSharedPreferences(ctx.getResources().getString(R.string.app_name), Context.MODE_PRIVATE).edit().remove(key);
    }

    public static void setBooleanPref(String key, boolean value, Context ctx) {
        if (ctx == null) {
            return;
        }
        Logger.d("setPref[" + key + "]  " + value);
        SharedPreferences.Editor edit = ctx.getSharedPreferences(ctx.getResources().getString(R.string.app_name), Context.MODE_PRIVATE).edit();
        edit.putBoolean(key, value).apply();
    }

    public static boolean getBooleanPref(String key, Context ctx) {
        if (ctx == null) {
            return false;
        }
        boolean value = ctx.getSharedPreferences(ctx.getResources().getString(R.string.app_name), Context.MODE_PRIVATE).getBoolean(key, false);
        Logger.d("getPref[" + key + "]  " + value);
        return value;
    }


    public static void setPref(String key, int value, Context ctx) {
        if (ctx == null) {
            return;
        }
        Logger.d("setPref[" + key + "]  " + value);
        SharedPreferences.Editor edit = ctx.getSharedPreferences(ctx.getResources().getString(R.string.app_name), Context.MODE_PRIVATE).edit();
        edit.putInt(key, value).apply();

    }

    public static int getPref(String key, Context ctx, int defaultValue) {
        if (ctx == null) {
            return defaultValue;
        }
        int value = ctx.getSharedPreferences(ctx.getResources().getString(R.string.app_name), Context.MODE_PRIVATE).getInt(key, defaultValue);
        Logger.d("getPref[" + key + "]  " + value);
        return value;
    }

    public static void setPrefString(String key, String value, Context ctx) {
        if (ctx == null) {
            return;
        }
        Logger.d("setPref[" + key + "]  " + value);
        if (value != null) {
            SharedPreferences.Editor edit = ctx.getSharedPreferences(ctx.getResources().getString(R.string.app_name), Context.MODE_PRIVATE).edit();
            edit.putString(key, value).apply();
        }
    }

    public static String getPrefString(String key, Context ctx, String defaultValue) {
        if (ctx == null) {
            return null;
        }
        String value = ctx.getSharedPreferences(ctx.getResources().getString(R.string.app_name), Context.MODE_PRIVATE).getString(key, defaultValue);
        Logger.d("getPref[" + key + "]  " + value);
        return value;
    }


    public static void clear(Context ctx) {
        if (ctx == null) {
            return;
        }
        ctx.getSharedPreferences(ctx.getResources().getString(R.string.app_name), Context.MODE_PRIVATE).edit().clear().apply();
    }


}
