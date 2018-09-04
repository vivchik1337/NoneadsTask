package com.noneadstask.adapter;

import android.content.Context;
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

public class ListAdapter extends BaseAdapter {

    public static String TAG = ListAdapter.class.getSimpleName();

    private Context context;
    private ListClicks listClicks;

    private LayoutInflater mInflater;

    private List<Person> list = new ArrayList<Person>();

    public class ViewHolder {
        public TextView nameTextView;
        public TextView infoTextView;
        public ImageView imgImageView;
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

        ViewHolder holder = null;

		/*
         * Create or restore ViewHolder
		 */
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_person, parent, false);
            holder = new ViewHolder();
            holder.nameTextView = (TextView) convertView.findViewById(R.id.nameTextView);
            holder.infoTextView = (TextView) convertView.findViewById(R.id.infoTextView);
            holder.imgImageView = (ImageView) convertView.findViewById(R.id.imgImageView);
            convertView.setTag(R.id.myId0, holder);
        } else {
            holder = (ViewHolder) convertView.getTag(R.id.myId0);
        }

		/*
         * Set item data
		 */
        Person item = list.get(position);
        holder.nameTextView.setText(item.getFirstname());
        holder.infoTextView.setText(item.getLastname());
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
