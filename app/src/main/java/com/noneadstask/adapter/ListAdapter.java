package com.noneadstask.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.noneadstask.R;
import com.noneadstask.model.Person;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ListAdapter extends BaseAdapter {

    public static String TAG = ListAdapter.class.getSimpleName();

    private Context context;
    private ListClicks listClicks;

    private LayoutInflater mInflater;

    private List<Person> list = new ArrayList<Person>();

    public class PersonViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.nameTextView)
        TextView nameTextView;
        @BindView(R.id.placeOfWorkTextView)
        TextView placeOfWorkTextView;
        @BindView(R.id.positionTextView)
        TextView positionTextView;

        @BindView(R.id.addToFavorite)
        ImageView addToFavorite;

        public PersonViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick(R.id.addToFavorite)
        public void addToFavorite(View view) {
            addToFavorite.setBackground(context.getResources().getDrawable(R.drawable.ic_star_active));
        }
    }

    public ListAdapter(Context context, ListClicks listClicks, List<Person> list) {

        this.context = context;
        this.listClicks = listClicks;
        this.list = list;

        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setList(List<Person> list) {
        this.list = list;
        notifyDataSetChanged();
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

		/*
         * Set item data
		 */
        Person item = list.get(position);
        holder.nameTextView.setText(item.getLastname() + " " + item.getFirstname());
        holder.placeOfWorkTextView.setText(item.getPlaceOfWork());
        holder.positionTextView.setText(item.getPosition());
        /*
         * Set actions
		 */
        convertView.setTag(R.id.myId1, position);
        convertView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                int pos = (Integer) v.getTag(R.id.myId1); // poz
                Person selectedItem = list.get(pos);
                if (listClicks != null) {
                    listClicks.onCommentClick(selectedItem);
                }
            }
        });

        return convertView;
    }
}
