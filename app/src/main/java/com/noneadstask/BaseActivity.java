package com.noneadstask;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.noneadstask.util.Toaster;

public abstract class BaseActivity extends AppCompatActivity {

    public static final String TAG = BaseActivity.class.getSimpleName();

    protected Context context;
    protected BaseActivity activity;
    protected android.support.v4.app.FragmentManager fragmentManager;
    public ProgressDialog waitDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getApplicationContext();
        activity = this;
        fragmentManager = getSupportFragmentManager();
        initWaitDialog();
    }

    // ------------------------- Activity states ---------------------------

    @Override
    protected void onStart() {
        super.onStart();
        if (!BuildConfig.DEBUG) {
            // TODO add GA
        }
    }

    public static boolean isResume = false;

    @Override
    protected void onResume() {
        isResume = true;
        super.onResume();
    }

    @Override
    protected void onPause() {
        isResume = false;
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (!BuildConfig.DEBUG) {
            // TODO add GA
        }
    }

    // ----------------- Wait Dialog logic ------------------

    public void initWaitDialog() {
        waitDialog = new ProgressDialog(activity);
        waitDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        waitDialog.setMessage(getString(R.string.Loading));
        waitDialog.setIndeterminate(true);
        waitDialog.setCanceledOnTouchOutside(false);
    }

    // ----------------- Run activity ------------------

    public void runActivity(Class<?> cls, Bundle bundle) {
        Intent intent = new Intent(this, cls);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }

    public void runActivityForResult(Class<?> cls, int requestCode, Bundle bundle) {
        Intent intent = new Intent(this, cls);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivityForResult(intent, requestCode);
    }

    // ----------------- Show message ------------------

    public void showMessage(String mess) {
        Toaster.showToast(context, mess);
    }

    public void showMessage(int messResId) {
        Toaster.showToast(context, messResId);
    }

    // --------------------- Keyboard ------------------

    public void hideKeyboard(EditText editText) {
        try {
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
        } catch (Exception e) {
            Log.d(TAG, e.getLocalizedMessage());
        }
    }

    public void showKeyboard(EditText editText) {
        try {
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInputFromWindow(editText.getApplicationWindowToken(), InputMethodManager.SHOW_FORCED, 0);
        } catch (Exception e) {
            Log.d(TAG, e.getLocalizedMessage());
        }
    }

    // ------------------------ Etc ---------------------

    protected void openURLinBrowser(String url) {
        try {
            Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(myIntent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, "No application can handle this request."
                    + " Please install a webbrowser", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    // ----------------- Destroy logic ------------------

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (BuildConfig.DEBUG) {
            System.gc();
            String p1 = String.format("%,d", android.os.Debug.getNativeHeapAllocatedSize());
            String p2 = String.format("%,d", android.os.Debug.getNativeHeapFreeSize());
            String p3 = String.format("%,d", android.os.Debug.getNativeHeapSize());
            Log.d("MEM USED", "HeapAllocatedSize=" + p1 + "-" + p2 + "/" + p3);
        }
    }
}
