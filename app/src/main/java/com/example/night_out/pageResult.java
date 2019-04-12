package com.example.night_out;
import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
    }

    public Request buildRequest(double lat, double lon, String item){
        String[] choices = item.split(",");
        String price = getPriceString(choices[0]);
        int radius = getDistance(choices[1].substring(1));
        String term = choices[2].substring(1);

        Request request = new Request.Builder()
                .url("https://api.yelp.com/v3/businesses/search?categories=" + term + "&price=" + price + "&latitude=" + lat + "&longitude=" + lon + "&radius=" + radius)
                .get()
                .addHeader("Authorization", "Bearer C7UvUlP5eSCdaKyC0T5mjE8EXnhPm4lu_lGFDFAI_vzIzUkNNbqyl6pnaZMYFcU4p2E-93JFKP20URmNfBbc12sKJ-lugBGY6FxyCRFUFjMuBFmc9n3gHCdYq4KSXHYx")
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .addHeader("cache-control", "no-cache")
                .addHeader("Postman-Token", "fb69fb24-4846-4a93-9e4e-1199fa4693ff")
                .build();
        return request;
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
                yelp_price = "1,2,3";
                break;
        }
        return yelp_price;
    }

    public int getDistance(String dist){
        int distance;
        switch (dist){
            case "Close":
                distance = 8050; //about 5 miles
                break;
            case "Mid":
                distance = 25000; //about 15 miles
                break;
            default:
                distance = 40000; //about 25 miles. Yelp's max radius
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
                JSONObject jsonObject = new JSONObject(response.body().string().trim());       // parser
                JSONArray myResponse = (JSONArray)jsonObject.get("businesses");
                Random rand = new Random();
                int objNum = rand.nextInt(myResponse.length());
                return (myResponse.getJSONObject(objNum));
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
            TextView onename = findViewById(R.id.onename);
            TextView twoname = findViewById(R.id.twoname);
            TextView threename = findViewById(R.id.threename);
            try {
                if(result.getString("name")!=null) {
                    if (onename.getText().toString().equals("Loading")) {
                        onename.setText(result.getString("name"));
                    } else if (twoname.getText().toString().equals("Loading")) {
                        twoname.setText(result.getString("name"));
                    } else if (threename.getText().toString().equals("Loading")) {
                        threename.setText(result.getString("name"));
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}

