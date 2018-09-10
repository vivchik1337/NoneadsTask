package com.noneadstask.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.noneadstask.R;
import com.noneadstask.model.Person;
import com.noneadstask.presenter.FavoritesListPresenter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FavoritesListAdapter extends RecyclerView.Adapter {

    public static String TAG = FavoritesListAdapter.class.getSimpleName();

    private Context context;
    private LayoutInflater mInflater;
    private List<Person> list = new ArrayList<Person>();

    private FavoritesListPresenter presenter;


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

        public PersonViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            Log.d(TAG, "ViewHolder created");
        }

        @OnClick(R.id.favorite)
        public void onFavoriteClick(View view) {

            final int position = (Integer) view.getTag();
            final Person item = list.get(position);

            presenter.onRemoveFromFavorite(item, position);
            Log.d(TAG, "Item " + item.getId() + "added to favorite");
        }

        @OnClick(R.id.openPDF)
        public void openPDF(View view) {
            Log.d(TAG, "openPDF");

            final int position = (Integer) view.getTag();
            final Person item = list.get(position);
            presenter.onPDFclick(item);
        }
    }

    RecyclerView recyclerView;

    public FavoritesListAdapter(Context context, List<Person> list, FavoritesListPresenter presenter, RecyclerView recyclerView) {
        this.context = context;
        this.list = list;
        this.presenter = presenter;
        this.recyclerView = recyclerView;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setFavoriteStatus(int position, int resID) {
        ((FavoritesListAdapter.PersonViewHolder) recyclerView.findViewHolderForAdapterPosition(position)).favorite.setBackground(context.getResources().getDrawable(resID));

    }

    @Override
    public FavoritesListAdapter.PersonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View convertView = mInflater.inflate(R.layout.item_person, parent, false);
        FavoritesListAdapter.PersonViewHolder holder = new FavoritesListAdapter.PersonViewHolder(convertView);

        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        setItemData(position, (FavoritesListAdapter.PersonViewHolder) holder);
    }


    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        if (null == list) return 0;
        return list.size();
    }

    /*
     * Set item data
	 */
    public void setItemData(int position, FavoritesListAdapter.PersonViewHolder holder) {
        Person item = list.get(position);
        holder.nameTextView.setText(item.getLastname() + " " + item.getFirstname());
        holder.placeOfWorkTextView.setText(item.getPlaceOfWork());
        holder.positionTextView.setText(item.getPosition());

        if (null != item.getLinkPDF() && !item.getLinkPDF().equals("") && !item.getLinkPDF().equalsIgnoreCase("null"))
            holder.openPDF.setTag(position);
        else
            holder.openPDF.setVisibility(View.GONE);

        holder.favorite.setTag(position);

    }
}