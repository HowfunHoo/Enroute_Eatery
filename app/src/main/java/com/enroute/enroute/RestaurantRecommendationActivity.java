package com.enroute.enroute;

import android.content.Intent;
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
import com.enroute.enroute.ZomatoHelpers.ZomatoHelper;
import com.enroute.enroute.interfaces.CuisineCallbacks;
import com.enroute.enroute.interfaces.RestaurantCallbacks;
import com.enroute.enroute.model.Cuisine;
import com.enroute.enroute.model.Restaurant;

import junit.framework.Assert;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class RestaurantRecommendationActivity extends AppCompatActivity  {

    //Set the default city as Halifax (The city_id of Halifax in Zomato API is 3099)
    String city_id = "3099";

    //Base Url for searching restaurants
    final String restaurant_base_url = "https://developers.zomato.com/api/v2.1/search?entity_id="
            + city_id + "&entity_type=city&start=";

    //Base url for searching all cuisines in the city
    final String cuisines_base_url = "https://developers.zomato.com/api/v2.1/cuisines?city_id=";


    //API Key for Zomato API
//    private final String api_key = "6cfc1a029b5b9a1d7e4b713ac61a99de";
    private final String api_key = "52daad033db74e49b6ab38e67e0b5174";

    ArrayList<Cuisine> cuisines = new ArrayList<Cuisine>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_recommendation);



        //TODO: get the preferred cuisines of the current user
        //Hard Code
        String preference = "Asian,BBQ,Mexican,Seafood";
        final String[] preferred_cuisines = preference.split(",");
//        final int[] preferred_cuisineIds = new int[preferred_cuisines.length];
        int[] preferred_cuisineIds = new int[preferred_cuisines.length];

        final ZomatoHelper zomatoHelper = new ZomatoHelper();

        Bundle bundle = this.getIntent().getExtras();
        preferred_cuisineIds = bundle.getIntArray("preferred_cuisineIds");

        for (int i =0; i<preferred_cuisineIds.length; i++){
            Log.d("preferred_cuisineIds2", String.valueOf(preferred_cuisineIds[i]));
        }
        //Built CountDownLatch
//        final CountDownLatch latch = new CountDownLatch(1);

//        //TEST
//        Runnable runnable = new Runnable() {
//            @Override
//            public void run() {
//                zomatoHelper.getCuisines(getApplicationContext(), new CuisineCallbacks() {
//                    @Override
//                    public void onCuisineCallbacks(JSONObject JSONObjectResult) {
//
//                        cuisinesJSONObject[0] = JSONObjectResult;
//                        latch.countDown();
//
//                        //LOG TEST
//                        Log.d("JSONObjectResult", JSONObjectResult.toString());
////                try{
////
////                    JSONArray cuisinesJSONArray = JSONObjectResult.getJSONArray("cuisines");
////
////
////                    //LOG TEST
////                    Log.d("cuisinesJSONArray", cuisinesJSONArray.toString());
//
////                    for (int i = 0; i < cuisinesJSONArray.length(); i++){
////
////                        Cuisine cuisine = new Cuisine();
////
////                        JSONObject cuisineData = cuisinesJSONArray.getJSONObject(i).getJSONObject("cuisine");
////
////                        cuisine.setCuisine_id(cuisineData.getInt("cuisine_id"));
////                        cuisine.setCuisine_name(cuisineData.getString("cuisine_name"));
////
////                        cuisines.add(cuisine);
////
//////                        latch.countDown();
////
////                        //LOG TEST
////                        Log.d("cuisines.size()", String.valueOf(cuisines.size()));
////                        for (int j=0; j<cuisines.size(); j++){
////                            Log.d("ArrayList-cuisines", cuisines.get(j).getCuisine_name());
////                        }
////
//////                        //Match preferred cuisine names with the IDs
//////                        for (int j=0; j<cuisines.size(); j++){
//////                            for (int x=0; x<preferred_cuisines.length; x++){
//////                                if (cuisines.get(j).getCuisine_name().equals(preferred_cuisines[x])){
//////                                    preferred_cuisineIds[x] = cuisines.get(j).getCuisine_id();
//////                                }
//////                            }
//////                        }
////
////
//////                        //LOG TEST
//////                        for (int y = 0; y<preferred_cuisineIds.length; y++){
//////                            Log.d("preferred_cuisineIds", String.valueOf(preferred_cuisineIds[y]));
//////                        }
////
////                        //TODO
//////                        getRestaurants(preferred_cuisineIds);
////
////
////
////                    }
//
////                }catch (JSONException e){
////                    Log.d("ERROR", "Error (JSONException): " + e.toString());
////                }
//
//                    }
//                });
//
//            }
//        };

