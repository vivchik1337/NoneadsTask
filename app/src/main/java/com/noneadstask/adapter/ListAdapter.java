package com.noneadstask.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.noneadstask.R;
import com.noneadstask.model.Person;
import com.noneadstask.presenter.ListPresenter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;

public class ListAdapter extends RecyclerView.Adapter {

    public static String TAG = ListAdapter.class.getSimpleName();

    private Context context;
    private LayoutInflater mInflater;
    private List<Person> list = new ArrayList<Person>();
    private List<Person> favoritesList = new ArrayList<>();
    private ListPresenter presenter;


    public class PersonViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.nameTextView)
        TextView nameTextView;
        @BindView(R.id.placeOfWorkTextView)
        TextView placeOfWorkTextView;
        @BindView(R.id.positionTextView)
        TextView positionTextView;
        @BindView(R.id.favorite)
        ImageView favorite;
        @BindView(R.id.openPDF)
        ImageView openPDF;
        @BindView(R.id.btnFavorite)
        FrameLayout btnFavorite;
        @BindView(R.id.btnOpenPDF)
        FrameLayout btnOpenPDF;
        @BindView(R.id.comment)
        EditText comment;

        public PersonViewHolder(final View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            Log.d(TAG, "ViewHolder created");

            comment.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                        Person person = list.get((int) itemView.findViewById(R.id.favorite).getTag());
                        presenter.saveComment(comment.getText().toString().trim(), person.getId());
                        hideKeyboard(comment);
                    }
                    return false;
                }
            });
        }

        @OnClick(R.id.favorite)
        public void onFavoriteClick(View view) {

            final int position = (Integer) view.getTag();
            final Person item = list.get(position);
            Log.d(TAG, "Item " + item.getId() + "added to favorite");

            presenter.onFavoriteClick(item, position);
        }

        @OnClick(R.id.btnOpenPDF)
        public void btnOpenPDF(View view) {
            Log.d(TAG, "openPDF");

            final int position = (Integer) view.getTag();
            final Person item = list.get(position);
            presenter.onPDFclick(item);
        }
    }

    RecyclerView recyclerView;
    Realm realm;

    public ListAdapter(Context context, List<Person> list, ListPresenter presenter, RecyclerView recyclerView, Realm realm) {
        this.context = context;
        this.list = list;
        this.presenter = presenter;
        this.recyclerView = recyclerView;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        this.realm = realm;
        favoritesList = realm.where(Person.class).findAll();
    }

    public void setFavoriteStatus(int position, boolean isFavorite) {
        PersonViewHolder holder = (PersonViewHolder) recyclerView.findViewHolderForAdapterPosition(position);

        if (null != holder)
            if (isFavorite) {
                holder.favorite.setBackground(context.getResources().getDrawable(R.drawable.ic_star_active));
                holder.comment.setVisibility(View.VISIBLE);
            } else {
                holder.favorite.setBackground(context.getResources().getDrawable(R.drawable.ic_star));
                holder.comment.setVisibility(View.GONE);
            }
    }

    @Override
    public ListAdapter.PersonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View convertView = mInflater.inflate(R.layout.item_person, parent, false);
        PersonViewHolder holder = new PersonViewHolder(convertView);

        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        setItemData(position, (PersonViewHolder) holder);
    }


    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    /*
     * Set item data
	 */
    public void setItemData(int position, PersonViewHolder holder) {
        Person item = list.get(position);
        holder.nameTextView.setText(item.getLastname() + " " + item.getFirstname());
        holder.placeOfWorkTextView.setText(item.getPlaceOfWork());
        holder.positionTextView.setText(item.getPosition());

        if (null != item.getLinkPDF() && !item.getLinkPDF().equals("") && !item.getLinkPDF().equalsIgnoreCase("null")) {
            holder.openPDF.setTag(position);
            holder.btnOpenPDF.setTag(position);
        } else {
            holder.openPDF.setVisibility(View.GONE);
            holder.btnOpenPDF.setVisibility(View.GONE);
        }

        for (Person person : favoritesList) {
            if (person.getId().equalsIgnoreCase(item.getId())) {
                holder.favorite.setBackground(context.getResources().getDrawable(R.drawable.ic_star_active));
                holder.comment.setVisibility(View.VISIBLE);
                holder.comment.setText(person.getComment());
                break;
            }
            else{
                holder.favorite.setBackground(context.getResources().getDrawable(R.drawable.ic_star));
                holder.comment.setVisibility(View.GONE);
                holder.comment.setText("");
            }
        }
        holder.favorite.setTag(position);
        holder.btnFavorite.setTag(position);
    }

    public void hideKeyboard(EditText editText) {
        try {
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
        } catch (Exception e) {
            com.noneadstask.util.Log.d(TAG, e.getLocalizedMessage());
        }
    }
}

