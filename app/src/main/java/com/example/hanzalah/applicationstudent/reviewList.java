package com.example.hanzalah.applicationstudent;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class reviewList extends ArrayAdapter<Rating_Attr> {
    private Activity context;
    private List<Rating_Attr> ratingAttrs;
    //constructor
    public reviewList(Activity context , List<Rating_Attr> ratingAttrs){

        super(context , R.layout.review , ratingAttrs);
        this.context = context;
        this.ratingAttrs = ratingAttrs;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();

        View listViewItem = inflater.inflate(R.layout.review , null , true);

        TextView comnt = (TextView) listViewItem.findViewById(R.id.comnt);
        TextView user = (TextView) listViewItem.findViewById(R.id.user);
        //hostel admin ka constructor
        Rating_Attr rating_attr = ratingAttrs.get(position);
        comnt.setText(rating_attr.getComment());
        user.setText(String.valueOf(rating_attr.getTotal()));

        return listViewItem;
    }
}
