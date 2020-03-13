package com.example.hanzalah.applicationstudent;


import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFareFragment extends Fragment {
    EditText searchtext;
    ArrayList<AddPacakgeAttr> pacakgeAttrs;
    PostListAdapter adapter;
    ListView recyclerView;
    Button recommend , search , pack;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_search_fare, container, false);
        getActivity().setTitle("Search by fare");
        searchtext= v.findViewById(R.id.find);
        pacakgeAttrs = new ArrayList<AddPacakgeAttr>();
        recyclerView=v.findViewById(R.id.searchList);
        initTextListener();
        search = v.findViewById(R.id.btnFilter);
        recommend = v.findViewById(R.id.btnRecommend);
        pack = v.findViewById(R.id.btnHostel);
        recommend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLocationPermission();
                Intent i = new Intent(getActivity(), Recommandations.class);
                startActivity(i);
            }
        });
        pack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SearchFragment searchPackageFragment = new SearchFragment();
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
        pacakgeAttrs.clear();
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
        pacakgeAttrs.clear();
        updatePostList();
        if(keyword.length() ==0)
        {
            return;
        }

        else
        {
            Query query1 = FirebaseDatabase.getInstance().getReference("Packages")
                    .orderByChild("fare")
                    .startAt(keyword)
                    .endAt(keyword+"\uf8ff");
            query1.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot)
                {
                    pacakgeAttrs.clear();
                    for(DataSnapshot singleSnapshot :  dataSnapshot.getChildren())
                    {
                        if(!pacakgeAttrs.contains(singleSnapshot.getValue(AddPacakgeAttr.class)))
                        {
                            pacakgeAttrs.add(singleSnapshot.getValue(AddPacakgeAttr.class));
                        }

                    }
                    try{
                        updatePostList();
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

    private void updatePostList() {

        adapter = new PostListAdapter(getContext() , R.layout.post, pacakgeAttrs );
        recyclerView.setAdapter(adapter);

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
