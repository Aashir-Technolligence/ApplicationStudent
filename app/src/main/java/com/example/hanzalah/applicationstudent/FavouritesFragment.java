package com.example.hanzalah.applicationstudent;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

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
public class FavouritesFragment extends Fragment {

    TextView name , contact , email  , password;
    Button btnEdit;
    FirebaseDatabase database;
    DatabaseReference reference;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_favourites, container, false);
        getActivity().setTitle("Profile");
        name = v.findViewById(R.id.txtSName);
        contact = v.findViewById(R.id.txtSContact);
        email = v.findViewById(R.id.txtSEmail);

        database = FirebaseDatabase.getInstance();
        reference = database.getReference("Student");
        FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
        final String userId=user.getUid();
        reference.child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Student_Attr student_attr =  dataSnapshot.getValue(Student_Attr.class);

                name.setText(student_attr.getStudent_Name());
                email.setText(student_attr.getEmail());
                contact.setText(student_attr.getContact());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        btnEdit = v.findViewById(R.id.btnUpdate);

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateprofileFragment updateprofile_fragment = new updateprofileFragment();
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.main_frame , updateprofile_fragment ,updateprofile_fragment.getTag()).commit();

            }
        });

        return v;
    }

}
