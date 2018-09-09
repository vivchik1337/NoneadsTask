package com.noneadstask.fragment;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.noneadstask.R;
import com.noneadstask.adapter.ListAdapter;
import com.noneadstask.adapter.MainList;
import com.noneadstask.api.ApiSingleton;
import com.noneadstask.api.ListRequest;
import com.noneadstask.model.Person;
import com.noneadstask.presenter.ListPresenter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

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
    @BindView(R.id.listView)
    public ListView listView;
    @BindView(R.id.btnReload)
    public Button btnReload;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.listEmpty)
    public View listEmpty;


    ListPresenter presenter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView");
        // create layouts
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        unbinder = ButterKnife.bind(this, view);

        presenter = new ListPresenter(this, getActivity().getApplicationContext());

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

        //mainActivity.toolbar.setTitle("Пошук");

        return view;
    }
    @Override
    public void refreshListStarted() {
        btnReload.setVisibility(View.INVISIBLE);
        listEmpty.setVisibility(View.INVISIBLE);

        isLoad = true;
        swipeRefreshLayout.setRefreshing(true);
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

    @Override
    public void refreshListFinished(List<Person> newList) {
        isLoad = false;
        swipeRefreshLayout.setRefreshing(false);

        listView.setVisibility(View.VISIBLE);
        btnReload.setVisibility(View.INVISIBLE);

        adapter = new ListAdapter(context, newList, presenter);
        listView.setAdapter(adapter);
    }
    @Override
    public void fetchListErrorListener() {
        isLoad = false;
        swipeRefreshLayout.setRefreshing(false);
        listView.setVisibility(View.INVISIBLE);
        btnReload.setVisibility(View.VISIBLE);
        listEmpty.setVisibility(View.INVISIBLE);

    }

    @Override
    public void showFavoriteStatus() {

    }
}
