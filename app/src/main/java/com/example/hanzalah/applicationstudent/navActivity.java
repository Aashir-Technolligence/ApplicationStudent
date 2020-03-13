package com.example.hanzalah.applicationstudent;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.FrameLayout;

public class navActivity extends AppCompatActivity {
    private BottomNavigationView mMainNav;
    private FrameLayout mMainFrame; //Creating instance
    String hostelId = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav);

        Intent intentReceived = getIntent();
        Bundle data = intentReceived.getExtras();
        if(data != null){
            hostelId = data.getString("HostelId");
        }
//
//        Bundle bundle = getIntent().getExtras();
//        if (bundle != null)
//        {
//            hostelId = bundle.getString("HostelId");
//        }

        mMainFrame = (FrameLayout) findViewById(R.id.main_frame);
        mMainNav = (BottomNavigationView) findViewById(R.id.main_nav);
        //calling layout on start
        if(hostelId == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, new homeFragment()).commit();
        }
        else
            {

                HostelDetails hostelDetails = new HostelDetails ();
                Bundle args = new Bundle();
                args.putString("HostelId", hostelId);
                hostelDetails.setArguments(args);
                //Toast.makeText(getApplicationContext() , hostelId , Toast.LENGTH_LONG).show();
                getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, hostelDetails).commit();


            }
        //click on bottom navigation items
        mMainNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment =null;
                switch (item.getItemId()){
                    case R.id.navigation_home:
                        selectedFragment = new homeFragment();//creating constructor
                        break;

                    case R.id.navigation_search:
                        selectedFragment = new SearchFragment();
                        break;

                    case R.id.navigation_booking:
                        selectedFragment = new BookingFragment();
                        break;

                    case R.id.navigation_favourite:
                        selectedFragment = new FavouritesFragment();
                        break;

                    case R.id.nav_logout:
                                SharedPreferences settings = getSharedPreferences("Log", MODE_PRIVATE);
                                SharedPreferences.Editor editor = settings.edit();
                                editor.remove("isLoggedIn");
                                editor.commit();

                                Intent intent = new Intent(navActivity.this , Login.class);
                                startActivity(intent);
                                finish();
                        return false;
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.main_frame,selectedFragment).commit();

                return true;
            }
        });

    }

    @Override
    public void onBackPressed() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure want to exit ?");
        builder.setCancelable(true);
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
           dialogInterface.cancel();
            }
        });
        builder.setPositiveButton("Exit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
           finish();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
