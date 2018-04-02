package com.enroute.enroute;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.enroute.enroute.Singleton.RequestQueueSingleton;
import com.enroute.enroute.model.Cuisine;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class RestaurantRecommendationActivity extends AppCompatActivity {

    //Set the default city as Halifax (The city_id of Halifax in Zomato API is 3099)
    String city_id = "3099";

    //Base Url for searching restaurants
    final String restaurant_base_url = "https://developers.zomato.com/api/v2.1/search?entity_id="
            + city_id + "&entity_type=city&start=";

    //Base url for searching all cuisines in the city
    final String cuisines_base_url = "https://developers.zomato.com/api/v2.1/cuisines?city_id=";


    //API Key for Zomato API
    final String api_key = "6cfc1a029b5b9a1d7e4b713ac61a99de";

    ArrayList<Cuisine> cuisines = new ArrayList<Cuisine>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_recommendation);

        String cuisines_url = cuisines_base_url.concat(city_id);

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

                                Cuisine cuisine = new Cuisine();

                                JSONObject cuisineData = cuisinesJSONArray.getJSONObject(i).getJSONObject("cuisine");

                                cuisine.setCuisine_id(cuisineData.getInt("cuisine_id"));
                                cuisine.setCuisine_name(cuisineData.getString("cuisine_name"));

                                cuisines.add(cuisine);

                                /////////
//                                Log.d("cuisine_name" , cuisineData.getString("cuisine_name"));

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

        RequestQueueSingleton.getmInstance(getApplicationContext()).addToRequestQueue (cuisineRequest);

        for (int i=0; i<cuisines.size(); i++){
            Log.d("ArrayList-cuisines", cuisines.get(i).getCuisine_name());
        }


        //TODO: get the preferred cuisines of the current user

        //Hard Code
        String preference = "Asian,BBQ,Mexican,Seafood";

        String[] preferred_cuisines = preference.split(",");

//        for (int i=0; i<preferred_cuisines.length; i++) {
//            Log.d("preferred_cuisines", preference[i]);
//        }

        //fetch results after offset
        int start = 0;
        //max number of results to display
        int count = 20;

        final String restaurant_url_start = restaurant_base_url + String.valueOf(start) +
                "&count=" + String.valueOf(count) + "&cuisines=";

        for (int i=0; i<preferred_cuisines.length; i++){

            String restaurant_url = restaurant_url_start + preferred_cuisines[i] + "&sort=rating";

            Log.d("restaurant_url", restaurant_url);

            //Set JsonObjectRequest
            JsonObjectRequest restaurantRequest = new JsonObjectRequest(Request.Method.GET, restaurant_url, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            //LOG
                            Log.d("Response" , response.toString());

//                            try{
//
//                                JSONArray cuisinesJSONArray = response.getJSONArray("cuisines");
//
//                                //LOG
//                                Log.d("cuisinesJSONArray", cuisinesJSONArray.toString());
//
//                                for (int i = 0; i < cuisinesJSONArray.length(); i++){
//
//                                    cuisineList.clear();
//
//                                    JSONObject cuisineData = cuisinesJSONArray.getJSONObject(i).getJSONObject("cuisine");
//                                    cuisineList.add(cuisineData.getString("cuisine_name"));
//
//                                    /////////
//                                    Log.d("cuisine_name" , cuisineData.getString("cuisine_name"));
//
//                                }
//
//                            }catch (JSONException e){
//                                Log.d("ERROR", "Error (JSONException): " + e.toString());
//                            }
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

//            RequestQueueSingleton.getmInstance(getApplicationContext()).addToRequestQueue (restaurantRequest);

        }





    }


//    public ArrayList<String> getCuisines() {
//
//        //A list to store all cuisines Zomato supports
//        final ArrayList<String> cuisineList = new ArrayList<>();
//        String cuisines_url = cuisines_base_url.concat(String.valueOf(city_id));
//
//        //Set JsonObjectRequest
//        JsonObjectRequest cuisineRequest = new JsonObjectRequest(Request.Method.GET, cuisines_url, null,
//                new Response.Listener<JSONObject>() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        //LOG
//                        Log.d("Response", response.toString());
//
//                        try{
//
//                            JSONArray cuisinesJSONArray = response.getJSONArray("cuisines");
//
//                            //LOG
//                            Log.d("cuisinesJSONArray", cuisinesJSONArray.toString());
//
//                            for (int i = 0; i < cuisinesJSONArray.length(); i++){
//
//                                cuisineList.clear();
//
//                                JSONObject cuisineData = cuisinesJSONArray.getJSONObject(i).getJSONObject("cuisine");
//                                cuisineList.add(cuisineData.getString("cuisine_name"));
//
//                                /////////
//                                Log.d("cuisine_name" , cuisineData.getString("cuisine_name"));
//
//                            }
//
//                        }catch (JSONException e){
//                            Log.d("ERROR", "Error (JSONException): " + e.toString());
//                        }
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError e) {
//                        Log.d("ERROR", "Error (onErrorResponse): " + e.toString());
//                    }
//                }
//        ) {
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                Map<String, String> params = new HashMap<String, String>();
//                params.put("user-key", api_key);
//                params.put("Accept", "application/json");
//
//                return params;
//            }
//        };
//
//        //Create RequestQueue
////        RequestQueue queue = Volley.newRequestQueue(this);
////        queue.add(cuisineRequest);
//
//        RequestQueueSingleton.getmInstance(getApplicationContext()).addToRequestQueue (cuisineRequest);
//
//        return cuisineList;
//    }
}
