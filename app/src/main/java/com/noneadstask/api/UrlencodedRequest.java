package com.noneadstask.api;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/*
 * Created by viktord (viktor.derk@gmail.com) on 19/6/2018.
 */

public class UrlencodedRequest extends StringRequest {

    Map<String, String> params = new HashMap<String, String>();
    Map<String, String> headers = new HashMap<String, String>();

    public UrlencodedRequest(int method, String url, Map<String, String> params, Map<String, String> headers,
                             Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(method, url, listener, errorListener);
        if (params != null) {
            this.params = params;
        }
        if (headers != null) {
            this.headers = headers;
        }
    }

    @Override
    public String getBodyContentType() {
        return "application/x-www-form-urlencoded; charset=UTF-8";
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return params;
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return headers;
    }
}
