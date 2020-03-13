package com.example.hanzalah.applicationstudent;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Hanzalah on 3/7/2019.
 */

public class UserListAdapter extends ArrayAdapter<Hostel_Admin> {
    private LayoutInflater mInflater;
    private List<Hostel_Admin> mUsers = null;
    private int layoutResource;
    private Context mContext;

    public UserListAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<Hostel_Admin> objects) {
        super(context, resource, objects);

        mContext = context;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layoutResource = resource;
        this.mUsers = objects;
    }

    private static class ViewHolder{
        TextView username;
        TextView type;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        final ViewHolder holder;

        if(convertView == null)
        {
            convertView = mInflater.inflate(layoutResource, parent, false);

            holder = new ViewHolder();

            holder.username = (TextView) convertView.findViewById(R.id.user);
            holder.type = (TextView) convertView.findViewById(R.id.type);

            convertView.setTag(holder);


        }else{

            holder = (ViewHolder) convertView.getTag();
        }

        holder.username.setText(getItem(position).getHostel_Name());
        holder.type.setText(getItem(position).getHostel_Type());
        return convertView;
    }
}
