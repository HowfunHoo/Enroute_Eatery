package com.enroute.enroute.ZomatoHelpers;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.enroute.enroute.Singleton.RequestQueueSingleton;
import com.enroute.enroute.interfaces.CuisineCallbacks;
import com.enroute.enroute.model.Cuisine;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ZomatoHelper {

    //Set the default city as Halifax (The city_id of Halifax in Zomato API is 3099)
    String city_id = "3099";

    //Base Url for searching restaurants
    final String restaurant_base_url = "https://developers.zomato.com/api/v2.1/search?entity_id="
            + city_id + "&entity_type=city&start=";

    //Base url for searching all cuisines in the city
    final String cuisines_base_url = "https://developers.zomato.com/api/v2.1/cuisines?city_id=";


    //API Key for Zomato API
    final String api_key = "6cfc1a029b5b9a1d7e4b713ac61a99de";


    public ZomatoHelper() {

    }

    public ArrayList<Cuisine> getCuisines(Context ApplicationContext, final CuisineCallbacks cuisineCallbacks){

        final ArrayList<Cuisine> cuisines = new ArrayList<Cuisine>();

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

        RequestQueueSingleton.getmInstance(ApplicationContext).addToRequestQueue (cuisineRequest);

        return cuisines;

    }
}
