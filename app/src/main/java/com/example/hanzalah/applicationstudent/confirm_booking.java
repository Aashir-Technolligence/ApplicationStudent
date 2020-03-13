package com.example.hanzalah.applicationstudent;


import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
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
public class confirm_booking extends Fragment {

    TextView hName  , sName, contact ,seats;
    Button btnCancel , btnConfirmBooking;
    FirebaseDatabase database;
    DatabaseReference reference;
    Notification builder;
    NotificationManager notificationManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final String hostelId = getArguments().getString("HostelId");//id get
        final String packageId = getArguments().getString("PackageId");
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_confirm_booking, container, false);
        getActivity().setTitle("Confirm booking");
        hName =v.findViewById(R.id.txtHName);
        sName =v.findViewById(R.id.txtSName);
        contact = v.findViewById(R.id.txtSContact);
        seats = v.findViewById(R.id.txtHSeats);

        database = FirebaseDatabase.getInstance();

        reference = database.getReference("Hostel_Admin");
        reference.child(hostelId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(getActivity()!=null) {
                    Hostel_Admin hostel_admin = dataSnapshot.getValue(Hostel_Admin.class);
                    if (hostel_admin != null) {
                        hName.setText(hostel_admin.getHostel_Name());
                        seats.setText("1");
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
        final String userId=user.getUid();
        database.getReference("Student").child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot1) {
                if(getActivity()!=null) {
                    Student_Attr student_attr = dataSnapshot1.getValue(Student_Attr.class);
                    if (student_attr != null) {
                        sName.setText(student_attr.getStudent_Name());
                        contact.setText(student_attr.getContact());
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        btnCancel = (Button) v.findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fr=new PackageDetail();
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
        btnConfirmBooking = (Button) v.findViewById(R.id.btnConfirmBooking);
        btnConfirmBooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(),"wait" , Toast.LENGTH_LONG);
                final String push = FirebaseDatabase.getInstance().getReference().child("Booking").push().getKey();

                Booking_Attr booking_attr = new Booking_Attr();
                booking_attr.setId(push);
                booking_attr.setHostelName(hName.getText().toString());
                booking_attr.setStudentName(sName.getText().toString());
                booking_attr.setStudentid(userId);
                booking_attr.setStudentContact(contact.getText().toString());
                booking_attr.setHostelId(hostelId);
                booking_attr.setStatus("Pending");
                booking_attr.setTime(System.currentTimeMillis());
                booking_attr.setPackageId(packageId);

                database.getReference("Booking").child(push)
                        .setValue(booking_attr);
                Toast.makeText(getContext(), "Your booking is recieved.", Toast.LENGTH_SHORT).show();

                database.getReference("Packages").child(packageId).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String seats = (String) dataSnapshot.child("noOfSeats").getValue();
                        int seat = Integer.parseInt(seats);
                        final int no = seat - 1;
                        String newSeats = String.valueOf(no);
                        database.getReference("Packages").child(packageId).child("noOfSeats").setValue(newSeats);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                String msg = "Please pay security fees online which is 10% of package. ";
                String user = sName.getText().toString();

                builder = new Notification.Builder(getActivity())
                        .setContentTitle(user +" your booking is pending").setContentText(msg).setSubText("Booking Successful")
                        .setSmallIcon(R.drawable.iconlogo)
                        .build();
              notificationManager =
                        (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.notify(0, builder);
                //sendSMS(no ,msg);

                String text = "Your booking is pending please pay security fees online which is 10% of package";
                String no = contact.getText().toString();
                sendSMS(no ,text);


                Fragment fr=new homeFragment();
                FragmentManager fm=getFragmentManager();
                FragmentTransaction ft=fm.beginTransaction();
                ft.replace(R.id.main_frame, fr);
                ft.commit();
            }
        });
        return v;
    }

    private void sendSMS(String contact, String msg) {
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(contact, null, msg, null, null);
        }
        catch (Exception e){
            Toast.makeText(getContext() , e.getMessage().toString() , Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }
}
