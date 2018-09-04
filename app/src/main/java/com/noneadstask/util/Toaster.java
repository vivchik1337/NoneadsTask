package com.noneadstask.util;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

/**
 * Helper to show {@link Toast} notifications
 */
public class Toaster {

    // global toast constants
    public static final int TOAST_LENGTH = Toast.LENGTH_LONG;

    public static void showToast(Context context, String mess) {
        Log.d("showToast", mess);
        Toast toast = Toast.makeText(context, mess, TOAST_LENGTH);
        toast.show();
    }

    public static void showToast(Context context, int messResId) {
        showToast(context, context.getString(messResId));
    }

}
