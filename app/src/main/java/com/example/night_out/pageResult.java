package com.example.night_out;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Objects;
import java.util.Random;

import okhttp3.*;

public class pageResult extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page_result);
        TextView oneName = findViewById(R.id.onename);
        TextView oneCoords = findViewById(R.id.oneLatLong);
        TextView twoName = findViewById(R.id.twoname);
        TextView twoCoords = findViewById(R.id.twoLatLong);
        TextView threeName = findViewById(R.id.threename);
        TextView threeCoords = findViewById(R.id.threeLatLong);

        Button reroll = findViewById(R.id.reroll);
        reroll.setOnClickListener(v -> recreate());

        Bundle b = getIntent().getExtras();
        if(b!= null && (!Objects.requireNonNull(b.getString("foodChoice")).equals("") ||
                !Objects.requireNonNull(b.getString("drinkChoice")).equals("") ||
                !Objects.requireNonNull(b.getString("funChoice")).equals(""))) {
            double lat = b.getDouble("lat");
            double lon = b.getDouble("long");
            String food = b.getString("foodChoice");
            String drink = b.getString("drinkChoice");
            String fun = b.getString("funChoice");
            if((!Objects.requireNonNull(food).equals(""))) {
                Request food_request = buildRequest(lat, lon, food);
                new downloadHandler().execute(food_request);
            }
            if((!Objects.requireNonNull(drink).equals(""))) {
                Request drink_request = buildRequest(lat, lon, drink);
                new downloadHandler().execute(drink_request);
            }
            if((!Objects.requireNonNull(fun).equals(""))) {
                Request fun_request = buildRequest(lat, lon, fun);
                new downloadHandler().execute(fun_request);
            }
        }
        else{
            finish();
        }

        Button map_btn = findViewById(R.id.goToMap);
        map_btn.setOnClickListener(v -> {
            Intent intent = new Intent(pageResult.this, resultMap.class);
            Bundle newB = new Bundle();
            assert b != null;
            newB.putDouble("lat", b.getDouble("lat"));
            newB.putDouble("long", b.getDouble("long"));
            newB.putString("oneName", oneName.getText().toString());
            newB.putString("twoName", twoName.getText().toString());
            newB.putString("threeName", threeName.getText().toString());
            newB.putString("oneCoords", oneCoords.getText().toString());
            newB.putString("twoCoords", twoCoords.getText().toString());
            newB.putString("threeCoords", threeCoords.getText().toString());
            intent.putExtras(newB); //bundles variables to send to result
            startActivity(intent);
        });
    }

    public Request buildRequest(double lat, double lon, String item){
        String[] choices = item.split(",");
        String price = getPriceString(choices[0]);
        int radius = getDistance(choices[1].substring(1));
        String term = choices[2].substring(1);

        if (price == null){
            return new Request.Builder()
                    .url("https://api.yelp.com/v3/businesses/search?open_now=true&categories=" + term + "&latitude=" + lat + "&longitude=" + lon + "&radius=" + radius)
                    .get()
                    .addHeader("Authorization", "Bearer C7UvUlP5eSCdaKyC0T5mjE8EXnhPm4lu_lGFDFAI_vzIzUkNNbqyl6pnaZMYFcU4p2E-93JFKP20URmNfBbc12sKJ-lugBGY6FxyCRFUFjMuBFmc9n3gHCdYq4KSXHYx")
                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .addHeader("cache-control", "no-cache")
                    .addHeader("Postman-Token", "fb69fb24-4846-4a93-9e4e-1199fa4693ff")
                    .build();
        }

        else{
            return new Request.Builder()
                .url("https://api.yelp.com/v3/businesses/search?open_now=true&categories=" + term + "&price=" + price + "&latitude=" + lat + "&longitude=" + lon + "&radius=" + radius)
                .get()
                .addHeader("Authorization", "Bearer C7UvUlP5eSCdaKyC0T5mjE8EXnhPm4lu_lGFDFAI_vzIzUkNNbqyl6pnaZMYFcU4p2E-93JFKP20URmNfBbc12sKJ-lugBGY6FxyCRFUFjMuBFmc9n3gHCdYq4KSXHYx")
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .addHeader("cache-control", "no-cache")
                .addHeader("Postman-Token", "fb69fb24-4846-4a93-9e4e-1199fa4693ff")
                .build();
        }
    }

    public String getPriceString(String price){
        String yelp_price;
        switch (price) {
            case "$":
                yelp_price = "1";
                break;
            case "$$":
                yelp_price = "2";
                break;
            case "$$$":
                yelp_price = "3";
                break;
            default:
                yelp_price = null;
                break;
        }
        return yelp_price;
    }

    public int getDistance(String dist){
        int distance;
        switch (dist){
            case "Close":
                distance = 2500; //about 1.5 miles
                break;
            case "Mid":
                distance = 12500; //about 7.5 miles
                break;
            default:
                distance = 40000; //about 25 miles.
        }
        return distance;
    }

    @SuppressLint("StaticFieldLeak")
    private class downloadHandler extends AsyncTask<Request, JSONObject, JSONObject> {
        OkHttpClient client = new OkHttpClient();
        @Override
        protected JSONObject doInBackground(Request... requests) {
            try {
                Response response = client.newCall(requests[0]).execute();
                assert response.body() != null;
                JSONObject jsonObject = new JSONObject(response.body().string().trim());
                if(jsonObject.getInt("total") == 0){ //if no results found
                    return null;
                }
                else { //results found
                    JSONArray myResponse = (JSONArray) jsonObject.get("businesses");
                    Random rand = new Random();
                    int objNum = rand.nextInt(myResponse.length());
                    return (myResponse.getJSONObject(objNum));
                }
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        }
        @Override
        protected void onPostExecute(JSONObject result){
            TextView oneName = findViewById(R.id.onename);
            TextView oneAdd = findViewById(R.id.oneAdd);
            TextView oneCoords = findViewById(R.id.oneLatLong);
            TextView twoName = findViewById(R.id.twoname);
            TextView twoAdd = findViewById(R.id.twoAdd);
            TextView twoCoords = findViewById(R.id.twoLatLong);
            TextView threeName = findViewById(R.id.threename);
            TextView threeAdd = findViewById(R.id.threeAdd);
            TextView threeCoords = findViewById(R.id.threeLatLong);
            try {
                if (result == null){ //if no results found
                    if (oneName.getText().toString().equals("Loading")) {
                        oneName.setText(getString(R.string.no_results_found));
                        oneAdd.setText(getString(R.string.no_results_found));
                        oneCoords.setText(getString(R.string.no_results_found));
                    } else if (twoName.getText().toString().equals("Loading")) {
                        twoName.setText(getString(R.string.no_results_found));
                        twoAdd.setText(getString(R.string.no_results_found));
                        twoCoords.setText(getString(R.string.no_results_found));
                    } else if (threeName.getText().toString().equals("Loading")) {
                        threeName.setText(getString(R.string.no_results_found));
                        threeAdd.setText(getString(R.string.no_results_found));
                        threeCoords.setText(getString(R.string.no_results_found));
                    }
                }
                else { //print results, finding which section hasnt already been taken first
                    if (oneName.getText().toString().equals("Loading")) {
                        oneName.setText(result.getString("name"));
                        oneAdd.setText(buildAdd(result));
                        oneCoords.setText(buildLatLong(result));
                    } else if (twoName.getText().toString().equals("Loading")) {
                        twoName.setText(result.getString("name"));
                        twoAdd.setText(buildAdd(result));
                        twoCoords.setText(buildLatLong(result));
                    } else if (threeName.getText().toString().equals("Loading")) {
                        threeName.setText(result.getString("name"));
                        threeAdd.setText(buildAdd(result));
                        threeCoords.setText(buildLatLong(result));
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        //builds address string given result object
        private String buildAdd(JSONObject result) throws JSONException {
            String add = "";
            JSONObject locationData = result.getJSONObject("location");
            return add + locationData.getString("address1") + " " + locationData.getString("city") + ", " + locationData.getString("state");
        }
        //builds lat,long string given result object
        private String buildLatLong(JSONObject result) throws JSONException {
            String coordinates = "";
            JSONObject locationData = result.getJSONObject("coordinates");
            return coordinates + locationData.getString("latitude") + ", " + locationData.getString("longitude");
        }
    }
}

