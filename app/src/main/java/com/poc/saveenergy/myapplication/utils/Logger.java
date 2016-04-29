package com.poc.saveenergy.myapplication.utils;

import android.util.Log;

import com.poc.saveenergy.myapplication.BuildConfig;

/**
 * Created by vaibhav.singhal on 4/29/2016.
 */
public class Logger {
    public static void debug(String tag,
                             String message) {
        if (BuildConfig.DEBUG) {
            Log.d(tag, message);
        }
    }

    public static void error(String tag,
                             String message) {
        if (BuildConfig.DEBUG) {
            Log.e(tag, message);
        }
    }

    public static void error(String tag,
                             String message, Throwable tr) {
        if (BuildConfig.DEBUG) {
            Log.e(tag, message, tr);
        }
    }
}