//        Thread thread = new Thread(null, runnable, "background");
//        thread.start();

//        Thread t1=new Thread(){
//            public void run(){
//
//
//                zomatoHelper.getCuisines(getApplicationContext(), new CuisineCallbacks() {
//                    @Override
//                    public void onCuisineCallbacks(JSONObject JSONObjectResult) {
//
//                        cuisinesJSONObject[0] = JSONObjectResult;
////                latch.countDown();
//
//                        //LOG TEST
//                        Log.d("JSONObjectResult", JSONObjectResult.toString());
////                try{
////
////                    JSONArray cuisinesJSONArray = JSONObjectResult.getJSONArray("cuisines");
////
////
////                    //LOG TEST
////                    Log.d("cuisinesJSONArray", cuisinesJSONArray.toString());
//
////                    for (int i = 0; i < cuisinesJSONArray.length(); i++){
////
////                        Cuisine cuisine = new Cuisine();
////
////                        JSONObject cuisineData = cuisinesJSONArray.getJSONObject(i).getJSONObject("cuisine");
////
////                        cuisine.setCuisine_id(cuisineData.getInt("cuisine_id"));
////                        cuisine.setCuisine_name(cuisineData.getString("cuisine_name"));
////
////                        cuisines.add(cuisine);
////
//////                        latch.countDown();
////
////                        //LOG TEST
////                        Log.d("cuisines.size()", String.valueOf(cuisines.size()));
////                        for (int j=0; j<cuisines.size(); j++){
////                            Log.d("ArrayList-cuisines", cuisines.get(j).getCuisine_name());
////                        }
////
//////                        //Match preferred cuisine names with the IDs
//////                        for (int j=0; j<cuisines.size(); j++){
//////                            for (int x=0; x<preferred_cuisines.length; x++){
//////                                if (cuisines.get(j).getCuisine_name().equals(preferred_cuisines[x])){
//////                                    preferred_cuisineIds[x] = cuisines.get(j).getCuisine_id();
//////                                }
//////                            }
//////                        }
////
////
//////                        //LOG TEST
//////                        for (int y = 0; y<preferred_cuisineIds.length; y++){
//////                            Log.d("preferred_cuisineIds", String.valueOf(preferred_cuisineIds[y]));
//////                        }
////
////                        //TODO
//////                        getRestaurants(preferred_cuisineIds);
////
////
////
////                    }
//
////                }catch (JSONException e){
////                    Log.d("ERROR", "Error (JSONException): " + e.toString());
////                }
//
//                    }
//                });
//
//
//
//
//
//            }
//        };
//
//        t1.start();
//
//        try {
//            t1.join();
//            Log.d("cuisinesJSONObject", cuisinesJSONObject[0].toString());
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }



//        try {
//            latch.await();
////            latch.await(10, TimeUnit.SECONDS);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        Assert.assertEquals(1, cuisinesJSONObject.length);

        //LOG TEST
//        Log.d("cuisinesJSONObject", cuisinesJSONObject[0].toString());


