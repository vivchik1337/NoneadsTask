package com.noneadstask.interfaces;

import android.content.Context;

import com.noneadstask.model.Person;

import java.util.List;

import io.realm.Realm;

public interface FavoritesList {
    interface UIview {
        void fetchListErrorListener();

        void refreshListFinished(List<Person> newList);

        void refreshListStarted();

        void removeElement();
    }

    interface Presenter {
        void refreshList(String query);

        void onRemoveFromFavorite(Person person, int position);

        void onPDFclick(Person person);

        void saveComment(String comment, final String id);

        void onDestroy();
    }

    interface OnLoadingFinishedListener {
        void onLodingFinished(List<Person> list);
    }

    interface Repository {
        List<Person> loadList(String query, Context context, Realm realm, OnLoadingFinishedListener onLoadingFinishedListener);
    }
}
