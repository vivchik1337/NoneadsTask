package com.noneadstask.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
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

public class ListAdapter extends BaseAdapter {

    public static String TAG = ListAdapter.class.getSimpleName();

    private Context context;
    private LayoutInflater mInflater;
    private List<Person> list = new ArrayList<Person>();

    private ListPresenter presenter;


    public class PersonViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.nameTextView)
        TextView nameTextView;
        @BindView(R.id.placeOfWorkTextView)
        TextView placeOfWorkTextView;
        @BindView(R.id.positionTextView)
        TextView positionTextView;
        @BindView(R.id.addToFavorite)
        ImageView addToFavorite;
        @BindView(R.id.openPDF)
        ImageView openPDF;

        public PersonViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            Log.d(TAG, "ViewHolder created");
        }

        @OnClick(R.id.addToFavorite)
        public void addToFavorite(View view) {

            final int position = (Integer) view.getTag();
            final Person item = list.get(position);

            presenter.onAddToFavoriteClick(item);
            setFavoriteStatus(true);
            Log.d(TAG, "Item " + "0000 " + "added to favorite");
        }

        public void setFavoriteStatus(boolean isFavorite) {
            if (isFavorite)
                addToFavorite.setBackground(context.getResources().getDrawable(R.drawable.ic_star_active));
            else
                addToFavorite.setBackground(context.getResources().getDrawable(R.drawable.ic_star));

        }

        @OnClick(R.id.openPDF)
        public void openPDF(View view) {
            Log.d(TAG, "pd");

            final int position = (Integer) view.getTag();
            final Person item = list.get(position);

            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://docs.google.com/viewer?url=" + item.getLinkPDF()));
            context.startActivity(browserIntent);
        }
    }

    public ListAdapter(Context context, List<Person> list, ListPresenter presenter) {
        this.context = context;
        this.list = list;
        this.presenter = presenter;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public Person getItem(int position) {
        return list.get(position);
    }

    public int getCount() {
        return list.size();
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        PersonViewHolder holder = null;
        /*
         * Create or restore ViewHolder
		 */
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_person, parent, false);
            holder = new PersonViewHolder(convertView);

            convertView.setTag(R.id.myId0, holder);
        } else {
            holder = (PersonViewHolder) convertView.getTag(R.id.myId0);
        }

        setItemData(position, holder);
        convertView.setTag(R.id.myId1, position);


        return convertView;
    }

    /*
     * Set item data
	 */
    public void setItemData(int position, PersonViewHolder holder) {
        Person item = list.get(position);
        holder.nameTextView.setText(item.getLastname() + " " + item.getFirstname());
        holder.placeOfWorkTextView.setText(item.getPlaceOfWork());
        holder.positionTextView.setText(item.getPosition());

        if (null != item.getLinkPDF() && !item.getLinkPDF().equals("") && !item.getLinkPDF().equalsIgnoreCase("null"))
            holder.openPDF.setTag(position);
        else
            holder.openPDF.setVisibility(View.GONE);

        holder.addToFavorite.setTag(position);

    }
}
