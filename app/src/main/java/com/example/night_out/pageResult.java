package com.example.night_out;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Random;

import okhttp3.*;

public class pageResult extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page_result);

        Bundle b = getIntent().getExtras();
        if(b!= null) {
            double lat = b.getDouble("lat");
            double lon = b.getDouble("long");
            String foodChoices = b.getString("foodChoice");
            String[] choices = foodChoices.split(",");
            String price;
            int radius;
            String term = choices[2].substring(1);
            switch (choices[0]) {
                case "$":
                    price = "1";
                    break;
                case "$$":
                    price = "2";
                    break;
                case "$$$":
                    price = "3";
                    break;
                default:
                    price = "1,2,3";
                    break;
            }
            switch (choices[1]){
                case " Close":
                    radius = 8050; //about 5 miles
                    break;
                case " Mid":
                    radius = 25000; //about 15 miles
                    break;
                default:
                    radius = 40000; //about 25 miles. Yelp's max radius
            }
            Request request = new Request.Builder()
                    .url("https://api.yelp.com/v3/businesses/search?categories="+term+"&price="+price+"&latitude="+lat+"&longitude="+lon+"&radius="+radius)
                    .get()
                    .addHeader("Authorization", "Bearer C7UvUlP5eSCdaKyC0T5mjE8EXnhPm4lu_lGFDFAI_vzIzUkNNbqyl6pnaZMYFcU4p2E-93JFKP20URmNfBbc12sKJ-lugBGY6FxyCRFUFjMuBFmc9n3gHCdYq4KSXHYx")
                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .addHeader("cache-control", "no-cache")
                    .addHeader("Postman-Token", "fb69fb24-4846-4a93-9e4e-1199fa4693ff")
                    .build();
            new downloadHandler().execute(request);
        }
    }

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
            TextView name = findViewById(R.id.yelpName);
            TextView add = findViewById(R.id.yelpAdd);
            TextView phone = findViewById(R.id.yelpPhone);
            TextView url = findViewById(R.id.yelpURL);
            try {
                name.setText(result.getString("name"));
                add.setText(result.getString("location"));
                phone.setText(result.getString("phone"));
                url.setText(result.getString("url"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}

