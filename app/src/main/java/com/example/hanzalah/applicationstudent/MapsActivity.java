package com.example.hanzalah.applicationstudent;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener{

    private GoogleMap mMap;
    MarkerOptions current , destination;
    String name , deviceLatitude , deviceLongitude , longitude , latitude;
    Double lat1 , long1 , lat2 , long2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        longitude = getIntent().getStringExtra("Longitude");
        latitude = getIntent().getStringExtra("Latitude");
        deviceLongitude = getIntent().getStringExtra("DevLongitude");
        deviceLatitude = getIntent().getStringExtra("DevLatitude");
        name = getIntent().getStringExtra("Name");
        destination = new MarkerOptions().position(new LatLng(Double.parseDouble(latitude) , Double.parseDouble(longitude))).title(name);
        current = new MarkerOptions().position(new LatLng(Double.parseDouble(deviceLatitude) , Double.parseDouble(deviceLongitude))).title("Your position");

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        lat1 = Double.parseDouble(deviceLatitude);
        long1 = Double.parseDouble(deviceLongitude);
        lat2 = Double.parseDouble(latitude);
        long2 = Double.parseDouble(longitude);

        mMap.addMarker(new MarkerOptions().position(new LatLng(lat2 , long2)).title(name)).setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));

        LatLng isb = new LatLng(lat2, long2);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(isb));
        CameraPosition camPos = new CameraPosition.Builder()
                .target(isb)
                .zoom(10)
                .build();
        CameraUpdate camUpd3 = CameraUpdateFactory.newCameraPosition(camPos);
        mMap.animateCamera(camUpd3);

        googleMap.setOnMarkerClickListener(this);
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }
}
