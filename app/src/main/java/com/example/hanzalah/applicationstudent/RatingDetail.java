package com.example.hanzalah.applicationstudent;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class RatingDetail extends Fragment {
    RatingBar security , food , building , staff;
    Button back;
    TextView txtSecuirty , txtFood , txtBuilding , txtStaff;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    ListView listView;

    List<Rating_Attr> rating_list;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final String hostelId = getArguments().getString("HostelId");//id get
        View v = inflater.inflate(R.layout.fragment_rating_detail, container, false);
        building = v.findViewById(R.id.ratingBarBuilding);
        food = v.findViewById(R.id.ratingBarFood);
        security = v.findViewById(R.id.ratingBarSecurity);
        staff = v.findViewById(R.id.ratingBarStaff);
        txtBuilding = v.findViewById(R.id.txtBuilding);
        txtFood = v.findViewById(R.id.txtFood);
        txtSecuirty = v.findViewById(R.id.txtSecurity);
        txtStaff = v.findViewById(R.id.txtStaff);
        listView = v.findViewById(R.id.reviewList);
        back = v.findViewById(R.id.btnBack);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference= firebaseDatabase.getReference("Rating");
        rating_list = new ArrayList<>();//constructor
        databaseReference.child(hostelId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                long count = dataSnapshot.getChildrenCount();
                Double securityV = 0.0;
                Double foodV = 0.0;
                Double staffV = 0.0;
                Double buildingV = 0.0;
                for (DataSnapshot adminsnapshot : dataSnapshot.getChildren()) {
                    Rating_Attr rating_attr = adminsnapshot.getValue(Rating_Attr.class);
                    securityV += rating_attr.getSecurity();
                    foodV += rating_attr.getFood();
                    buildingV += rating_attr.getBuilding();
                    staffV += rating_attr.getStaff();

                }
                String starB = String.valueOf(new DecimalFormat("#.#").format(buildingV/count));
                String strB = String.valueOf(buildingV/count);
                building.setRating(Float.parseFloat(strB));
                txtBuilding.setText(starB);

                String starF = String.valueOf(new DecimalFormat("#.#").format(foodV/count));
                String strF = String.valueOf(foodV/count);
                food.setRating(Float.parseFloat(strF));
                txtFood.setText(starF);

                String starSt = String.valueOf(new DecimalFormat("#.#").format(staffV/count));
                String strSt = String.valueOf(staffV/count);
                staff.setRating(Float.parseFloat(strSt));
                txtStaff.setText(starSt);

                String starS = String.valueOf(new DecimalFormat("#.#").format(securityV/count));
                String strS = String.valueOf(securityV/count);
                security.setRating(Float.parseFloat(strS));
                txtSecuirty.setText(starS);




            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        databaseReference.child(hostelId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (getActivity() != null) {
                    rating_list.clear();
                    for (DataSnapshot adminsnapshot : dataSnapshot.getChildren()) {
                        Rating_Attr rating_attr = adminsnapshot.getValue(Rating_Attr.class);
                        rating_list.add(rating_attr);
                    }
                    reviewList adapter = new reviewList(getActivity(), rating_list);
                    listView.setAdapter(adapter);

                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fr = new HostelDetails();
                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                Bundle args = new Bundle();
                args.putString("HostelId", hostelId);
                fr.setArguments(args);
                ft.replace(R.id.main_frame, fr);
                ft.commit();
            }
        });
        return v;
    }

}
