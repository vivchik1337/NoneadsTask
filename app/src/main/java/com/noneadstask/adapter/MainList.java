package com.noneadstask.adapter;

import android.content.Context;

import com.noneadstask.model.Person;

import java.util.List;

public interface MainList {
    interface UIview {
        void fetchListErrorListener();
        void refreshListFinished(List<Person> newList);
        void refreshListStarted();
        void showFavoriteStatus();
    }

    interface Presenter {
        void refreshList(String query);
        void onAddToFavoriteClick(Person person);
        void onPDFclick(Person person);
        void onDestroy();
    }

    interface OnLoadingFinishedListener{
        void onLodingFinished(List<Person> list);
    }

    interface Repository {
        String loadList(String query, Context context, OnLoadingFinishedListener onLoadingFinishedListener);
    }
    /*interface Addapter{
        void changeFavoriteStatus(boolean isFavorite);
    }*/
}
