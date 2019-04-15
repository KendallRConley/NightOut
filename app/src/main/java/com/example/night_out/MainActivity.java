package com.example.night_out;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    public static final int MY_PERMISSIONS_REQUEST_FINE_LOCATION = 1;
    public static final int SET_FOOD_FILTERS_REQUEST = 11;
    public static final int SET_DRINK_FILTERS_REQUEST = 12;
    public static final int SET_FUN_FILTERS_REQUEST = 13;

    TextView address_text;
    TextView foodChoice, drinkChoice, funChoice;

    //hamburger drawer members
    private ListView mDrawerList;//the list view
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private String mActivityTitle;
    public double latitude;
    public double longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //locate textViews on creation
        foodChoice = findViewById(R.id.foodChoice);
        drinkChoice = findViewById(R.id.drinkChoice);
        funChoice = findViewById(R.id.funChoice);

        //listeners to move to each filtering activity
        Button food_btn = findViewById(R.id.foodSelect);
        food_btn.setOnClickListener(v -> {
            startActivityForResult(new Intent(MainActivity.this, FoodFilters.class),
                    SET_FOOD_FILTERS_REQUEST);//expect a result
        });
        Button drink_btn = findViewById(R.id.drinkSelect);
        drink_btn.setOnClickListener(v -> startActivityForResult(new Intent(MainActivity.this, DrinkFilters.class),
                SET_DRINK_FILTERS_REQUEST));
        Button fun_btn = findViewById(R.id.funSelect);
        fun_btn.setOnClickListener(v -> startActivityForResult(new Intent(MainActivity.this, FunFilters.class),
                SET_FUN_FILTERS_REQUEST));

        //hamburger drawer code
        mDrawerList = findViewById(R.id.navList);//make the drawer
        mDrawerLayout = findViewById(R.id.drawer_layout);//store the layout
        mActivityTitle = getTitle().toString();//store the title

        addDrawerItems();//populate the drawer
        setupDrawer();//setup the title changing when drawer is open/close

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);//enables the drawer button
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu);//sets the icon
        getSupportActionBar().setHomeButtonEnabled(true);//same

        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_FINE_LOCATION );
        }
        else {
            //Gets GPS latitude and longitude location
            getLatLong();
        }
        Button yelp_btn = findViewById(R.id.yelpSelect);
        double finalLatitude = latitude;
        double finalLongitude = longitude;
        yelp_btn.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, pageResult.class);
            Bundle b = new Bundle();
            b.putString("foodChoice", foodChoice.getText().toString());
            b.putString("drinkChoice", drinkChoice.getText().toString());
            b.putString("funChoice", funChoice.getText().toString());
            b.putDouble("lat", finalLatitude);
            b.putDouble("long", finalLongitude);
            intent.putExtras(b); //Put your id to your next Intent
            startActivity(intent);
        });
    }
    //gets lat and long from Location data
    private void getLatLong() {
        LocationManager lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        @SuppressLint("MissingPermission") Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SET_FOOD_FILTERS_REQUEST) {
            String choiceStr = data.getStringExtra("foodFilters");
            if (choiceStr == null || resultCode != RESULT_OK) {
                choiceStr = "No food filters set!";
            }
            foodChoice.setText(choiceStr);
        }
        if (requestCode == SET_DRINK_FILTERS_REQUEST) {
            String choiceStr = data.getStringExtra("drinkFilters");
            if (choiceStr == null || resultCode != RESULT_OK) {
                choiceStr = "No drink filters set!";
            }
            drinkChoice.setText(choiceStr);
        }
        if (requestCode == SET_FUN_FILTERS_REQUEST) {
            String choiceStr = data.getStringExtra("funFilters");
            if (choiceStr == null || resultCode != RESULT_OK) {
                choiceStr = "No fun filters set!";
            }
            funChoice.setText(choiceStr);
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
        ArrayAdapter<String> mAdapter;//adapts strings to drawer items
        mAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, osArray);
        mDrawerList.setAdapter(mAdapter);

        //adds tap listener
        mDrawerList.setOnItemClickListener((parent, view, position, id) -> Toast.makeText(MainActivity.this, "Page NYI!", Toast.LENGTH_SHORT).show());
    }

    //changes the title bar based on drawer status
    private void setupDrawer() {//this activity, drawer's layout, drawable icon, accessibility strings
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.drawer_open, R.string.drawer_close) {

            //called when a drawer has settled in a completely open state
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                Objects.requireNonNull(getSupportActionBar()).setTitle("Hamburger Drawer");
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            //Called when a drawer has settled in a completely closed state
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                Objects.requireNonNull(getSupportActionBar()).setTitle(mActivityTitle);
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
                                           @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_FINE_LOCATION : {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getLatLong();
                } else {
                    Toast.makeText(MainActivity.this, "Location usage required", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
