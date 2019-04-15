package com.example.night_out;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import java.util.Random;

public class DrinkFilters extends AppCompatActivity {
    //instance vars to be altered by listeners in onCreate
    private String costStr = "All";
    private String distStr = "All";
    private String typeStr = "All";
    public String[] drinkTypes = {"bars", "cocktailbars", "hookah_bars", "sportsbars", "wine_bars",
                                  "pianobars", "cigarbars", "divebars", "irish_pubs", "beerbar",
                                  "barcrawl", "lounges", "whiskeybars", "bars"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drink_filters);

        //buttons in this activity
        Button button_cost1, button_cost2, button_cost3, button_dist1, button_dist2, button_dist3,
                button_apply, button_choice1, button_choice2, button_random;
        button_cost1 = findViewById(R.id.button_cost1);
        button_cost2 = findViewById(R.id.button_cost2);
        button_cost3 = findViewById(R.id.button_cost3);
        button_dist1 = findViewById(R.id.button_dist1);
        button_dist2 = findViewById(R.id.button_dist2);
        button_dist3 = findViewById(R.id.button_dist3);
        button_apply = findViewById(R.id.button_apply);
        button_choice1 = findViewById(R.id.choice1);
        button_choice2 = findViewById(R.id.choice2);
        button_random = findViewById(R.id.random);

        Random rand = new Random();
        randomizeButtons(rand, button_choice1, button_choice2);
        //button listeners to change textViews in MainActivity
        button_cost1.setOnClickListener(v -> costStr = getString(R.string.button_name_cost1));
        button_cost2.setOnClickListener(v -> costStr = getString(R.string.button_name_cost2));
        button_cost3.setOnClickListener(v -> costStr = getString(R.string.button_name_cost3));
        button_dist1.setOnClickListener(v -> distStr = getString(R.string.button_name_dist1));
        button_dist2.setOnClickListener(v -> distStr = getString(R.string.button_name_dist2));
        button_dist3.setOnClickListener(v -> distStr = getString(R.string.button_name_dist3));
        button_apply.setOnClickListener(v -> {
            Intent filtersIntent = new Intent();
            filtersIntent.putExtra("drinkFilters", costStr+", "+distStr+", "+typeStr);
            setResult(RESULT_OK, filtersIntent);
            finish();
        });
        button_choice1.setOnClickListener(v -> typeStr = button_choice1.getText().toString());
        button_choice2.setOnClickListener(v -> typeStr = button_choice2.getText().toString());
        button_random.setOnClickListener(v -> randomizeButtons(rand, button_choice1, button_choice2));
    }

    private void randomizeButtons(Random rand, Button button_choice1, Button button_choice2) {
        int objNum1 = rand.nextInt(drinkTypes.length);
        int objNum2 = rand.nextInt(drinkTypes.length);
        while (objNum1 == objNum2) {objNum2 = rand.nextInt(drinkTypes.length);}
        button_choice1.setText(drinkTypes[objNum1]);
        button_choice2.setText(drinkTypes[objNum2]);
    }

    @Override
    public void onBackPressed() {
        Intent filtersIntent = new Intent();
        filtersIntent.putExtra("drinkFilters", costStr+", "+distStr+", "+typeStr);
        setResult(RESULT_OK, filtersIntent);
        finish();
    }
}
