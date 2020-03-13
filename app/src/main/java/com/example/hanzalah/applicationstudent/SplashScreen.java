package com.example.hanzalah.applicationstudent;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SplashScreen extends AppCompatActivity {
Handler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        getSupportActionBar().hide();
        handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                SharedPreferences prefs = getSharedPreferences("Log", MODE_PRIVATE);
                boolean isLoggedIn = prefs.getBoolean("isLoggedIn", false);
                if (isLoggedIn) {
                    Intent intent=new Intent(SplashScreen.this,navActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Intent intent=new Intent(SplashScreen.this,Login.class);
                    startActivity(intent);
                    finish();
                }
            }
        },3000);
    }
}
