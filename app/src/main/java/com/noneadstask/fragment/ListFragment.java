package com.noneadstask.fragment;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Editable;
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

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.noneadstask.R;
import com.noneadstask.adapter.ListAdapter;
import com.noneadstask.adapter.ListClicks;
import com.noneadstask.api.ApiSingleton;
import com.noneadstask.api.ListRequest;
import com.noneadstask.model.Person;
import com.noneadstask.util.SearchTextWatcher;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ListFragment extends BaseFragment implements ListClicks, SwipeRefreshLayout.OnRefreshListener {

    public static ListFragment getInstance(boolean isMyPets) {
        ListFragment listFragment = new ListFragment();
        if (isMyPets) {
            Bundle bundle = new Bundle();
            bundle.putSerializable("isMyPets", isMyPets);
            listFragment.setArguments(bundle);
        }
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

    private boolean isMyPets = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView");
        // create layouts
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        unbinder = ButterKnife.bind(this, view);

        editSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    hideKeyboard(editSearch);
                }
                return false;
            }
        });

        editSearch.addTextChangedListener(new SearchTextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                waitQuery = s.toString().trim();
            }
        });

        initSwipeRefresh();

        if (getArguments() != null) {
            isMyPets = getArguments().getBoolean("isMyPets");
        }

        if (savedInstanceState != null) {
            query = savedInstanceState.getString("query");
            editSearch.setText(query);
        }
        if (query.isEmpty()) {
            listView.setVisibility(View.INVISIBLE);
            btnReload.setVisibility(View.INVISIBLE);
            listEmpty.setVisibility(View.INVISIBLE);
        }
        fetchList(true, query);

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (isRun) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (!isLoad && !waitQuery.equals(query)) {
                        query = waitQuery;
                        mainActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                fetchList(false, query);
                            }
                        });
                    }
                }
            }
        }).start();

        if (isMyPets) {
            mainActivity.toolbar.setTitle("Пошук");
        } else {
            mainActivity.toolbar.setTitle(R.string.app_name);
        }

        return view;
    }

    private void fetchList(boolean isHideListView, String query) {
        if (isHideListView) {
            listView.setVisibility(View.INVISIBLE);
        } else {
            listView.setVisibility(View.VISIBLE);
        }
        btnReload.setVisibility(View.INVISIBLE);
        listEmpty.setVisibility(View.INVISIBLE);
        // showWaitDialog();
        isLoad = true;
        swipeRefreshLayout.setRefreshing(true);
        new ListRequest(context, null, query, new Response.Listener<List<Person>>() {
            @Override
            public void onResponse(List<Person> response) {
                isLoad = false;
                swipeRefreshLayout.setRefreshing(false);
                ListFragment.this.response = response;
                initList();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                isLoad = false;
                swipeRefreshLayout.setRefreshing(false);
                listView.setVisibility(View.INVISIBLE);
                btnReload.setVisibility(View.VISIBLE);
                listEmpty.setVisibility(View.INVISIBLE);
            }
        });
    }

    private List<Person> response;
    private ListAdapter adapter;

    private boolean isRun = true;
    private boolean isLoad = false;

    private String query = "";
    private String waitQuery = "";

    private void initList() {
        listView.setVisibility(View.VISIBLE);
        btnReload.setVisibility(View.INVISIBLE);
        if (response.isEmpty()) {
            listEmpty.setVisibility(View.VISIBLE);
        } else {
            listEmpty.setVisibility(View.INVISIBLE);
        }
        adapter = new ListAdapter(context, this, response);
        listView.setAdapter(adapter);
    }
/*
    @Override
    public void onClick(Person person) {
        Log.d(TAG, "onClick Pet");
        hideKeyboard(editSearch);
        //TODO make correct check
        if (1>2) {
            BaseFragment.openFragment(fragmentManager, R.id.frameLayout, PetAddEditFragment.getInstance(pet), true);
        } else {
            BaseFragment.openFragment(fragmentManager, R.id.frameLayout, PetFragment.getInstance(pet), true);
        }
    }*/

    @OnClick(R.id.btnReload)
    public void btnReload() {
        Log.d(TAG, "btnReload");
        fetchList(true, editSearch.getText().toString().trim());
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
    public void onFavoriteClick(Person person) {

    }

    @Override
    public void onCommentClick(Person person) {

    }

    @Override
    public void onSearch(String searchText) {

    }
}
