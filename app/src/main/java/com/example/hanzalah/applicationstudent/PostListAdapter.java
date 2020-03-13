package com.example.hanzalah.applicationstudent;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class PostListAdapter extends ArrayAdapter<AddPacakgeAttr> {

    private LayoutInflater mInflater;
    private ArrayList<AddPacakgeAttr> mUsers = null;
    private int layoutResource;
    private Context mContext;
    public PostListAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull ArrayList<AddPacakgeAttr> objects) {
        super(context, resource, objects);

        mContext = context;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layoutResource = resource;
        this.mUsers = objects;
    }

    private static class ViewHolder{
        TextView postType , postSeats , postFare , postTime;
        ImageView postimage;
    }


    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        final PostListAdapter.ViewHolder holder;

        if(convertView == null)
        {
            convertView = mInflater.inflate(layoutResource, parent, false);

            holder = new PostListAdapter.ViewHolder();

            holder.postType = (TextView) convertView.findViewById(R.id.postType);
            holder.postFare = (TextView) convertView.findViewById(R.id.postFare);
            holder.postSeats = (TextView) convertView.findViewById(R.id.postSeats);
            holder.postimage = (ImageView) convertView.findViewById(R.id.postPic);
            holder.postTime = (TextView) convertView.findViewById(R.id.postTime);
            convertView.setTag(holder);


        }else{

            holder = (PostListAdapter.ViewHolder) convertView.getTag();
        }
        Picasso.get().load(getItem(position).getImage_url()).into(holder.postimage);//show pic
        holder.postType.setText(getItem(position).getPackageType());
        holder.postSeats.setText(getItem(position).getNoOfSeats());
        holder.postFare.setText(getItem(position).getFare());
        String timestampDifference = getTimestampDifference(getItem(position));
        holder.postTime.setText(timestampDifference);

        holder.postimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String packageId = null;
                String hostelId = null;
                hostelId = getItem(position).getHostelId();
                packageId = getItem(position).getId();
                //Toast.makeText(v.getContext() , hostelId , Toast.LENGTH_LONG).show();

                navActivity activity = (navActivity) v.getContext();
                Fragment fr = new PackageDetail();
                FragmentManager fm = activity.getSupportFragmentManager();
                FragmentTransaction ft=fm.beginTransaction();
                Bundle args = new Bundle();
                args.putString("PackageId", packageId);
                args.putString("HostelId" , hostelId);
                fr.setArguments(args);
                ft.replace(R.id.main_frame, fr);
                ft.commit();

            }
        });
        return convertView;
    }
    private String getTimestampDifference(AddPacakgeAttr addPacakgeAttr)
    {

        long difference = addPacakgeAttr.getTime();
        String result="";

        result=TimeCheck.getTimeAgo(difference);
        return  result;

    }
}
