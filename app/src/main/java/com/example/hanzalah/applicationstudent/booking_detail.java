package com.example.hanzalah.applicationstudent;


import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;


/**
 * A simple {@link Fragment} subclass.
 */
public class booking_detail extends Fragment {

    TextView packageType , fare  , parking ,  electricity , mess , laundary , ac , cctv , internet , status , hostelName;
    Button btnDelete , btnPay;
    ImageView packageImage;
    FirebaseDatabase database;
    DatabaseReference reference;
    public String studentName , hName , contact ,s ;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final String hostelId = getArguments().getString("HostelId");//id get
        final String packageId = getArguments().getString("PackageId");
        final String bookingId = getArguments().getString("BookingId");

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_booking_detail, container, false);
        getActivity().setTitle("Booking detail");
        packageType = v.findViewById(R.id.txtType);//design view get
        fare = v.findViewById(R.id.txtFare);
        parking = v.findViewById(R.id.txtParking);
        electricity = v.findViewById(R.id.txtElectricity);
        mess = v.findViewById(R.id.txtMess);
        laundary = v.findViewById(R.id.txtLaundary);
        ac = v.findViewById(R.id.txtAC);
        cctv = v.findViewById(R.id.txtCctv);
        internet = v.findViewById(R.id.txtInternet);
        packageImage = v.findViewById(R.id.packageImg);
        btnDelete= v.findViewById(R.id.btnDltBooking);
        btnPay = v.findViewById(R.id.btnPayCharges);
        status = v.findViewById(R.id.txtStatus);
        hostelName = v.findViewById(R.id.txtHName);
        database = FirebaseDatabase.getInstance();
        reference = database.getReference("Booking");
        reference.child(bookingId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(getActivity()!=null) {
                    Booking_Attr booking_attr = dataSnapshot.getValue(Booking_Attr.class);
                    if (booking_attr != null) {
                        hostelName.setText(booking_attr.getHostelName());
                        status.setText(booking_attr.getStatus());
                        hName = booking_attr.getHostelName();
                        s = booking_attr.getStatus().toString().trim();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        if(s == "Succeed")
            btnPay.setVisibility(View.GONE);
        database = FirebaseDatabase.getInstance();
        reference = database.getReference("Packages");
        reference.child(packageId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(getActivity()!=null) {
                    AddPacakgeAttr pacakgeAttr = dataSnapshot.getValue(AddPacakgeAttr.class);
                    if (pacakgeAttr != null) {
                        packageType.setText(pacakgeAttr.getPackageType());//value show on txt view
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
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("Are you sure want to delete?");
                builder.setCancelable(true);
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        database.getReference("Booking").child(bookingId).setValue(null);
                        database.getReference("Packages").child(packageId).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                String seats = (String) dataSnapshot.child("noOfSeats").getValue();
                                int seat = Integer.parseInt(seats);
                                final int no = seat + 1;
                                String newSeats = String.valueOf(no);
                                database.getReference("Packages").child(packageId).child("noOfSeats").setValue(newSeats);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                        BookingFragment bookingFragment = new BookingFragment();
                        FragmentManager fragmentManager = getFragmentManager();
                        fragmentManager.beginTransaction().replace(R.id.main_frame , bookingFragment ,bookingFragment.getTag()).commit();

                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();

            }
        });
        FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
        final String userId=user.getUid();
        database.getReference("Student").child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Student_Attr student_attr =  dataSnapshot.getValue(Student_Attr.class);

                 studentName = student_attr.getStudent_Name();
                 contact = student_attr.getContact();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fr = new PaymentFragment();
                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                Bundle args = new Bundle();
                args.putString("BookingId", bookingId);
                args.putString("PackageId", packageId);
                args.putString("HostelId", hostelId);
                args.putString("StudentName", studentName);
                args.putString("HostelName", hName);
                args.putString("Contact", contact);
                fr.setArguments(args);
                ft.replace(R.id.main_frame, fr);
                ft.commit();
            }
        });
        return v;
    }

}
