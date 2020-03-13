package com.example.hanzalah.applicationstudent;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static android.support.v7.widget.RecyclerView.VERTICAL;


/**
 * A simple {@link Fragment} subclass.
 */
public class BookingFragment extends Fragment {
    ListView listView;

    FirebaseDatabase database;
    DatabaseReference reference;
    List<Booking_Attr> booking_list;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_booking, container, false);
        getActivity().setTitle("My bookings");
        listView = (ListView) v.findViewById(R.id.bookingList);

        booking_list = new ArrayList<>();//constructor
        //listView.setLayoutDirection();

        database = FirebaseDatabase.getInstance();  //database connectivity
        reference = database.getReference("Booking");//table name
        FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
        final String userId=user.getUid();
        reference.orderByChild("studentid").equalTo(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (getActivity() != null) {
                    booking_list.clear();
                    for (DataSnapshot adminsnapshot : dataSnapshot.getChildren()) {
                        Booking_Attr booking = adminsnapshot.getValue(Booking_Attr.class);
                        booking_list.add(booking);
                    }
                    bookinglist adapter = new bookinglist(getActivity(), booking_list);
                    listView.setAdapter(adapter);

                }


                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String bookingId = null;
                        bookingId = booking_list.get(position).getId();
                        String packageId = null;
                        String hostelId = null;
                        packageId = booking_list.get(position).getPackageId();
                        hostelId = booking_list.get(position).getHostelId();

                        Fragment fr = new booking_detail();
                        FragmentManager fm = getFragmentManager();
                        FragmentTransaction ft = fm.beginTransaction();
                        Bundle args = new Bundle();
                        args.putString("BookingId", bookingId);
                        args.putString("PackageId", packageId);
                        args.putString("HostelId", hostelId);
                        fr.setArguments(args);
                        ft.replace(R.id.main_frame, fr);
                        ft.commit();
                    }
                });

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return v;
    }

}
