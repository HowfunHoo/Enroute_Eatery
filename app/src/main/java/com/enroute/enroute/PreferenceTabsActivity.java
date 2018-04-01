package com.enroute.enroute;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.enroute.enroute.Singleton.RequestQueueSingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PreferenceTabsActivity extends AppCompatActivity {

    final String cuisines_base_url = "https://developers.zomato.com/api/v2.1/cuisines?city_id=";

    //API Key for Zomato API
    final String api_key = "6cfc1a029b5b9a1d7e4b713ac61a99de";

    //Set the default city as Halifax (The city_id of Halifax in Zomato API is 3099)
    String city = "Halifax";
    int city_id = 3099;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preference_tabs);

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                getCuisines();

            }
        };

        //TODO: Shows tabs show the cuisines for users to select
        //A friendly reminder: try to make this function finished in getCuisines(), not in onCreate().
    }

    public void getCuisines() {

        //A list to store all cuisines Zomato supports
        final ArrayList<String> cuisineList = new ArrayList<>();
        String cuisines_url = cuisines_base_url.concat(String.valueOf(city_id));

        //Set JsonObjectRequest
        JsonObjectRequest cuisineRequest = new JsonObjectRequest(Request.Method.GET, cuisines_url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //LOG
                        Log.d("Response", response.toString());

                        try{

                            JSONArray cuisinesJSONArray = response.getJSONArray("cuisines");

                            //LOG
                            Log.d("cuisinesJSONArray", cuisinesJSONArray.toString());

                            for (int i = 0; i < cuisinesJSONArray.length(); i++){

                                cuisineList.clear();

                                JSONObject cuisineData = cuisinesJSONArray.getJSONObject(i).getJSONObject("cuisine");
                                cuisineList.add(cuisineData.getString("cuisine_name"));

                                /////////
                                Log.d("cuisine_name" , cuisineData.getString("cuisine_name"));

                            }

                        }catch (JSONException e){
                            Log.d("ERROR", "Error (JSONException): " + e.toString());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError e) {
                        Log.d("ERROR", "Error (onErrorResponse): " + e.toString());
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user-key", api_key);
                params.put("Accept", "application/json");

                return params;
            }
        };

        //Create RequestQueue
//        RequestQueue queue = Volley.newRequestQueue(this);
//        queue.add(cuisineRequest);

        RequestQueueSingleton.getmInstance(getApplicationContext()).addToRequestQueue (cuisineRequest);
    }
}
