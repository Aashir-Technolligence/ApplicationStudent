package com.example.hanzalah.applicationstudent;


import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends Fragment {

    List<Hostel_Admin> models= new ArrayList<>();
    EditText searchtext;
    UserListAdapter mAdapter;
    ListView list;
    Button recommend , search , fare;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_search, container, false);
        getActivity().setTitle("Search by hostel name");
        searchtext= v.findViewById(R.id.find);
        list=v.findViewById(R.id.searchList);
        initTextListener();
        search = v.findViewById(R.id.btnFilter);
        recommend = v.findViewById(R.id.btnRecommend);
        fare = v.findViewById(R.id.btnFare);
        recommend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLocationPermission();
                Intent i = new Intent(getActivity(), Recommandations.class);
                startActivity(i);
            }
        });
        fare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SearchFareFragment searchPackageFragment = new SearchFareFragment();
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.main_frame , searchPackageFragment ,searchPackageFragment.getTag()).commit();

            }
        });
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SearchPackageFragment searchPackageFragment = new SearchPackageFragment();
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.main_frame , searchPackageFragment ,searchPackageFragment.getTag()).commit();

            }
        });
        return v;
    }

    private void initTextListener() {
        models.clear();
        searchtext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String text = searchtext.getText().toString();
                searchForMatch(text);
            }
        });
    }

    private void searchForMatch(String keyword) {
        models.clear();
        updateUsersList();
        if(keyword.length() ==0)
        {
            return;
        }

        else
        {
            Query query = FirebaseDatabase.getInstance().getReference("Hostel_Admin")
                    .orderByChild("hostel_Name")
                    .startAt(keyword)
                    .endAt(keyword+"\uf8ff");
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot)
                {
                    models.clear();
                    for(DataSnapshot singleSnapshot :  dataSnapshot.getChildren())
                    {
                        if(!models.contains(singleSnapshot.getValue(Hostel_Admin.class)))
                        {
                            models.add(singleSnapshot.getValue(Hostel_Admin.class));
                        }

                    }
                    try{
                        updateUsersList();
                    }
                    catch (Exception ex)
                    {

                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            Query query1 = FirebaseDatabase.getInstance().getReference("Hostel_Admin")
                    .orderByChild("hostel_Type")
                    .startAt(keyword)
                    .endAt(keyword+"\uf8ff");
            query1.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot)
                {
                    models.clear();
                    for(DataSnapshot singleSnapshot :  dataSnapshot.getChildren())
                    {
                        if(!models.contains(singleSnapshot.getValue(Hostel_Admin.class)))
                        {
                            models.add(singleSnapshot.getValue(Hostel_Admin.class));
                        }

                    }
                    try{
                        updateUsersList();
                    }
                    catch (Exception ex)
                    {

                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }
    }

    private void updateUsersList() {

        mAdapter = new UserListAdapter(getContext(), R.layout.search_listview, models);
        list.setAdapter(mAdapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String hostelId = null;
                hostelId = models.get(position).getId();

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
    }
    private void getLocationPermission() {
        if (ContextCompat.checkSelfPermission(this.getContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this.getActivity(),
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    1);
        }
    }
}
