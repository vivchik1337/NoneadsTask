package com.noneadstask.api;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.LruCache;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

public class ApiSingleton {

    private static ApiSingleton mInstance;
    private RequestQueue mRequestQueue;
    private static Context context;

    private ApiSingleton(Context context) {
        this.context = context;
        mRequestQueue = getRequestQueue();
    }

    public static synchronized ApiSingleton getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new ApiSingleton(context);
        }
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
            mRequestQueue = Volley.newRequestQueue(context.getApplicationContext());
        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }
}
