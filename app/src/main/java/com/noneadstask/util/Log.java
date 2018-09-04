package com.noneadstask.util;

public class Log {

    //in future that class give possibility to add logging into file

    public static void d(String TAG, String mess) {
        android.util.Log.d(TAG, mess);
    }

    public static void w(String TAG, String mess) {
        android.util.Log.w(TAG, mess);
    }

    public static void i(String TAG, String mess) {
        android.util.Log.i(TAG, mess);
    }

    public static void e(String TAG, String mess) {
        android.util.Log.e(TAG, mess);
    }
}
