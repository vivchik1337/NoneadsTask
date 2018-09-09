package com.noneadstask.model;

import android.content.Context;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.noneadstask.adapter.MainList;
import com.noneadstask.api.ListRequest;
import com.noneadstask.util.Log;
import com.noneadstask.util.Toaster;

import java.util.List;

/**
 * Created by vivchik on 08.09.2018.
 */

public class PersonRepository implements MainList.Repository {

    public static final String TAG = PersonRepository.class.getSimpleName();
    private MainList.OnLoadingFinishedListener onLoadingFinishedListener;
    private Context context;

    @Override
    public String loadList(String query, final Context context,
                           final MainList.OnLoadingFinishedListener onLoadingFinishedListener) {
        this.onLoadingFinishedListener = onLoadingFinishedListener;
        this.context = context;

        new ListRequest(context, null, query, new Response.Listener<List<Person>>() {
            @Override
            public void onResponse(List<Person> response) {
                Log.d(TAG, "onResponse. There are " + response.size() + " objects");

                onLoadingFinishedListener.onLodingFinished(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "onErrorResponse." + error.getLocalizedMessage());

                Toaster.showToast(context, error.getLocalizedMessage());
                onLoadingFinishedListener.onLodingFinished(null);
            }
        });

        return null;
    }
}
