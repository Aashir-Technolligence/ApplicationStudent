package com.example.hanzalah.applicationstudent;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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
public class updateprofileFragment extends Fragment {
    FirebaseDatabase database;
    DatabaseReference reference;
    private Button btnUpdate;

    private EditText name , password   , contact , email;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_updateprofile, container, false);
        getActivity().setTitle("Update profile");
        btnUpdate = (Button) v.findViewById(R.id.btnUpdateProfile);

        name = (EditText) v.findViewById(R.id.edtHostelName);
        password = (EditText) v.findViewById(R.id.edtHPassword);
        contact = (EditText) v.findViewById(R.id.edtContact);
        email = (EditText) v.findViewById(R.id.edtEmail);

        database = FirebaseDatabase.getInstance();
        reference = database.getReference("Student");
        FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
        final String userId=user.getUid();
        reference.child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Student_Attr student_attr =  dataSnapshot.getValue(Student_Attr.class);

                name.setText(student_attr.getStudent_Name());
                password.setText(student_attr.getPassword());
                email.setText(student_attr.getEmail());
                contact.setText(student_attr.getContact());

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!name.getText().toString().isEmpty() && !password.getText().toString().isEmpty() && !contact.getText().toString().isEmpty() && !email.getText().toString().isEmpty()) {

                    reference.child(userId).child("student_Name").setValue(name.getText().toString().trim());
                    reference.child(userId).child("password").setValue(password.getText().toString().trim());
                    reference.child(userId).child("email").setValue(email.getText().toString().trim());
                    reference.child(userId).child("contact").setValue(contact.getText().toString().trim());

                    FavouritesFragment profile_fragment = new FavouritesFragment();
                    FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.main_frame , profile_fragment ,profile_fragment.getTag()).commit();
            }
                else
            {
                Toast.makeText(getActivity(), "Please Enter all Information", Toast.LENGTH_SHORT).show();
            }
            }
        });
        return v;
    }

}
