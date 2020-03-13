package com.example.hanzalah.applicationstudent;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


/**
 * A simple {@link Fragment} subclass.
 */
public class Rating extends Fragment {

RatingBar  security , food , building , staff;
Float total , securityv , foodv , buildingv , staffv;
Button rate;
EditText comnt;
String sName;
    FirebaseDatabase database;
    DatabaseReference reference;
    int i = 0;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final String hostelId = getArguments().getString("HostelId");//id get
        getActivity().setTitle("Rate hostel");
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_rating, container, false);
        building = v.findViewById(R.id.ratingBarBuilding);
        food = v.findViewById(R.id.ratingBarFood);
        security = v.findViewById(R.id.ratingBarSecurity);
        staff = v.findViewById(R.id.ratingBarStaff);
        comnt = v.findViewById(R.id.edtComnt);
        rate = v.findViewById(R.id.btnRate);

        database = FirebaseDatabase.getInstance();
        FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
        final String userId=user.getUid();
        database.getReference("Student").child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Student_Attr student_attr =  dataSnapshot.getValue(Student_Attr.class);

                sName = student_attr.getStudent_Name();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        rate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                buildingv = building.getRating();
                securityv = security.getRating();
                foodv = food.getRating();
                staffv = staff.getRating();
                total = (buildingv + securityv + foodv + staffv) /4;
                final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                final String userid = user.getUid();
                Rating_Attr rating_attr = new Rating_Attr();
                rating_attr.setStudentId(userid);
                rating_attr.setHostelId(hostelId);
                rating_attr.setTotal(total);
                rating_attr.setFood(foodv);
                rating_attr.setSecurity(securityv);
                rating_attr.setStaff(staffv);
                rating_attr.setBuilding(buildingv);
                rating_attr.setComment(comnt.getText().toString().trim());

//                database.getReference("Payment").orderByChild("studentName").equalTo(sName).addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                       if(dataSnapshot.exists())
//                           i =1;
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                    }
//                });
//                if(i == 1)
                {database.getReference("Rating").child(hostelId).child(userid)
                        .setValue(rating_attr);}
                Toast.makeText(getContext(), "Your review is recieved.", Toast.LENGTH_SHORT).show();

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
