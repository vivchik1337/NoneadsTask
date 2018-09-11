package com.noneadstask.api;

import android.app.Dialog;
import android.content.Context;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.noneadstask.R;
import com.noneadstask.util.Log;
import com.noneadstask.util.Toaster;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class BaseRequest implements Response.ErrorListener, Response.Listener<String> {

    public static String API_URL; // host url define in config.xml
    public static String IMG_BASE_URL;

    public static String SUCCESS = "success";
    public static String MSG = "msg";
    public static String DATA = "data";
    public static String ITEMS = "items";

    private String TAG;
    private Context context;
    private Dialog waitDialog;

    private Response.Listener<String> listener;
    private Response.ErrorListener errorListener;
    private UrlencodedRequest request;

    public BaseRequest(String TAG, Context context, Dialog waitDialog, int method, String apiMethod, Map<String, String> params,
                       Response.Listener<String> listener, Response.ErrorListener errorListener) {
        this.TAG = TAG;
        this.context = context;
        this.waitDialog = waitDialog;
        this.errorListener = errorListener;
        this.listener = listener;

        API_URL = context.getString(R.string.HOST_URL) + "/v1/";
        IMG_BASE_URL = context.getString(R.string.HOST_URL) + "/web/img/";

        if (waitDialog != null) {
            waitDialog.show();
        }
        Log.d(TAG, (method == Request.Method.POST ? "POST , " : "GET ") + "url: " + API_URL + apiMethod + ", params: " + params);

        request = new UrlencodedRequest(method, API_URL + apiMethod, params, null, this, this);
        request.setTag(TAG);
        setRetryPolicy(request);
    }

    private void setRetryPolicy(StringRequest request) {
        request.setRetryPolicy(
                new DefaultRetryPolicy(15000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    public static int tryGetInt(JSONObject jo, String name) {
        try {
            return jo.getInt(name);
        } catch (JSONException e) {
            return -1;
        }
    }

    // ----------------- Parse logic ------------------

    public static String tryGetStr(JSONObject jo, String name) {
        try {
            String rez = jo.getString(name);
            if ((rez.equals("null")) || (rez.equals(""))) {
                return null;
            } else {
                return rez;
            }
        } catch (JSONException e) {
            return null;
        }
    }

    public static String tryGetStrNotNull(JSONObject jo, String name) {
        String rez = tryGetStr(jo, name);
        return rez == null ? "" : rez;
    }

    public static boolean isNetworkErr(Exception e) {
        return e.toString().contains("UnknownHost") || e.toString().contains("HttpHostConnectException")
                || e.toString().contains("Timeout");
    }

    public static boolean isTrue(String text) {
        return text != null && !text.isEmpty() && !text.equals("null") && !text.equals("Null") && text.equals("True");
    }

    public StringRequest getRequest() {
        return request;
    }

    // ----------------- Exception logic ------------------

    @Override
    public void onErrorResponse(VolleyError error) {
        Log.d(TAG, error.toString());
        if (waitDialog != null) {
            waitDialog.hide();
        }
        if (errorListener != null) {
            errorListener.onErrorResponse(error);
        }
        if (BaseRequest.isNetworkErr(error)) {
            Toaster.showToast(context, R.string.NetworkErr);
        } else {
            Toaster.showToast(context, R.string.ServerErr);
        }
    }

    @Override
    public void onResponse(String response) {
        Log.d(TAG, "response: " + response);
        if (waitDialog != null) {
            waitDialog.hide();
        }
        JSONObject jo = null;
        try {
            jo = new JSONObject(response);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (jo != null && listener != null) {
            listener.onResponse(response);
        } else {
            String msg = null;
            if (jo != null) {
                msg = tryGetStr(jo, MSG);
            }
            if (msg == null) {
                Toaster.showToast(context, R.string.ServerErr);
            } else {
                Toaster.showToast(context, msg);
            }
            if (errorListener != null) {
                errorListener.onErrorResponse(new VolleyError(msg));
            }
        }
    }
}
