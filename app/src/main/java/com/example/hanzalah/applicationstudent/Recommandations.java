package com.example.hanzalah.applicationstudent;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Recommandations extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener,LocationListener {

    private GoogleMap mMap;
    FirebaseDatabase database;
    DatabaseReference reference;
    Marker marker;
    private LocationManager locationManager;
    double LocLongitude;
    double LocLatitude;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommandations);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        ChildEventListener mChildEventListener;
        database = FirebaseDatabase.getInstance();
        reference = database.getReference("Hostel_Admin");
        reference.push().setValue(marker);
       // Toast.makeText(getApplicationContext() ,"Please long click the area", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            getLocationPermission();
        }
        Location location = locationManager.getLastKnownLocation(locationManager.NETWORK_PROVIDER);

        onLocationChanged(location);
        googleMap.setOnMarkerClickListener(this);
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot s : dataSnapshot.getChildren()){
                    Hostel_Admin user = s.getValue(Hostel_Admin.class);
                    LatLng location=new LatLng(user.getLocLatitude(),user.getLocLongitude());
                    mMap.addMarker(new MarkerOptions().position(location).title(user.getId())).hideInfoWindow();

                }
                mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker) {
                        marker.hideInfoWindow();


                        String hostelId = marker.getTitle();

                        Intent intent = new Intent (Recommandations.this, navActivity.class);
                        Bundle data1 = new Bundle();
                        data1.putString("HostelId",hostelId);
                        intent.putExtras(data1);
                        startActivity(intent);
                        return false;
                    }
                });

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

//        final Circle[] circle = new Circle[1];
//        //final CircleOptions circleOptions = new CircleOptions();
//        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
//            @Override
//            public void onMapLongClick(LatLng latLng) {
//                if(circle[0] != null)
//                    circle[0].remove();
//                circle[0] = mMap.addCircle(new CircleOptions()
//                        .center(latLng)
//                        .radius(1000)
//                        .strokeWidth(3f)
//                        .strokeColor(Color.BLUE)
//                        .fillColor(Color.TRANSPARENT));
//
//
//
//            }
//        });

    }


    @Override
    public boolean onMarkerClick(Marker marker) {

        return false;
    }

    @Override
    public void onLocationChanged(Location location) {
        LocLatitude = location.getLatitude();
        LocLongitude = location.getLongitude();
        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(LocLatitude, LocLongitude);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        CameraPosition camPos = new CameraPosition.Builder()
                .target(sydney)
                .zoom(16)
                .bearing(location.getBearing())
                .build();
        CameraUpdate camUpd3 = CameraUpdateFactory.newCameraPosition(camPos);
        mMap.animateCamera(camUpd3);

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
    private void getLocationPermission() {
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    1);
        }
    }

}
