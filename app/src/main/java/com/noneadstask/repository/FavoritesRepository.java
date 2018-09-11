package com.noneadstask.repository;

import android.content.Context;

import com.noneadstask.interfaces.FavoritesList;
import com.noneadstask.model.Person;

import java.util.ArrayList;
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
        List<Person> list = new ArrayList<>();
        list.addAll(realm.where(Person.class).contains("lastname", query.toUpperCase()).findAll());
        return list;
    }

}
