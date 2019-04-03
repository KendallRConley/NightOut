package com.example.night_out;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class downloadHandler extends AsyncTask<Request, Void, String> {
    OkHttpClient client = new OkHttpClient();
    @Override
    protected String doInBackground(Request... requests) {
        try {
            Response response = client.newCall(requests[0]).execute();
            JSONObject jsonObject = new JSONObject(response.body().string().trim());       // parser
            JSONArray myResponse = (JSONArray)jsonObject.get("businesses");
            return (myResponse.getJSONObject(0).getString("name"));
        } catch (IOException e) {
            e.printStackTrace();
            return "E1";
        } catch (JSONException e) {
            e.printStackTrace();
            return "E2";
        }
    }
}
