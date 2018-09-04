package com.noneadstask;

import android.app.Application;

import com.noneadstask.util.Log;
/*
 * Created by viktord (viktor.derk@gmail.com) on 19/6/2018.
 */

public class AppController extends Application {

    public static final String TAG = AppController.class.getSimpleName();

    @Override
    public void onCreate() {
        super.onCreate();

        Log.d(TAG, "onCreate");
    }
}
