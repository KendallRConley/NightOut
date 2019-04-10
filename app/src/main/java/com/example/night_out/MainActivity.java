package com.example.night_out;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
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
import android.widget.Button;
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
    public static final int SET_FOOD_FILTERS_REQUEST = 11;
    public static final int SET_DRINK_FILTERS_REQUEST = 12;
    public static final int SET_FUN_FILTERS_REQUEST = 13;
    public double latitude, longitude;

    TextView address_text;
    TextView foodChoice, drinkChoice, funChoice;

    //hamburger drawer members
    private ListView mDrawerList;//the list view
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private String mActivityTitle;

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
        food_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(MainActivity.this, FoodFilters.class),
                        SET_FOOD_FILTERS_REQUEST);//expect a result
            }
        });
        Button drink_btn = findViewById(R.id.drinkSelect);
        drink_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(MainActivity.this, DrinkFilters.class),
                        SET_DRINK_FILTERS_REQUEST);
            }
        });
        Button fun_btn = findViewById(R.id.funSelect);
        fun_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(MainActivity.this, FunFilters.class),
                        SET_FUN_FILTERS_REQUEST);
            }
        });

        //hamburger drawer code
        mDrawerList = findViewById(R.id.navList);//make the drawer
        mDrawerLayout = findViewById(R.id.drawer_layout);//store the layout
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

        Button map_button = findViewById(R.id.show_Map);
        map_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mapIntent = new Intent(MainActivity.this, NightOutMap.class);
                mapIntent.putExtra("latString", latitude);
                mapIntent.putExtra("lonString", longitude);
                startActivity(mapIntent);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            //TODO add branches for other filtering options
            if (requestCode == SET_FOOD_FILTERS_REQUEST) {
                String choiceStr = data.getStringExtra("foodFilters");
                if (choiceStr == null) {
                    choiceStr = "None, None, None";
                }
                foodChoice.setText(choiceStr);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
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

            //called when a drawer has settled in a completely open state
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
            }

            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }
}
