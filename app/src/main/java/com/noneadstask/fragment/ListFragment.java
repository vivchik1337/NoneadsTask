package com.noneadstask.fragment;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.noneadstask.R;
import com.noneadstask.adapter.ListAdapter;
import com.noneadstask.api.ApiSingleton;
import com.noneadstask.api.ListRequest;
import com.noneadstask.interfaces.MainList;
import com.noneadstask.model.Person;
import com.noneadstask.presenter.ListPresenter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;

public class ListFragment extends BaseFragment implements MainList.UIview,
        SwipeRefreshLayout.OnRefreshListener {
    public static ListFragment listFragment;

    public static ListFragment getInstance() {
        if (null == listFragment) listFragment = new ListFragment();
        return listFragment;
    }

    public static final String TAG = ListFragment.class.getSimpleName();

    @BindView(R.id.editSearch)
    public EditText editSearch;
    @BindView(R.id.recyclerView)
    public RecyclerView recyclerView;
    @BindView(R.id.btnReload)
    public Button btnReload;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.listEmpty)
    public View listEmpty;

    ListPresenter presenter;
    Realm realm;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView");
        // create layouts
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        unbinder = ButterKnife.bind(this, view);

        presenter = new ListPresenter(this, getActivity().getApplicationContext());

        realm = Realm.getInstance(context);

        editSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    presenter.refreshList(editSearch.getText().toString().trim());
                    hideKeyboard(editSearch);
                }
                return false;
            }
        });

        initSwipeRefresh();

        if (savedInstanceState != null) {
            query = savedInstanceState.getString("query");
            editSearch.setText(query);
        }
        presenter.refreshList(query);

        mainActivity.getSupportActionBar().setTitle("Search");
        mainActivity.showAddMenuItem();

        return view;
    }

    @Override
    public void refreshListStarted() {
        btnReload.setVisibility(View.INVISIBLE);
        listEmpty.setVisibility(View.INVISIBLE);
        isLoad = true;
        swipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void setFavoriteStatus(int position, boolean isFavorite) {
        adapter.setFavoriteStatus(position, isFavorite);
    }

    private ListAdapter adapter;

    private boolean isLoad = false;

    private String query = "";

    @OnClick(R.id.btnReload)
    public void btnReload() {
        Log.d(TAG, "btnReload");
        presenter.refreshList(editSearch.getText().toString().trim());
    }

    @OnClick(R.id.btnSearch)
    public void btnSearch() {
        Log.d(TAG, "btnSearch");
        presenter.refreshList(editSearch.getText().toString().trim());
    }

    private void initSwipeRefresh() {
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
    }

    @Override
    public void onRefresh() {
        Log.d(TAG, "onRefresh");
        hideKeyboard(editSearch);
        editSearch.setText("");
        btnReload();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("query", query);
    }

    @Override
    public void onDestroyView() {
        Log.d(TAG, "onDestroyView");
        super.onDestroyView();
        unbinder.unbind();
        if (isLoad) {
            ApiSingleton.getInstance(context).getRequestQueue().cancelAll(ListRequest.TAG);
        }
    }

    private RecyclerView.LayoutManager layoutManager;

    @Override
    public void refreshListFinished(List<Person> newList) {
        isLoad = false;
        swipeRefreshLayout.setRefreshing(false);

        recyclerView.setVisibility(View.VISIBLE);
        btnReload.setVisibility(View.INVISIBLE);

        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        adapter = new ListAdapter(context, newList, presenter, recyclerView, realm);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void fetchListErrorListener() {
        isLoad = false;
        swipeRefreshLayout.setRefreshing(false);
        recyclerView.setVisibility(View.INVISIBLE);
        btnReload.setVisibility(View.VISIBLE);
        listEmpty.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
    }
}
