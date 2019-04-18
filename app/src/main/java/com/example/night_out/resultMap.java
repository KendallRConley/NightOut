package com.example.night_out;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Objects;

public class resultMap extends FragmentActivity implements OnMapReadyCallback {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Bundle b = getIntent().getExtras();
        assert b != null;
        if (!b.getString("oneName").equals("Loading") && !b.getString("oneName").equals(getString(R.string.no_results_found))){
            String[] latlng = Objects.requireNonNull(b.getString("oneCoords")).split(",");
            LatLng loc1 = new LatLng(Double.valueOf(latlng[0]), Double.valueOf(latlng[1].substring(1)));
            googleMap.addMarker(new MarkerOptions().position(loc1).title(b.getString("oneName")));
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(loc1));
        }

        if (!b.getString("twoName").equals("Loading") && !b.getString("twoName").equals(getString(R.string.no_results_found))){
            String[] latlng = Objects.requireNonNull(b.getString("twoCoords")).split(",");
            LatLng loc2 = new LatLng(Double.valueOf(latlng[0]), Double.valueOf(latlng[1].substring(1)));
            googleMap.addMarker(new MarkerOptions().position(loc2).title(b.getString("twoName")));
        }
        if (!b.getString("threeName").equals("Loading") && !b.getString("threeName").equals(getString(R.string.no_results_found))){
            String[] latlng = Objects.requireNonNull(b.getString("threeCoords")).split(",");
            LatLng loc3 = new LatLng(Double.valueOf(latlng[0]), Double.valueOf(latlng[1].substring(1)));
            googleMap.addMarker(new MarkerOptions().position(loc3).title(b.getString("threeName")));
        }
    }
}
