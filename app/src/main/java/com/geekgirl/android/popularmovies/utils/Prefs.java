package com.geekgirl.android.popularmovies.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.geekgirl.android.popularmovies.R;

import java.lang.reflect.Field;



public class Prefs {

    public static final String SORT_OPTION = "SORT_OPTION";
    public static final String TOP_RATED_VALUE = "top_rated";
    public static final String MOST_POPULAR_VALUE = "most_popular";

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


    public static void setPref(String key, String value, Context ctx) {
        if (ctx == null) {
            return;
        }
        Logger.d("setPref[" + key + "]  " + value);
        if (value != null) {
            SharedPreferences.Editor edit = ctx.getSharedPreferences(ctx.getResources().getString(R.string.app_name), Context.MODE_PRIVATE).edit();
            edit.putString(key, value).apply();
        }
    }

    public static String getPref(String key, Context ctx) {
        if (ctx == null) {
            return null;
        }
        String value = ctx.getSharedPreferences(ctx.getResources().getString(R.string.app_name), Context.MODE_PRIVATE).getString(key, "");
        Logger.d("getPref[" + key + "]  " + value);
        return value;
    }

    public static void setPrefObject(Object obj, Context ctx) {

        Logger.d("setPref[" + obj.getClass().getSimpleName() + "]");
        for (Field f : obj.getClass().getFields()) {
            try {

                if (f.getType() == String.class) {
                    if (f.get(obj) != null) {
                        String value = f.get(obj) + "";
                        String name = f.getName();
                        String className = obj.getClass().getSimpleName();
                        SharedPreferences.Editor edit = ctx.getSharedPreferences(ctx.getResources().getString(R.string.app_name), Context.MODE_PRIVATE).edit();
                        edit.putString(className + "_" + name, value).apply();
                        Logger.d("set [" + className + "_" + name + "]" + f.get(obj));
                    }
                } else if (f.getType() == double.class) {
                    if (f.get(obj) != null) {
                        String value = String.valueOf(f.get(obj));
                        String name = f.getName();
                        String className = obj.getClass().getSimpleName();
                        SharedPreferences.Editor edit = ctx.getSharedPreferences(ctx.getResources().getString(R.string.app_name), Context.MODE_PRIVATE).edit();
                        edit.putString(className + "_" + name, value).apply();
                        Logger.d("set [" + className + "_" + name + "]" + f.get(obj));
                    }
                } else if (f.getType() == int.class) {
                    if (f.get(obj) != null) {
                        String value = String.valueOf(f.get(obj));
                        String name = f.getName();
                        String className = obj.getClass().getSimpleName();
                        SharedPreferences.Editor edit = ctx.getSharedPreferences(ctx.getResources().getString(R.string.app_name), Context.MODE_PRIVATE).edit();
                        edit.putString(className + "_" + name, value).apply();
                        Logger.d("set [" + className + "_" + name + "]" + f.get(obj));
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public static void clear(Context ctx) {
        if (ctx == null) {
            return;
        }
        ctx.getSharedPreferences(ctx.getResources().getString(R.string.app_name), Context.MODE_PRIVATE).edit().clear().apply();
    }


    public static Object getPrefObject(Class<?> cl, Context ctx) {

        try {
            String className = cl.getSimpleName();
            Logger.d("getPref[" + className + "]");
            Object obj = cl.newInstance();
            for (Field f : cl.getFields()) {
                String value = ctx.getSharedPreferences(ctx.getResources().getString(R.string.app_name), Context.MODE_PRIVATE).getString(className + "_" + f.getName(), null);
                if ((value != null) && (value.length() > 0) && (f.getType() == String.class)) {

                    f.set(obj, value.replace("null", ""));
                    Logger.d("get [" + className + "_" + f.getName() + "]" + value);
                } else if ((value != null) && (value.length() > 0) && (f.getType() == double.class)) {

                    f.set(obj, Double.valueOf(value.replace("null", "")));
                    Logger.d("get [" + className + "_" + f.getName() + "]" + Double.valueOf(value.replace("null", "")));
                } else if ((value != null) && (value.length() > 0) && (f.getType() == int.class)) {

                    f.set(obj, Integer.parseInt(value.replace("null", "")));
                    Logger.d("get [" + className + "_" + f.getName() + "]" + Integer.parseInt(value.replace("null", "")));
                }
            }

            return obj;
        } catch (Exception e) {
            Logger.d("Error[" + cl + "]:" + e.getLocalizedMessage() + " ctx:" + ctx);
        }

        try {
            return cl.newInstance();
        } catch (Exception e) {
        }

        return null;
    }

}
