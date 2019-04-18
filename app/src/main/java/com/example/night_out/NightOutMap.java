package com.example.night_out;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class NightOutMap extends FragmentActivity implements OnMapReadyCallback {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_night_out_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Bundle getLocation = getIntent().getExtras();
        assert getLocation != null;
        double usr_lat = getLocation.getDouble("latString");
        double usr_long = getLocation.getDouble("lonString");
        // Add a marker for the user's choice and move the camera
        LatLng usr_choice = new LatLng(usr_lat, usr_long);
        googleMap.addMarker(new MarkerOptions().position(new LatLng(usr_lat, usr_long)).title("Marker"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(usr_choice));
    }
}
