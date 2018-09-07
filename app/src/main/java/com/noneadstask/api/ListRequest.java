package com.noneadstask.api;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.noneadstask.R;
import com.noneadstask.model.Person;
import com.noneadstask.util.Toaster;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class ListRequest implements Response.Listener<String> {

    public static final String TAG = ListRequest.class.getSimpleName();

    //Request example: https://public-api.nazk.gov.ua/v1/declaration/?q=Чер

    private Dialog waitDialog;
    private Context context;
    private Response.Listener<List<Person>> listener;
    private Response.ErrorListener errorListener;

    private String params;

    public ListRequest(Context context, Dialog waitDialog, @NonNull String requestString,
                       Response.Listener<List<Person>> listener, Response.ErrorListener errorListener) {
        this.context = context;
        this.waitDialog = waitDialog;
        this.listener = listener;
        this.errorListener = errorListener;

        try {
            params = "?q=" + URLEncoder.encode(requestString, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        BaseRequest baseRequest = new BaseRequest(TAG, context, waitDialog, Request.Method.GET, "declaration" + params,
                null, this, errorListener);
        ApiSingleton.getInstance(context).addToRequestQueue(baseRequest.getRequest());
    }

    @Override
    public void onResponse(String response) {
        JSONObject jo;
        try {
            jo = new JSONObject(response);
            JSONArray jsonData = jo.getJSONArray(BaseRequest.ITEMS);
            ArrayList<Person> list = new ArrayList<>();
            for (int i = 0; i < jsonData.length(); i++) {
                JSONObject jItem = jsonData.getJSONObject(i);
                Person item = Person.parseJSON(jItem);
                list.add(item);
            }
            if (listener != null) {
                listener.onResponse(list);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Toaster.showToast(context, R.string.ServerErr);
            if (errorListener != null) {
                errorListener.onErrorResponse(new VolleyError(e));
            }
            return;
        }
    }

}
