package com.noneadstask.presenter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import com.noneadstask.interfaces.FavoritesList;
import com.noneadstask.model.Person;
import com.noneadstask.repository.FavoritesRepository;

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
                view.removeElement();
                //setFavoriteStatus(position, false);
            }
        }
        realm.commitTransaction();
    }

    @Override
    public void onPDFclick(Person person) {
        //to open declaration with google disk
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://docs.google.com/viewer?url=" + person.getLinkPDF()));

        //uncomment when need declaration downloading
        //Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(person.getLinkPDF()));
        browserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(browserIntent);
        Log.d(TAG, "Opening pdf from URL: " + person.getLinkPDF());
    }

    @Override
    public void saveComment(final String comment, final String id) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Person person = realm.where(Person.class).equalTo("id", id).findFirst();
                if (person == null) {
                    return;
                }
                person.setComment(comment);
            }
        });
    }

    @Override
    public void onLodingFinished(List<Person> list) {
        if (null != list) {
            view.refreshListFinished(list);
        } else {
            view.fetchListErrorListener();
        }
    }

    @Override
    public void onDestroy() {
        realm.close();
    }

}
