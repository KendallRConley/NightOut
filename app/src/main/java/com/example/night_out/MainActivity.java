package com.example.night_out;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    public static final int MY_PERMISSIONS_REQUEST_FINE_LOCATION = 1;

    TextView address_text;

    //hamburger drawer members
    private ListView mDrawerList;//the list view
    private ArrayAdapter<String> mAdapter;//adapts strings to drawer items
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private String mActivityTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //hamburger drawer code
        mDrawerList = (ListView)findViewById(R.id.navList);//make the drawer
        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);//store the layout
        mActivityTitle = getTitle().toString();//store the title

        addDrawerItems();//populate the drawer
        setupDrawer();//setup the title changing when drawer is open/close

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//enables the drawer button
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu);//sets the icon
        getSupportActionBar().setHomeButtonEnabled(true);//same

        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_FINE_LOCATION );

                // MY_PERMISSIONS_REQUEST_FINE_LOCATION is an
                // app-defined int constant. The callback method gets the
                // result of the request
        }
        else {
            // Permission has already been granted
            //Gets GPS latitude and longitude location
            LocationManager lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
            Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            double latitude, longitude;
            if (location != null) {
                longitude = location.getLongitude();
                latitude = location.getLatitude();
            } else {
                latitude = 38.0406;
                longitude = -84.5037;
            }

            String loc = getAddress(latitude, longitude);
            address_text = findViewById(R.id.address_text);
            address_text.setText(loc);
        }
    }

    //Gets address given lat and long.
    public String getAddress(double lat, double lng){
        String fullAdd=null;
        try {
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);

            if(addresses.size() >0){
                Address address = addresses.get(0);
                fullAdd = address.getAddressLine(0);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return fullAdd;
    }

    //defines and adds items to the drawer, sets demo listeners
    private void addDrawerItems() {
        String[] osArray = {"Login", "History", "Destination Options", "Settings"};//str array of items
        mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, osArray);
        mDrawerList.setAdapter(mAdapter);

        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {//adds tap listener
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(MainActivity.this, "Page NYI!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //changes the title bar based on drawer status
    private void setupDrawer() {//this activity, drawer's layout, drawable icon, accessibility strings
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.drawer_open, R.string.drawer_close) {

            //alled when a drawer has settled in a completely open state
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getSupportActionBar().setTitle("Hamburger Drawer");
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            //Called when a drawer has settled in a completely closed state
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getSupportActionBar().setTitle(mActivityTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        mDrawerToggle.setDrawerIndicatorEnabled(true);//enables the drawer button
        mDrawerLayout.addDrawerListener(mDrawerToggle);//registers this listener
    }

    @Override//override to enable title bar toggle
    public boolean onOptionsItemSelected(MenuItem item) {

        // Activate the navigation drawer toggle
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_FINE_LOCATION : {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // location-related task you need to do
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }
}
