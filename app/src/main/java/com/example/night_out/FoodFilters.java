package com.example.night_out;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class FoodFilters extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_filters);

        Spinner spinner_foodType = (Spinner)findViewById(R.id.spinner_foodType);
        //create arrayAdapter from the string resource for a spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.food_types_array, android.R.layout.simple_spinner_item);
        //set the spinner layout in the adapter
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //apply the adapter
        spinner_foodType.setAdapter(adapter);
    }
}
