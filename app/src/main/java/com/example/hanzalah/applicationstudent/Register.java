package com.example.hanzalah.applicationstudent;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class Register extends AppCompatActivity {
    private Button btnRegister;
    private EditText name, password, contact, email;
    FirebaseAuth auth;
    Student_Attr student_attr;
    String Name, Password, Email, Contact;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        this.setTitle("Register");
        btnRegister = (Button) findViewById(R.id.btnRegister);
        name = (EditText) findViewById(R.id.edtName);
        password = (EditText) findViewById(R.id.edtHPassword);
        contact = (EditText) findViewById(R.id.edtContact);
        email = (EditText) findViewById(R.id.edtEmail);
        progressBar = (ProgressBar) findViewById(R.id.registerProgress);
        progressBar.setVisibility(View.GONE);
        auth = FirebaseAuth.getInstance();
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Name = name.getText().toString().trim();
                Password = password.getText().toString().trim();
                Contact = contact.getText().toString().trim();
                Email = email.getText().toString().trim();

                if (!Name.isEmpty() && !Password.isEmpty() && !Contact.isEmpty() && !Email.isEmpty() )
                {
                    if(Email.contains("@")||Email.contains(".com")) {
                        progressBar.setVisibility(View.VISIBLE);

                         FirebaseAuth.getInstance().createUserWithEmailAndPassword(Email, Password)
                                 .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    student_attr = new Student_Attr();
                                    student_attr.setId(FirebaseAuth.getInstance().getCurrentUser().getUid());
                                    student_attr.setStudent_Name(Name);
                                    student_attr.setPassword(Password);
                                    student_attr.setContact(Contact);
                                    student_attr.setEmail(Email);

                                    FirebaseDatabase.getInstance().getReference()
                                            .child("Student")
                                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                            .setValue(student_attr);
                                    Toast.makeText(getApplicationContext(), "Welcome " + Name, Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(Register.this, navActivity.class);
                                    startActivity(intent);
                                    finish();
                                    SharedPreferences.Editor editor = getSharedPreferences("Log", MODE_PRIVATE).edit();
                                    editor.putBoolean("isLoggedIn", true);
                                    editor.commit();



                                } else {
                                    progressBar.setVisibility(View.GONE);
                                    Toast.makeText(Register.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }

                            }
                        });
                    }
                    else
                    {
                        Toast.makeText(Register.this, "Invalid Email", Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    Toast.makeText(Register.this, "Please Enter all Information", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}
