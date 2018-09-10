package com.noneadstask.presenter;

import android.content.Context;
import android.util.Log;

import com.noneadstask.adapter.FavoritesList;
import com.noneadstask.model.FavoritesRepository;
import com.noneadstask.model.Person;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by vivchik on 08.09.2018.
 */

public class FavoritesListPresenter implements FavoritesList.Presenter, FavoritesList.OnLoadingFinishedListener {

    public static final String TAG = FavoritesListPresenter.class.getSimpleName();


    private FavoritesList.UIview view;
    private FavoritesList.Repository repository;
    private Context context;
    private Realm realm;


    public FavoritesListPresenter(FavoritesList.UIview view, Context context) {
        this.view = view;
        this.context = context;
        this.repository = new FavoritesRepository();
        realm = Realm.getInstance(context);


        Log.d(TAG, "Constructor");
    }

    @Override
    public void refreshList(String query) {
        view.refreshListStarted();
        if (null == query)
            query = "";
        view.refreshListFinished(repository.loadList(query, context, realm, this));
    }

    @Override
    public void onRemoveFromFavorite(Person person, int position) {
        realm.beginTransaction();

        RealmResults<Person> favoritePersons = realm.where(Person.class).equalTo("id", person.getId()).findAll();
        if (!favoritePersons.isEmpty()) {
            //removing person from realm
            for (int i = favoritePersons.size() - 1; i >= 0; i--) {
                favoritePersons.get(i).removeFromRealm();
                //setFavoriteStatus(position, false);
            }
        }
        realm.commitTransaction();
    }

    @Override
    public void onPDFclick(Person person) {

    }

    @Override
    public void onDestroy() {
        realm.close();
    }

    @Override
    public void onLodingFinished(List<Person> list) {
        if (null != list) {
            view.refreshListFinished(list);
        } else {
            view.fetchListErrorListener();
        }
    }
}
