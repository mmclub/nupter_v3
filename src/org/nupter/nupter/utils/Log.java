package org.nupter.nupter.utils;


import org.nupter.nupter.MyApplication;

public class Log {

    protected static final String TAG = "TAG";

    public static void d(String msg) {
        if (MyApplication.IS_DEBUG)
            android.util.Log.d(TAG, msg);
    }

}