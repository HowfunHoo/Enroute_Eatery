package com.enroute.enroute;


import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    ArrayList<LatLng> MarkerPoints;

    // TO be used for storing all Lat/Lang for a route
    protected List<List<List<HashMap<String, String>>>> routesLatLangs = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        MarkerPoints =  new ArrayList<>();

    }

    private String getUrl(LatLng origin, LatLng dest) {
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;           // Origin of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;            // Destination of route
        String sensor = "sensor=false";                                                     // Sensor enabled
        String parameters = str_origin + "&" + str_dest + "&" + sensor;                     // Building the parameters to the web service
        String output = "json";                                                             // Output format
        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;
        return url;
    }

    private String getRestaurantUrl(LatLng poi) {
        String str_poi = "location=" + poi.latitude + "," + poi.longitude;          // One of the place of route
        String radius = "radius=50";                                                // Radius in meters
        // Building the parameters to the web service
        String parameters = str_poi + "&" + radius + "&type=restaurant&key=AIzaSyDDrOrd1iT25wyrMHajcaluBJoi9Ezuois";
        //String parameters = str_poi + "&" + radius + "&type=restaurant&key=AIzaSyBXCCDI4g1xqM4TnNcWSSJWzie5eV8OnWE";    // Need to pass MAP key with each request
        String output = "json";                     // Output format

        // Building the url to the web service
        // https://maps.googleapis.com/maps/api/place/nearbysearch/json?
        // location=44.63579,-63.59289&radius=500&type=restaurant&key=AIzaSyBXCCDI4g1xqM4TnNcWSSJWzie5eV8OnWE
        String url = "https://maps.googleapis.com/maps/api/place/nearbysearch/" + output + "?" + parameters;

        return url;
    }


    // Fetches data from url passed
    private class FetchUrl extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... url) {
            // For storing data from web service
            String data = "";
            try {
                // Fetching the data from web service
                data = downloadUrl(url[0]);
                Log.d("Background Task data", data.toString());
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            ParserTask parserTask = new ParserTask();
            // Invokes the thread for parsing the JSON data
            parserTask.execute(result);
        }
    }

    // Fetches data from url passed for restaurant search in near by API
    private class FetchRestUrl extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... url) {
            // For storing data from web service
            String data = "";
            try {
                // Fetching the data from web service
                data = downloadUrl(url[0]);
                Log.d("Background Task data", data.toString());
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            // Write new data parser class to parse nearby data
            ParserRestaurantTask parserRestTask = new ParserRestaurantTask();
            // Invokes the thread for parsing the JSON data
            parserRestTask.execute(result);
        }
    }


    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);
            urlConnection = (HttpURLConnection) url.openConnection();       // Creating an http connection to communicate with url
            urlConnection.connect();                                        // Connecting to url
            iStream = urlConnection.getInputStream();                       // Reading data from url
            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));
            StringBuffer sb = new StringBuffer();
            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            data = sb.toString();
            Log.d("downloadUrl", data.toString());
            br.close();

        } catch (Exception e) {
            Log.d("Exception", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    /**
     * Manipulates the map once available.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
      
        // Use the current location here
        // PV 44.640676, -63.578326  & SB 44.642750, -63.578449  // dal 44.636345, -63.593050

        LatLng currentLocation = new LatLng(44.642750, -63.578449);
        LatLng destLocation    = new LatLng(44.640676, -63.578326);
        mMap.addMarker(new MarkerOptions().position(currentLocation).title("Marker at current Location !!"));
        // Set the zoom level
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 18 ) );

        // Already two locations
        if (MarkerPoints.size() > 1) {
            MarkerPoints.clear();
            mMap.clear();
        }
        // Adding new item to the ArrayList
        MarkerPoints.add(currentLocation);
        MarkerPoints.add(destLocation);
        // Creating MarkerOptions
        MarkerOptions options = new MarkerOptions();
        options.position(currentLocation);          // Setting the position of the marker
        options.position(destLocation);             // Setting the position of the marker

        /**
         * For the start location, the color of marker is GREEN and
         * for the end location, the color of marker is RED.
         */
        if (MarkerPoints.size() == 1) {
            options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
        } else if (MarkerPoints.size() == 2) {
            options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        }
        // Add new marker to the Google Map Android API V2
        mMap.addMarker(options);

        // Checks, whether start and end locations are captured
        if (MarkerPoints.size() >= 2) {
            LatLng origin = MarkerPoints.get(0);
            LatLng dest = MarkerPoints.get(1);

            // Getting URL to the Google Directions API
            String url = getUrl(origin, dest);
            Log.d("onMapClick", url.toString());
            FetchUrl FetchUrl = new FetchUrl();

            // Start downloading json data from Google Directions API
            FetchUrl.execute(url);

            //move map camera
            mMap.moveCamera(CameraUpdateFactory.newLatLng(origin));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(12));
        }
    }


    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {
        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {
            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;
            try {
                jObject = new JSONObject(jsonData[0]);
                Log.d("ParserTask",jsonData[0].toString());
                DataParser parser = new DataParser();
                Log.d("ParserTask", parser.toString());
                // Starts parsing data
                routes = parser.parse(jObject);
                Log.d("ParserTask","Executing routes");
                Log.d("ParserTask",routes.toString());
            } catch (Exception e) {
                Log.d("ParserTask",e.toString());
                e.printStackTrace();
            }
            return routes;
        }

        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList<LatLng> points;
            PolylineOptions lineOptions = null;

            // Traversing through all the routes
            for (int i = 0; i < result.size(); i++) {
                points = new ArrayList<>();
                lineOptions = new PolylineOptions();
                List<HashMap<String, String>> path = result.get(i);             // Fetching i-th route

                // Fetching all the points in i-th route
                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);
                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);
                    points.add(position);


                    //Start showing restaurants
                    String restUrl = getRestaurantUrl(position);
                    FetchRestUrl FetchRestUrl = new FetchRestUrl();
                    // Start downloading json data from Google search nearby API
                    FetchRestUrl.execute(restUrl);


               }

                // Adding all the points in the route to LineOptions
                lineOptions.addAll(points);
                lineOptions.width(10);
                lineOptions.color(Color.GREEN);

                Log.d("onPostExecute","onPostExecute lineoptions decoded");

            }

            // Drawing polyline in the Google Map for the i-th route
            if(lineOptions != null) {
                mMap.addPolyline(lineOptions);
            }
            else {
                Log.d("onPostExecute","without Polylines drawn");
            }
        }
    }



    // Class for restaurants pin-ups
    private class ParserRestaurantTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {
            List<List<HashMap<String, String>>> restaurants = null;
            JSONObject jObject;

            try {
                jObject = new JSONObject(jsonData[0]);
                Log.d("ParserRestaurantTask",jsonData[0].toString());
                RestDataParser restParser = new RestDataParser();
                Log.d("ParserRestaurantTask", restParser.toString());

                // Starts parsing data
                restaurants = restParser.parse(jObject);

                Log.d("ParserRestaurantTask","Executing restaurants");
                Log.d("ParserRestaurantTask",restaurants.toString());

            } catch (Exception e) {
                Log.d("ParserRestaurantTask",e.toString());
                e.printStackTrace();
            }
            return restaurants;
        }

        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList<LatLng> points;
            // Creating MarkerOptions
            MarkerOptions options = new MarkerOptions();
            for (int i = 0; i < result.size(); i++) {
                List<HashMap<String, String>> restaurant = result.get(i);             // Fetching i-th restaurant
                // Fetching all the points in i-th route
                for (int j = 0; j < restaurant.size(); j++) {
                    HashMap<String, String> point = restaurant.get(j);
                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);
                    // Setting the position of the marker
                    options.position(position);
                }

                // Drawing marker in the Google Map on route
                if(options != null) {
                    mMap.addMarker(options);                  // Setting the position of the marker
                    options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));  //For the restaurants location, the color of marker is Yellow
                }
                else {
                    Log.d("onPostExecute","without any restaurants drawn");
                }
            }
        }
    }
}