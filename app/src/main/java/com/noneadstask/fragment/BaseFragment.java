package com.noneadstask.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.noneadstask.MainActivity;
import com.noneadstask.util.Log;

import java.util.UUID;

import butterknife.Unbinder;

/*
 * Created by viktord (viktor.derk@gmail.com) on 19/6/2018.
 */

public abstract class BaseFragment extends Fragment {

    public static final String TAG = BaseFragment.class.getSimpleName();

    protected Context context;
    protected MainActivity mainActivity;
    protected FragmentManager fragmentManager;
    protected Unbinder unbinder;

    public BaseFragment() {
        // add other config here ...
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity().getApplicationContext();
        if (getActivity() instanceof MainActivity) {
            mainActivity = (MainActivity) getActivity();
        }
        fragmentManager = getActivity().getSupportFragmentManager();
    }

    // --------------------------------- Open logic ---------------------------

    public String uuid = UUID.randomUUID().toString();

    public static void openFragment(FragmentManager fragmentManager, int frameLayoutId, BaseFragment fragment, boolean isAddToBackStack) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(frameLayoutId, fragment, fragment.uuid);
        if (isAddToBackStack) fragmentTransaction.addToBackStack(null);
        // add other config here ...
        fragmentTransaction.commit();
    }

    // --------------------- Keyboard ------------------

    public void hideKeyboard(EditText editText) {
        try {
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
        } catch (Exception e) {
            Log.d(TAG, e.getLocalizedMessage());
        }
    }

    public void showKeyboard(EditText editText) {
        try {
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInputFromWindow(editText.getApplicationWindowToken(), InputMethodManager.SHOW_FORCED, 0);
        } catch (Exception e) {
            Log.d(TAG, e.getLocalizedMessage());
        }
    }
}