//        try{
//            JSONArray cuisinesJSONArray = cuisinesJSONObject[0].getJSONArray("cuisines");
//
//
//            //LOG TEST
//            Log.d("cuisinesJSONArray", cuisinesJSONArray.toString());
//
//            for (int i = 0; i < cuisinesJSONArray.length(); i++){
//
//                Cuisine cuisine = new Cuisine();
//
//                JSONObject cuisineData = cuisinesJSONArray.getJSONObject(i).getJSONObject("cuisine");
//
//                cuisine.setCuisine_id(cuisineData.getInt("cuisine_id"));
//                cuisine.setCuisine_name(cuisineData.getString("cuisine_name"));
//
//                cuisines.add(cuisine);
//
////                        latch.countDown();
//
//                //LOG TEST
//                Log.d("cuisines.size()", String.valueOf(cuisines.size()));
//                for (int j=0; j<cuisines.size(); j++){
//                    Log.d("ArrayList-cuisines", cuisines.get(j).getCuisine_name());
//                }
//
////                        //Match preferred cuisine names with the IDs
////                        for (int j=0; j<cuisines.size(); j++){
////                            for (int x=0; x<preferred_cuisines.length; x++){
////                                if (cuisines.get(j).getCuisine_name().equals(preferred_cuisines[x])){
////                                    preferred_cuisineIds[x] = cuisines.get(j).getCuisine_id();
////                                }
////                            }
////                        }
//
//
////                        //LOG TEST
////                        for (int y = 0; y<preferred_cuisineIds.length; y++){
////                            Log.d("preferred_cuisineIds", String.valueOf(preferred_cuisineIds[y]));
////                        }
//
//                //TODO
////                        getRestaurants(preferred_cuisineIds);
//
//
//
//            }
//        }catch (JSONException e){
//            e.printStackTrace();
//        }


        //Match preferred cuisine names with the IDs
//        for (int j=0; j<cuisines.size(); j++){
//            for (int x=0; x<preferred_cuisines.length; x++){
//                if (cuisines.get(j).getCuisine_name().equals(preferred_cuisines[x])){
//                    preferred_cuisineIds[x] = cuisines.get(j).getCuisine_id();
//                }
//            }
//        }
//
//        //LOG TEST
//        for (int y = 0; y<preferred_cuisineIds.length; y++){
//            Log.d("preferred_cuisineIds", String.valueOf(preferred_cuisineIds[y]));
//        }

//        getRestaurants(preferred_cuisineIds);



//        String cuisines_url = cuisines_base_url.concat(city_id);
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
//                                Cuisine cuisine = new Cuisine();
//
//                                JSONObject cuisineData = cuisinesJSONArray.getJSONObject(i).getJSONObject("cuisine");
//
//                                cuisine.setCuisine_id(cuisineData.getInt("cuisine_id"));
//                                cuisine.setCuisine_name(cuisineData.getString("cuisine_name"));
//
//                                cuisines.add(cuisine);
//
//                                /////////
////                                Log.d("cuisine_name" , cuisineData.getString("cuisine_name"));
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
//        RequestQueueSingleton.getmInstance(getApplicationContext()).addToRequestQueue (cuisineRequest);






//        for (int i=0; i<preferred_cuisines.length; i++) {
//            Log.d("preferred_cuisines", preference[i]);
//        }

        //fetch results after offset


