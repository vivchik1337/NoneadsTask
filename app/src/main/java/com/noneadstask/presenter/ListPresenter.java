package com.noneadstask.presenter;

import android.content.Context;
import android.util.Log;

import com.noneadstask.adapter.MainList;
import com.noneadstask.model.Person;
import com.noneadstask.model.PersonRepository;

import java.util.List;

/**
 * Created by vivchik on 08.09.2018.
 */

public class ListPresenter implements MainList.Presenter, MainList.OnLoadingFinishedListener {

    public static final String TAG = ListPresenter.class.getSimpleName();


    private MainList.UIview view;
    private MainList.Repository repository;
    private Context context;


    public ListPresenter(MainList.UIview view, Context context) {
        this.view = view;
        this.context = context;
        this.repository = new PersonRepository();
        Log.d(TAG, "Constructor");
    }

    @Override
    public void refreshList(String query) {
        view.refreshListStarted();
        if (null == query)
            query = "";
        repository.loadList(query, context, this);
    }

    DBHelper dbHelper;
    @Override
    public void onAddToFavoriteClick(Person person) {
    if(dbHelper == null)
        dbHelper = new DBHelper(this);
    }

    @Override
    public void onPDFclick() {

    }

    @Override
    public void onDestroy() {

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
