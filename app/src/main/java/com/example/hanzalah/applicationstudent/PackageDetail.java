package com.example.hanzalah.applicationstudent;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;


/**
 * A simple {@link Fragment} subclass.
 */
public class PackageDetail extends Fragment {
    TextView packageType , seats , fare  , parking ,  electricity , mess , laundary , ac , cctv , internet ;
    Button btnBook , btnViewHostel;
    ImageView packageImage;
    FirebaseDatabase database;
    DatabaseReference reference;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final String hostelId = getArguments().getString("HostelId");//id get
        final String packageId = getArguments().getString("PackageId");
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_package_detail, container, false);
        getActivity().setTitle("Package details");
        packageType = v.findViewById(R.id.txtType);//design view get
        seats = v.findViewById(R.id.txtSeats);
        fare = v.findViewById(R.id.txtFare);
        parking = v.findViewById(R.id.txtParking);
        electricity = v.findViewById(R.id.txtElectricity);
        mess = v.findViewById(R.id.txtMess);
        laundary = v.findViewById(R.id.txtLaundary);
        ac = v.findViewById(R.id.txtAC);
        cctv = v.findViewById(R.id.txtCctv);
        internet = v.findViewById(R.id.txtInternet);
        packageImage = v.findViewById(R.id.packageImg);
        btnViewHostel = v.findViewById(R.id.btnViewHostel);

        database = FirebaseDatabase.getInstance();
        reference = database.getReference("Packages");
        reference.child(packageId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(getActivity()!=null) {
                    AddPacakgeAttr pacakgeAttr = dataSnapshot.getValue(AddPacakgeAttr.class);
                    if (pacakgeAttr != null) {
                        packageType.setText(pacakgeAttr.getPackageType());//value show on txt view
                        seats.setText(pacakgeAttr.getNoOfSeats());
                        fare.setText(pacakgeAttr.getFare());
                        parking.setText(pacakgeAttr.getParking());
                        electricity.setText(pacakgeAttr.getElectricity());
                        mess.setText(pacakgeAttr.getMess());
                        laundary.setText(pacakgeAttr.getLaundary());
                        ac.setText(pacakgeAttr.getAc());
                        cctv.setText(pacakgeAttr.getCctv());
                        internet.setText(pacakgeAttr.getInternet());
                        Picasso.get().load(pacakgeAttr.getImage_url()).into(packageImage);

                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        btnViewHostel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fr=new HostelDetails();
                FragmentManager fm=getFragmentManager();
                FragmentTransaction ft=fm.beginTransaction();
                Bundle args = new Bundle();
                args.putString("HostelId", hostelId);
                fr.setArguments(args);
                ft.replace(R.id.main_frame, fr);
                ft.commit();
            }
        });
        btnBook = v.findViewById(R.id.btnBookPackage);
        btnBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fr=new confirm_booking();
                FragmentManager fm=getFragmentManager();
                FragmentTransaction ft=fm.beginTransaction();
                Bundle args = new Bundle();
                args.putString("HostelId", hostelId);
                args.putString("PackageId" , packageId);
                fr.setArguments(args);
                ft.replace(R.id.main_frame, fr);
                ft.commit();
            }
        });
        return v;
    }

}
