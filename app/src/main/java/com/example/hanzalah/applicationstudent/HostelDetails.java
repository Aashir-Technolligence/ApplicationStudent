package com.example.hanzalah.applicationstudent;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class HostelDetails extends Fragment {

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    ArrayList<AddPacakgeAttr> pacakgeAttrs;
    ViewHolder adapter;
    RecyclerView recyclerView;
    TextView hostelName , txtRating , txtNum;
    ImageButton btnCall, btnMsg, btnEmail, btnDirection;
    String contact, email, locLat, locLong , hName;
    private LocationManager locationManager;
    RatingBar ratingBar;
    LinearLayout linearLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

            final String hostelId = getArguments().getString("HostelId");//id get
            getActivity().setTitle("Hostel details");
        // hostelId = getArguments().getBundle("HostelId");
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_hostel_details, container, false);
        hostelName = v.findViewById(R.id.txtName);
        recyclerView = v.findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        ratingBar =  v.findViewById(R.id.ratingBar);
        txtRating = v.findViewById(R.id.txtRating);
        txtNum = v.findViewById(R.id.txtNum);
        linearLayout = v.findViewById(R.id.linearRating);
        pacakgeAttrs = new ArrayList<AddPacakgeAttr>();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Packages");

        databaseReference.orderByChild("hostelId").equalTo(hostelId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                pacakgeAttrs.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    AddPacakgeAttr p = dataSnapshot1.getValue(AddPacakgeAttr.class);
                    pacakgeAttrs.add(p);
                }
                adapter = new ViewHolder(getContext(), pacakgeAttrs);
                recyclerView.setAdapter(adapter);


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getContext(), "Oppss.. Something went wrong", Toast.LENGTH_LONG).show();
            }
        });
        DatabaseReference hosteldb = firebaseDatabase.getReference("Hostel_Admin");
        hosteldb.child(hostelId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                contact = dataSnapshot.child("contact").getValue().toString();
                email = dataSnapshot.child("email").getValue().toString();
                locLat = dataSnapshot.child("locLatitude").getValue().toString();
                locLong = dataSnapshot.child("locLongitude").getValue().toString();
                hName = dataSnapshot.child("hostel_Name").getValue().toString();
                hostelName.setText(dataSnapshot.child("hostel_Name").getValue().toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final String userid = user.getUid();
        DatabaseReference booking = firebaseDatabase.getReference("Rating");

        booking.child(hostelId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                long count = dataSnapshot.getChildrenCount();
                Double total = 0.0;
                for (DataSnapshot adminsnapshot : dataSnapshot.getChildren()) {
                    Rating_Attr rating_attr = adminsnapshot.getValue(Rating_Attr.class);
                    total += rating_attr.getTotal();
                }
                String star = String.valueOf(new DecimalFormat("#.#").format(total/count));
                String str = String.valueOf(total/count);
                ratingBar.setRating(Float.parseFloat(str));
                //ratingBar.setNumStars((int) (total/count));
                txtRating.setText(star);
                txtNum.setText(String.valueOf(count));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        btnCall = v.findViewById(R.id.imgcall);
        btnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("tel:" + contact);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });
        btnMsg = v.findViewById(R.id.imgmsg);
        btnMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("tel:" + contact);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });
        btnEmail = v.findViewById(R.id.imgmail);
        btnEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:" + email));
                startActivity(Intent.createChooser(emailIntent, "Chooser Title"));
            }
        });
        btnDirection = v.findViewById(R.id.imgdirection);
        btnDirection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);

                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    getLocationPermission();
                }
                Location location = locationManager.getLastKnownLocation(locationManager.NETWORK_PROVIDER);
                String cLong = String.valueOf(location.getLongitude());
                String cLat = String.valueOf(location.getLatitude());

                Intent i = new Intent(getActivity(), MapsActivity.class);
                i.putExtra("Longitude", locLong);
                i.putExtra("Latitude", locLat);
                i.putExtra("DevLongitude", cLong);
                i.putExtra("DevLatitude", cLat);
                i.putExtra("Name", hName);
                startActivity(i);
            }
        });
        txtRating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fr = new Rating();
                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                Bundle args = new Bundle();
                args.putString("HostelId", hostelId);
                fr.setArguments(args);
                ft.replace(R.id.main_frame, fr);
                ft.commit();
            }
        });
        txtNum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fr = new RatingDetail();
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
    private void getLocationPermission() {
        if (ContextCompat.checkSelfPermission(getContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    1);
        }
    }
}