//        int start = 0;
//        //max number of results to display
//        int count = 20;
//
//        final String restaurant_url_start = restaurant_base_url + String.valueOf(start) +
//                "&count=" + String.valueOf(count) + "&cuisines=";
//
//        for (int i=0; i<preferred_cuisines.length; i++){
//
//            String restaurant_url = restaurant_url_start + preferred_cuisines[i] + "&sort=rating";
//
//            Log.d("restaurant_url", restaurant_url);
//
//            //Set JsonObjectRequest
//            JsonObjectRequest restaurantRequest = new JsonObjectRequest(Request.Method.GET, restaurant_url, null,
//                    new Response.Listener<JSONObject>() {
//                        @Override
//                        public void onResponse(JSONObject response) {
//                            //LOG
//                            Log.d("Response" , response.toString());
//
////                            try{
////
////                                JSONArray cuisinesJSONArray = response.getJSONArray("cuisines");
////
////                                //LOG
////                                Log.d("cuisinesJSONArray", cuisinesJSONArray.toString());
////
////                                for (int i = 0; i < cuisinesJSONArray.length(); i++){
////
////                                    cuisineList.clear();
////
////                                    JSONObject cuisineData = cuisinesJSONArray.getJSONObject(i).getJSONObject("cuisine");
////                                    cuisineList.add(cuisineData.getString("cuisine_name"));
////
////                                    /////////
////                                    Log.d("cuisine_name" , cuisineData.getString("cuisine_name"));
////
////                                }
////
////                            }catch (JSONException e){
////                                Log.d("ERROR", "Error (JSONException): " + e.toString());
////                            }
//                        }
//                    },
//                    new Response.ErrorListener() {
//                        @Override
//                        public void onErrorResponse(VolleyError e) {
//                            Log.d("ERROR", "Error (onErrorResponse): " + e.toString());
//                        }
//                    }
//            ) {
//                @Override
//                public Map<String, String> getHeaders() throws AuthFailureError {
//                    Map<String, String> params = new HashMap<String, String>();
//                    params.put("user-key", api_key);
//                    params.put("Accept", "application/json");
//
//                    return params;
//                }
//            };
//
////            RequestQueueSingleton.getmInstance(getApplicationContext()).addToRequestQueue (restaurantRequest);
//
//        }

        zomatoHelper.getRestaurants(getApplicationContext(), preferred_cuisineIds, new RestaurantCallbacks() {
            @Override
            public void onRestaurantCallback(ArrayList<Restaurant> restaurants) {
                //LOG TEST
                for (int i =0; i<restaurants.size();i++){
                    Log.d("gottenRestaurants", restaurants.get(i).getRname());
                }
            }
        });

    }







//    public void getRestaurants(int[] preferred_cuisineIds){
//
//        for (int i=0; i<preferred_cuisineIds.length; i++){
//            Log.d("onResume: cuisineIds", String.valueOf(preferred_cuisineIds[i]));
//
//        }
//
//        //fetch results after offset
//        int start = 0;
//        //max number of results to display
//        int count = 20;
//
//        final String restaurant_url_start = restaurant_base_url + String.valueOf(start) +
//                "&count=" + String.valueOf(count) + "&cuisines=";
//
//
//        //add cuisine ids to request url
//        StringBuilder restaurant_url= new StringBuilder();
//        for (int i=0; i<preferred_cuisineIds.length; i++){
//            if (i==0){
//                restaurant_url = new StringBuilder(restaurant_url_start + String.valueOf(preferred_cuisineIds[i]));
//            }else {
//                restaurant_url.append(",").append(String.valueOf(preferred_cuisineIds[i]));
//            }
//        }
//        //Sort by rating
//        restaurant_url.append("&sort=rating");
//
//        //LOG TEST
//        Log.d("restaurant_url", restaurant_url.toString());
//
//        //Set JsonObjectRequest
//        JsonObjectRequest restaurantRequest = new JsonObjectRequest(Request.Method.GET, restaurant_url.toString(), null,
//                new Response.Listener<JSONObject>() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        //LOG
//                        Log.d("Response" , response.toString());
//
////                            try{
////
////                                JSONArray cuisinesJSONArray = response.getJSONArray("cuisines");
////
////                                //LOG
////                                Log.d("cuisinesJSONArray", cuisinesJSONArray.toString());
////
////                                for (int i = 0; i < cuisinesJSONArray.length(); i++){
////
////                                    cuisineList.clear();
////
////                                    JSONObject cuisineData = cuisinesJSONArray.getJSONObject(i).getJSONObject("cuisine");
////                                    cuisineList.add(cuisineData.getString("cuisine_name"));
////
////                                    /////////
////                                    Log.d("cuisine_name" , cuisineData.getString("cuisine_name"));
////
////                                }
////
////                            }catch (JSONException e){
////                                Log.d("ERROR", "Error (JSONException): " + e.toString());
////                            }
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
////            RequestQueueSingleton.getmInstance(getApplicationContext()).addToRequestQueue (restaurantRequest);
//
//
//
//    }


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
