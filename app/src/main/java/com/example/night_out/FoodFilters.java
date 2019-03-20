package com.example.night_out;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

public class FoodFilters extends AppCompatActivity {
    //instance vars to be altered by listeners in onCreate
    private String costStr = "None";
    private String distStr = "None";
    private String typeStr = "None";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_filters);

        //buttons in this activity
        Button button_cost1, button_cost2, button_cost3, button_dist1, button_dist2, button_dist3;
        button_cost1 = findViewById(R.id.button_cost1);
        button_cost2 = findViewById(R.id.button_cost2);
        button_cost3 = findViewById(R.id.button_cost3);
        button_dist1 = findViewById(R.id.button_dist1);
        button_dist2 = findViewById(R.id.button_dist2);
        button_dist3 = findViewById(R.id.button_dist3);
        //button listeners to change textViews in MainActivity
        button_cost1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                costStr = getString(R.string.button_name_cost1);
            }
        });
        button_cost2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                costStr = getString(R.string.button_name_cost2);
            }
        });
        button_cost3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                costStr = getString(R.string.button_name_cost3);
            }
        });
        button_dist1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                distStr = getString(R.string.button_name_dist1);
            }
        });
        button_dist2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                distStr = getString(R.string.button_name_dist2);
            }
        });
        button_dist3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                distStr = getString(R.string.button_name_dist3);
            }
        });

        Spinner spinner_foodType = findViewById(R.id.spinner_foodType);
        //create arrayAdapter from the string resource for a spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.food_types_array, android.R.layout.simple_spinner_item);
        //set the spinner layout in the adapter
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //apply the adapter
        spinner_foodType.setAdapter(adapter);
        //listener to get type selection
        spinner_foodType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                typeStr = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                typeStr = "None";
            }
        });
    }

    @Override
    protected void onPause() {
        Intent filtersIntent = new Intent();
        filtersIntent.putExtra("foodFilters", costStr+", "+distStr+", "+typeStr);
        setResult(RESULT_OK, filtersIntent);
        super.onPause();
    }
}
