package com.example.night_out;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class NightOutMap extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_night_out_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Bundle getLocation = getIntent().getExtras();
        double usr_lat = getLocation.getDouble("latString");
        double usr_long = getLocation.getDouble("lonString");
        // Add a marker for the user's choice and move the camera
        LatLng usr_choice = new LatLng(usr_lat, usr_long);
        mMap.addMarker(new MarkerOptions().position(new LatLng(usr_lat,usr_long)).title(""));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(usr_choice));
    }

    /*
     * A demo class that stores and retrieves data objects with each marker.
     */
}
/*
    public class MarkerDemoActivity extends FragmentActivity implements
            OnMarkerClickListener,
            OnMapReadyCallback {

        private final LatLng PERTH = new LatLng(-31.952854, 115.857342);
        private final LatLng SYDNEY = new LatLng(-33.87365, 151.20689);
        private final LatLng BRISBANE = new LatLng(-27.47093, 153.0235);


        private Marker mPerth;
        private Marker mSydney;
        private Marker mBrisbane;

        private GoogleMap mMap;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_night_out_map.xml);

            SupportMapFragment mapFragment =
                    (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);
        }

/* Called when the map is ready. */
/*
        @Override
        public void onMapReady(GoogleMap map) {
            mMap = map;

            // Add some markers to the map, and add a data object to each marker.
            mPerth = mMap.addMarker(new MarkerOptions()
                    .position(PERTH)
                    .title("Perth");
            mPerth.setTag(0);

            mSydney = mMap.addMarker(new MarkerOptions()
                    .position(SYDNEY)
                    .title("Sydney");
            mSydney.setTag(0);

            mBrisbane = mMap.addMarker(new MarkerOptions()
                    .position(BRISBANE)
                    .title("Brisbane");
            mBrisbane.setTag(0);

            // Set a listener for marker click.
            mMap.setOnMarkerClickListener(this);
        }
*/
/*

        @Override
        public boolean onMarkerClick(final Marker marker) {

            // Retrieve the data from the marker.
            Integer clickCount = (Integer) marker.getTag();

            // Check if a click count was set, then display the click count.
            if (clickCount != null) {
                clickCount = clickCount + 1;
                marker.setTag(clickCount);
                Toast.makeText(this,
                        marker.getTitle() +
                                " has been clicked " + clickCount + " times.",
                        Toast.LENGTH_SHORT).show();
            }

            // Return false to indicate that we have not consumed the event and that we wish
            // for the default behavior to occur (which is for the camera to move such that the
            // marker is centered and for the marker's info window to open, if it has one).
            return false;
        }
    }
*/
/*Called when the user clicks a marker. */

