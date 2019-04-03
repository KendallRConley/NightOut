package com.example.night_out;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import okhttp3.*;

public class pageResult extends AppCompatActivity {
    TextView yelpQuery;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page_result);
        yelpQuery = findViewById(R.id.yelpQuery);
        Request request = new Request.Builder()
                .url("https://api.yelp.com/v3/businesses/search?categories=seafood&price=1&location=Lexington,%20KY")
                .get()
                .addHeader("Authorization", "Bearer C7UvUlP5eSCdaKyC0T5mjE8EXnhPm4lu_lGFDFAI_vzIzUkNNbqyl6pnaZMYFcU4p2E-93JFKP20URmNfBbc12sKJ-lugBGY6FxyCRFUFjMuBFmc9n3gHCdYq4KSXHYx")
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .addHeader("cache-control", "no-cache")
                .addHeader("Postman-Token", "fb69fb24-4846-4a93-9e4e-1199fa4693ff")
                .build();
        AsyncTask<Request, Void, String> query = new downloadHandler().execute(request);
        yelpQuery.setText("TESTING QUERY");
    }
}
