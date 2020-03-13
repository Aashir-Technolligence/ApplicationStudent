package com.example.hanzalah.applicationstudent;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;

import java.math.BigDecimal;


/**
 * A simple {@link Fragment} subclass.
 */
public class PaymentFragment extends Fragment {

    private Button btnPay;
    private EditText amount;
    ProgressBar progressBar;
    String hId, pId, bId, Amount , id ,sN , hN , c , s;
    FirebaseDatabase database;
    DatabaseReference reference;

    public static final int Paypal_Request_Code = 123;
    private static PayPalConfiguration configuration = new PayPalConfiguration()
            .environment(PayPalConfig.CONFIG_ENVIROMENT)
            .clientId(PayPalConfig.PAYPAL_CLIENT_ID);

    @Override
    public void onDestroy() {
        getActivity().stopService(new Intent(getActivity() , PayPalService.class));
        super.onDestroy();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final String hostelId = getArguments().getString("HostelId");//id get
        final String packageId = getArguments().getString("PackageId");
        final String bookingId = getArguments().getString("BookingId");
        final String studentName = getArguments().getString("StudentName");
        final String hostelName = getArguments().getString("HostelName");
        final String contact = getArguments().getString("Contact");
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_payment, container, false);
        hId = hostelId;
        pId = packageId;
        bId = bookingId;
        sN = studentName;
        hN = hostelName;
        c= contact;
        btnPay = (Button) v.findViewById(R.id.btnPay);
        amount = (EditText) v.findViewById(R.id.edtAmount);
        progressBar = v.findViewById(R.id.registerProgress);
        progressBar.setVisibility(View.GONE);
        database = FirebaseDatabase.getInstance();
        reference = database.getReference("Packages");
        reference.child(packageId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                AddPacakgeAttr addPacakgeAttr =  dataSnapshot.getValue(AddPacakgeAttr.class);
                String am = addPacakgeAttr.getFare();
                Float a = Float.valueOf(am);
                amount.setText(String.valueOf(a/1300));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        Intent intent = new Intent(getActivity(), PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, configuration);
        getActivity().startService(intent);

        btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //progressBar.setVisibility(View.VISIBLE);

                processPayment();
            }
        });
        return v;
    }

    private void processPayment() {
        Amount = amount.getText().toString().trim();
        PayPalPayment payPalPayment = new PayPalPayment(new BigDecimal(String.valueOf(Amount)),"USD", "Payment for booking",
                PayPalPayment.PAYMENT_INTENT_SALE);
        Intent intent = new Intent(getActivity() , PaymentActivity.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, configuration);
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT , payPalPayment);
        startActivityForResult(intent , Paypal_Request_Code);


        }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == Paypal_Request_Code){
            if(resultCode == Activity.RESULT_OK){
                PaymentConfirmation confirmation = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                if(confirmation != null){
                    try {
                        String paymentDetails = confirmation.toJSONObject().toString(4);
                        startActivity(new Intent(getActivity(), ConfirmationActivity.class)
                                .putExtra("PaymentDetails", paymentDetails)
                                .putExtra("PaymentAmount", Amount)
                                .putExtra("hostelId" , hId)
                                .putExtra("packageId" , pId)
                                .putExtra("bookingId" , bId)
                                .putExtra("StudentName" ,sN )
                                .putExtra("HostelName" ,hN )
                                .putExtra("Contact" ,c ));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
            else if(requestCode == Activity.RESULT_CANCELED)
                Toast.makeText(getContext() , "Cancel" , Toast.LENGTH_LONG).show();
        }
        else if(requestCode == PaymentActivity.RESULT_EXTRAS_INVALID)
            Toast.makeText(getContext() , "Invalid" , Toast.LENGTH_LONG).show();
    }
}
