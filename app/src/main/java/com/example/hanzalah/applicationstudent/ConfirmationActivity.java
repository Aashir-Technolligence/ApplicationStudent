package com.example.hanzalah.applicationstudent;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

public class ConfirmationActivity extends AppCompatActivity {
    String hostelId , packageId , bookingId , hostelName , studentName , amount , contact;
    private FirebaseDatabase database;
    DatabaseReference reference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation);
        this.setTitle("Payment detail");
        Intent intent = getIntent();
        hostelId = intent.getStringExtra("hostelId");
        packageId = intent.getStringExtra("packageId");
        bookingId = intent.getStringExtra("bookingId");
        amount = intent.getStringExtra("PaymentAmount");
        studentName = intent.getStringExtra("StudentName");
        hostelName = intent.getStringExtra("HostelName");
        contact = intent.getStringExtra("Contact");
        database = FirebaseDatabase.getInstance();  //database connectivity
        reference = database.getReference("Booking");//table name
        reference.child(bookingId).child("status").setValue("Succeed");


        final String push = FirebaseDatabase.getInstance().getReference().child("Payment").push().getKey();
        Payment payment = new Payment();
        payment.setAmount(amount);
        payment.setHostelName(hostelName);
        payment.setStudentName(studentName);
        payment.setId(push);
        payment.setStudentContact(contact);
        database.getReference("Payment").child(push)
                .setValue(payment);
        String text = "Hy "+studentName+" your payment is recieved. You have to pay remaining amount at the time of arrival.";
        String no = contact;
        sendSMS(no ,text);

        try {
            JSONObject jsonDetails = new JSONObject(intent.getStringExtra("PaymentDetails"));

            //Displaying payment details
            showDetails(jsonDetails.getJSONObject("response"), intent.getStringExtra("PaymentAmount"));
        } catch (JSONException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    private void sendSMS(String contact, String msg) {
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(contact, null, msg, null, null);
        }
        catch (Exception e){
            Toast.makeText(getApplicationContext(), e.getMessage().toString() , Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }
    private void showDetails(JSONObject jsonDetails, String paymentAmount) throws JSONException {
        //Views
        TextView textViewId = (TextView) findViewById(R.id.paymentId);
        TextView textViewStatus= (TextView) findViewById(R.id.paymentStatus);
        TextView textViewAmount = (TextView) findViewById(R.id.paymentAmount);

        //Showing the details from json object
        textViewId.setText(jsonDetails.getString("id"));
        textViewStatus.setText(jsonDetails.getString("state"));
        textViewAmount.setText(paymentAmount+" USD");
    }
}
