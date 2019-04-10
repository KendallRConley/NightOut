package com.example.night_out;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

public class FunFilters extends AppCompatActivity {
    //instance vars to be altered by listeners in onCreate
    private String costStr = "All";
    private String distStr = "All";
    private String typeStr = "All";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fun_filters);

        //buttons in this activity
        Button button_cost1, button_cost2, button_cost3, button_dist1, button_dist2, button_dist3,
                button_apply;
        button_cost1 = findViewById(R.id.button_cost1);
        button_cost2 = findViewById(R.id.button_cost2);
        button_cost3 = findViewById(R.id.button_cost3);
        button_dist1 = findViewById(R.id.button_dist1);
        button_dist2 = findViewById(R.id.button_dist2);
        button_dist3 = findViewById(R.id.button_dist3);
        button_apply = findViewById(R.id.button_apply);
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
        button_apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent filtersIntent = new Intent();
                filtersIntent.putExtra("funFilters", costStr+", "+distStr+", "+typeStr);
                setResult(RESULT_OK, filtersIntent);
                finish();
            }
        });

        /*TODO on yelp merge:
        JSONObject jsonObject = new JSONObject(getIntent().getStringExtra("jsonObject"));
        ArrayList<String> categoriesList = new ArrayList(0);
        for (each categoryString in JSONObject) {//may need to be a few nested fors to get to the right level
            categoriesList.add(categoryString);
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String> (this,
                android.R.layout.simple_spinner_dropdown_item, categoriesList);
        TODO: then delete the two lines before spinner.setAdapter(adapter) below this
         */

        Spinner spinner_funType = findViewById(R.id.spinner_funType);
        //create arrayAdapter from the string resource for a spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.fun_types_array, android.R.layout.simple_spinner_item);
        //set the spinner layout in the adapter
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //apply the adapter
        spinner_funType.setAdapter(adapter);
        //listener to get type selection
        spinner_funType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                typeStr = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                typeStr = "All";
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent filtersIntent = new Intent();
        filtersIntent.putExtra("funFilters", costStr+", "+distStr+", "+typeStr);
        setResult(RESULT_OK, filtersIntent);
        finish();
    }
}
