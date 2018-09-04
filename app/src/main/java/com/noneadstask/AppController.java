package com.noneadstask;

import android.app.Application;

import com.noneadstask.util.Log;


public class AppController extends Application {

    public static final String TAG = AppController.class.getSimpleName();

    @Override
    public void onCreate() {
        super.onCreate();

        Log.d(TAG, "onCreate");
    }
}
