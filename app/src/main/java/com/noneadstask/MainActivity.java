package com.noneadstask;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.noneadstask.fragment.BaseFragment;
import com.noneadstask.fragment.ListFragment;
import com.noneadstask.util.Toaster;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    public Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        BaseFragment.openFragment(getSupportFragmentManager(), R.id.frameLayout, ListFragment.getInstance(), false);

    }

    // ------------------ Menu item logic  ------------------

    private boolean addMenuItemVisible = true;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        MenuItem addMenuItem = menu.findItem(R.id.favorites);
        addMenuItem.setVisible(addMenuItemVisible);
        return true;
    }

    public void hideAddMenuItem() {
        addMenuItemVisible = false;
        invalidateOptionsMenu();
    }

    public void showAddMenuItem() {
        addMenuItemVisible = true;
        invalidateOptionsMenu();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.favorites) {
            Toaster.showToast(getApplicationContext(), "CLicked");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
