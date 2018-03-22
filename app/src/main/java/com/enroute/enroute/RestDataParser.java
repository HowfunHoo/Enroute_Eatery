package com.enroute.enroute;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by SINGH on 04/03/2018.
 *
 */

public class RestDataParser {
    /** Receives a JSONObject and returns a list of lists containing latitude and longitude */
    public List<List<HashMap<String,String>>> parse(JSONObject jObject){

        List<List<HashMap<String, String>>> restList = new ArrayList<>() ;
        JSONArray jResults;
        JSONArray jGeometry;
        JSONArray jLocation;

        try {
            jResults = jObject.getJSONArray("results");
            List path = new ArrayList<>();
            /** Traversing all results only if we have results > 1 */
            if ( jResults.length() > 0 ) {
                for(int i=0;i<jResults.length();i++) {
                    HashMap<String, String> hm = new HashMap<>();
                    hm.put("lat", jResults.getJSONObject(i).getJSONObject("geometry").getJSONObject("location").getString("lat"));
                    hm.put("lng", jResults.getJSONObject(i).getJSONObject("geometry").getJSONObject("location").getString("lng"));
                    // Restaurant name
                    hm.put("name",jResults.getJSONObject(i).getString("name"));

                    path.add(hm);
                    Log.d("Inside parser class", hm.toString());
                }
                restList.add(path);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }catch (Exception e){
        }
        //Log.d("Inside parser class", restList.toString());
        return restList;
    }

}

