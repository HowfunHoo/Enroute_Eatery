package com.enroute.enroute.ZomatoHelpers;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.enroute.enroute.Singleton.RequestQueueSingleton;
import com.enroute.enroute.interfaces.CuisineCallbacks;
import com.enroute.enroute.interfaces.RestaurantCallbacks;
import com.enroute.enroute.model.Cuisine;
import com.enroute.enroute.model.Restaurant;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ZomatoHelper {

    //Set the default city as Halifax (The city_id of Halifax in Zomato API is 3099)
    private String city_id = "3099";

    //Base Url for searching restaurants
    final String restaurant_base_url = "https://developers.zomato.com/api/v2.1/search?entity_id="
            + city_id + "&entity_type=city&start=";

    //Base url for searching all cuisines in the city
    private final String cuisines_base_url = "https://developers.zomato.com/api/v2.1/cuisines?city_id=";


    //API Key for Zomato API
//    private final String api_key = "6cfc1a029b5b9a1d7e4b713ac61a99de";
    private final String api_key = "52daad033db74e49b6ab38e67e0b5174";


    public ZomatoHelper() {

    }


    public void getCuisines(Context ApplicationContext, final CuisineCallbacks cuisineCallbacks){

        final ArrayList<Cuisine> cuisines = new ArrayList<Cuisine>();

        String cuisines_url = cuisines_base_url.concat(city_id);

        //Set JsonObjectRequest
        JsonObjectRequest cuisineRequest = new JsonObjectRequest(Request.Method.GET, cuisines_url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.d("response_cuisines", String.valueOf(response));

                        cuisineCallbacks.onCuisineCallbacks(response);

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

        RequestQueueSingleton.getmInstance(ApplicationContext).addToRequestQueue (cuisineRequest);

    }


    public void getRestaurants(Context context, int[] preferred_cuisineIds, final RestaurantCallbacks restaurantCallbacks){

        final ArrayList<Restaurant> restaurants = new ArrayList<>();

        //fetch results after offset
        int start = 0;
        //max number of results to display
        int count = 20;

        final String restaurant_url_start = restaurant_base_url + String.valueOf(start) +
                "&count=" + String.valueOf(count) + "&cuisines=";


        //add cuisine ids to request url
        StringBuilder restaurant_url= new StringBuilder();
        for (int i=0; i<preferred_cuisineIds.length; i++){
            if (i==0){
                restaurant_url = new StringBuilder(restaurant_url_start + String.valueOf(preferred_cuisineIds[i]));
            }else {
                restaurant_url.append(",").append(String.valueOf(preferred_cuisineIds[i]));
            }
        }
        //Sort by rating
        restaurant_url.append("&sort=rating");

        //LOG TEST
        Log.d("restaurant_url", restaurant_url.toString());

        //Set JsonObjectRequest
        JsonObjectRequest restaurantRequest = new JsonObjectRequest(Request.Method.GET, restaurant_url.toString(), null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //LOG
                        Log.d("Response" , response.toString());

                        try {
                            Restaurant restaurant = new Restaurant();

                            JSONArray restaurantJSONArray = response.getJSONArray("restaurants");

                            for (int i = 0; i < restaurantJSONArray.length(); i++){


                                JSONObject restaurantData = restaurantJSONArray.getJSONObject(i).getJSONObject("restaurant");

                                restaurant.setRid(String.valueOf(restaurantData.getJSONObject("R").getInt("res_id")));
                                restaurant.setRname(restaurantData.getString("name"));
                                restaurant.setRpicurl(restaurantData.getString("photos_url"));
                                restaurant.setRaddress(restaurantData.getJSONObject("location").getString("address"));
                                restaurant.setRcity(restaurantData.getJSONObject("location").getString("city"));
                                restaurant.setRlat(Double.parseDouble(restaurantData.getJSONObject("location").getString("latitude")));
                                restaurant.setRlng(Double.parseDouble(restaurantData.getJSONObject("location").getString("longitude")));
                                restaurant.setRaveragecost(String.valueOf(restaurantData.getInt("average_cost_for_two")));
                                restaurant.setRrate(restaurantData.getJSONObject("user_rating").getString("aggregate_rating"));
                                restaurant.setRcuisines(restaurantData.getString("cuisines"));

                                restaurants.add(restaurant);

                                restaurantCallbacks.onRestaurantCallback(restaurants);

                                }
                        }catch (JSONException e){
                            e.printStackTrace();
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

            RequestQueueSingleton.getmInstance(context).addToRequestQueue (restaurantRequest);



    }

}
