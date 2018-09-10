package com.noneadstask.model;

import android.content.Context;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.noneadstask.adapter.FavoritesList;
import com.noneadstask.adapter.MainList;
import com.noneadstask.api.ListRequest;
import com.noneadstask.util.Log;
import com.noneadstask.util.Toaster;

import java.util.List;

import io.realm.Realm;

/**
 * Created by vivchik on 08.09.2018.
 */

public class FavoritesRepository implements FavoritesList.Repository {

    public static final String TAG = FavoritesRepository.class.getSimpleName();
    private FavoritesList.OnLoadingFinishedListener onLoadingFinishedListener;
    private Context context;

    @Override
    public List<Person> loadList(String query, Context context, Realm realm,
                                 FavoritesList.OnLoadingFinishedListener onLoadingFinishedListener) {
        this.onLoadingFinishedListener = onLoadingFinishedListener;
        this.context = context;

        return realm.allObjects(Person.class);
    }

}
