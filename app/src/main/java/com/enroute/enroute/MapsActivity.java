package com.enroute.enroute;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Dash;
import com.google.android.gms.maps.model.Dot;
import com.google.android.gms.maps.model.Gap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PatternItem;
import com.google.android.gms.maps.model.PolylineOptions;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import javax.net.ssl.HttpsURLConnection;

/**
 * References:
 * 1. https://www.androidtutorialpoint.com/intermediate/google-maps-draw-path-two-points-using-google-directions-google-map-android-api-v2/
 * 2. https://stackoverflow.com/questions/14710744/how-to-draw-road-directions-between-two-geocodes-in-android-google-map-v2?utm_medium=organic&utm_source=google_rich_qa&utm_campaign=google_rich_qa
 * 3. https://stackoverflow.com/questions/28295199/android-how-to-show-route-between-markers-on-googlemaps?utm_medium=organic&utm_source=google_rich_qa&utm_campaign=google_rich_qa
 * 4. https://stackoverflow.com/questions/29439754/parsing-json-from-the-google-maps-distancematrix-api-in-android?utm_medium=organic&utm_source=google_rich_qa&utm_campaign=google_rich_qa

 */
public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    private GoogleMap mMap;
    ArrayList<LatLng> MarkerPoints;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    Marker mCurrLocationMarker;
    LocationRequest mLocationRequest;

    private Button walk; // for walk mode button
    private Button cycle; // for cycle mode button

    private LatLng currentLoc; // for getting current location
    private boolean conditionCheckWalk; //for checking cycle or walk mode

    //lat&lng for destination
    private double dst_lat;
    private double dst_lng;

    private String duration;
    private String distance;

    private boolean count_walk; // for showing multiple route - walk mode
    private boolean count_cycle; // for showing multiple route - cycle mode

    //Shaerpreferences
    public SharedPreferences sharedPreferences;
    public SharedPreferences.Editor editor;

//    private BottomNavigationView bottomNavigationView;


    private static final String TAG = "MapsActivity";
    private Context mcontext=MapsActivity.this;
    private static final int ACTIVITY_NUM=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        walk = findViewById(R.id.walk); // for walk
        cycle = findViewById(R.id.cycle); // for cycle

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkLocationPermission();
        }
        // Initializing
        MarkerPoints = new ArrayList<>();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //automatic completion
        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

        //set the range for automatically completed places be around Halifax
        autocompleteFragment.setBoundsBias(new LatLngBounds(
                new LatLng(44.6375656 - 1, -63.5871266 - 1),
                new LatLng(44.6375656 + 1, -63.5871266 + 1)));

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                dst_lat = place.getLatLng().latitude;
                dst_lng = place.getLatLng().longitude;
                //Log.i("test", "Place: " + place.getName());
                //Log.i("test", "Address: " + place.getAddress());
            }

            @Override
            public void onError(Status status) {
                Log.i("test", "An error occurred: " + status);
            }
        });

        setupBottomNavigationView();

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        //Initialize Google Play Services
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
                mMap.setMyLocationEnabled(true);
            }
        }
        else {
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
        }

        //get data from RestaurantRecommendationActivity
        Intent intent = this.getIntent();
        dst_lat = intent.getDoubleExtra("ResLat",0.0);
        dst_lng = intent.getDoubleExtra("ResLng",0.0);

        if (dst_lat!=0.0 && dst_lng!=0.0){

            mMap.clear();
            conditionFunction("driving", "false");
            conditionFunction("driving", "true");

            sharedPreferences = getSharedPreferences("currentLoc",0);
            Double cur_lat = Double.valueOf(sharedPreferences.getString("cur_lat","default"));
            Double cur_lng = Double.valueOf(sharedPreferences.getString("cur_lng","default"));
            currentLoc = new LatLng(cur_lat, cur_lng);
            conditionFunction("driving", "false");
        }

        // on click listener for walk mode
        walk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMap.clear();
                conditionCheckWalk = true;
                count_walk = false;
                conditionFunction("walking", "false"); // shows shortest route - walk mode
                conditionFunction("walking", "true"); // shows alternate route - walk mode
            }
        });

        //change the position of MyLocation Button
        mMap.setPadding(0,200,0,0);

        // on click listener for cycle mode
        cycle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMap.clear();
                conditionCheckWalk = false;
                count_cycle = false;
                conditionFunction("bicycling", "false"); // shows shortest route - cycle mode
                conditionFunction("bicycling", "true"); // shows alternate route - cycle mode
                }
        });

    }

    /**
     *
     * @param origin: take latlang of origin which will be user's live or current location
     * @param dest: take user's converted latlang from the entered string
     * @return: which gives the whole url
     */
    private String getUrl(LatLng origin, LatLng dest, String travel_mode, String alternate_route) {
        String mode = "mode=" + travel_mode + "&";
        String alternatives = "&alternatives=" + alternate_route ;
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;           // Origin of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;            // Destination of route
        String sensor = "sensor=false";                                                     // Sensor enabled
        String parameters = mode + str_origin + "&" + str_dest + "&" + sensor + alternatives;                     // Building the parameters to the web service
        String output = "json";                                                             // Output format

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;
        return url;
    }

    /**
     *
     * @param poi: takes latlang of restaurants
     * @return gives the url for displaying restaurants
     */
    private String getRestaurantUrl(LatLng poi) {
        String str_poi = "location=" + poi.latitude + "," + poi.longitude;          // One of the place of route
        String radius = "radius=20";                                                // Radius in meters

        // Building the parameters to the web service
        String parameters = str_poi + "&" + radius + "&type=restaurant&key=AIzaSyBSuFO5k_nS7L7-MsHBaaJQLKsdwbD0A-c";
        //String parameters = str_poi + "&" + radius + "&type=restaurant&key=AIzaSyBXCCDI4g1xqM4TnNcWSSJWzie5eV8OnWE";    // Need to pass MAP key with each request

        String output = "json";                     // Output format
        String url = "https://maps.googleapis.com/maps/api/place/nearbysearch/" + output + "?" + parameters;
        return url;
    }

    /**
     *
     * @param poi: takes latlang for Convenience store
     * @return a url which will be used to marked down Convenience store
     */
    private String getConvenienceUrl(LatLng poi) {
        String str_poi = "location=" + poi.latitude + "," + poi.longitude;          // One of the place of route
        String radius = "radius=100";                                                // Radius in meters

        // Building the parameters to the web service
        String parameters = str_poi + "&" + radius + "&type=convenience_store&key=AIzaSyDDrOrd1iT25wyrMHajcaluBJoi9Ezuois";

        //String parameters = str_poi + "&" + radius + "&type=restaurant&key=AIzaSyBXCCDI4g1xqM4TnNcWSSJWzie5eV8OnWE";    // Need to pass MAP key with each request
        String output = "json";                     // Output format
        String url = "https://maps.googleapis.com/maps/api/place/nearbysearch/" + output + "?" + parameters;
        return url;
    }


    /**
     * A method to download json data from url
     */
    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();
            // Reading data from url
            iStream = urlConnection.getInputStream();
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

    // Fetches data from url passed
    private class FetchUrl extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... url) {

            // For storing data from web service
            String data = "";
            try {
                // Fetching the data from web service
                data = downloadUrl(url[0]);
                //Log.d("Background Task data", data.toString());
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
                //Log.d("Background Task data", data.toString());
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


    /**
     * A class to parse the Google Places in JSON format
     *
     * Reference:
     https://stackoverflow.com/questions/29439754/parsing-json-from-the-google-maps-distancematrix-api-in-android?utm_medium=organic&utm_source=google_rich_qa&utm_campaign=google_rich_qa
     */
    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                //Log.d("ParserTask",jsonData[0].toString());
                DataParser parser = new DataParser();
                //Log.d("ParserTask", parser.toString());

                // Starts parsing data
                routes = parser.parse(jObject);
                //Log.d("ParserTask","Executing routes");
                //Log.d("ParserTask",routes.toString());

            } catch (Exception e) {
                Log.d("ParserTask",e.toString());
                e.printStackTrace();
            }
            return routes;
        }

        /**
         * Reference:
         * https://stackoverflow.com/questions/29439754/parsing-json-from-the-google-maps-distancematrix-api-in-android?utm_medium=organic&utm_source=google_rich_qa&utm_campaign=google_rich_qa
         *
         * Executes in UI thread, after the parsing process
         */

        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList<LatLng> points;
            PolylineOptions lineOptions = null;

            // Traversing through all the routes
            for (int i = 0; i < result.size(); i++) {
                points = new ArrayList<>();
                lineOptions = new PolylineOptions();

                // Fetching i-th route
                List<HashMap<String, String>> path = result.get(i);

                distance = path.get(0).get("Distance"); // fetching Distance from thread
                duration = path.get(0).get("Duration"); // fetching Duration from thread

                String distance_var = "Distance = " + distance;
                String duration_var = "Duration = " + duration;

                Toast.makeText(MapsActivity.this, distance_var + ", " + duration_var , Toast.LENGTH_LONG ).show();

                // Fetching all the points in i-th route
                for (int j = 1; j < path.size(); j++) {
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

                    //Start showing convenience store
                    String convenienceUrl = getConvenienceUrl(position);
                    FetchConvenienceUrl FetchConvenienceUrl = new FetchConvenienceUrl();
                    // Start downloading json data from Google search nearby API
                    FetchConvenienceUrl.execute(convenienceUrl);
                }

                // Adding all the points in the route to LineOptions
                lineOptions.addAll(points);

                // condition check for walk or cycle mode
                if(conditionCheckWalk){
                    // for walking mode
                    if (!count_walk){
                        lineOptions.color(Color.BLUE); // display shortest path in blue color - walk mode
                        count_walk = true;}
                    else {
                        lineOptions.color(Color.GRAY); // display alternate path - walk mode
                        count_walk = true;
                    }
                    lineOptions.width(13);
                    lineOptions.pattern(Arrays.<PatternItem>asList(
                        new Dot(), new Gap(10)));

                    Log.d("onPostExecute","onPostExecute lineoptions decoded");
                }

                else if (!conditionCheckWalk){
                    // for cycle mode
                    if (!count_cycle){
                        lineOptions.color(Color.BLUE);  // display shortest path in blue color - cycle mode
                        count_cycle = true;}
                    else {
                        lineOptions.color(Color.GRAY); // display alternate path - cycle mode
                        count_cycle = false;
                    }
                    lineOptions.width(13);
                    lineOptions.pattern(Arrays.<PatternItem>asList(
                            new Dash(17), new Gap(0)));

                    Log.d("onPostExecute","onPostExecute lineoptions decoded");
                }
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

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(Bundle bundle) {

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        // Do Nothing
    }

    @Override
    public void onLocationChanged(Location location) {

        mLastLocation = location;
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }

        //Place current location marker
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        currentLoc = latLng;

        sharedPreferences = this.getSharedPreferences("currentLoc", 0);
        editor = sharedPreferences.edit();
        editor.putString("cur_lat", String.valueOf(currentLoc.latitude));
        editor.putString("cur_lng", String.valueOf(currentLoc.longitude));
        editor.apply();

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Current Position");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
        mCurrLocationMarker = mMap.addMarker(markerOptions);

        //move map camera
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15)); //current location zoom

        //stop location updates
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }

        System.out.println("Cur Lat"+currentLoc.latitude);
        System.out.println("Cur lng"+currentLoc.longitude);

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        // Do Nothing
    }

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    public boolean checkLocationPermission(){
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Asking user if explanation is needed
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                //Prompt the user once explanation has been shown
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);

            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted. Do the
                    // contacts-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        mMap.setMyLocationEnabled(true);
                    }

                } else {

                    // Permission denied, Disable the functionality that depends on this permission.
                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }

    private class DataLongOperationAsynchTask extends AsyncTask<String, Void, String[]> {
        ProgressDialog dialog = new ProgressDialog(MapsActivity.this);
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setMessage("Please wait...");
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
        }

        @Override
        protected String[] doInBackground(String... params) {
            String response;
            try {
                response = getLatLongByURL("http://maps.google.com/maps/api/geocode/json?address=halifax&sensor=false");
                Log.d("response",""+response);
                return new String[]{response};
            } catch (Exception e) {
                return new String[]{"error"};
            }
        }

        @Override
        protected void onPostExecute(String... result) {
            try {
                JSONObject jsonObject = new JSONObject(result[0]);

                double lng = ((JSONArray)jsonObject.get("results")).getJSONObject(0)
                        .getJSONObject("geometry").getJSONObject("location")
                        .getDouble("lng");

                double lat = ((JSONArray)jsonObject.get("results")).getJSONObject(0)
                        .getJSONObject("geometry").getJSONObject("location")
                        .getDouble("lat");

                LatLng newCorrdinates = new LatLng(lat, lng);
                Log.d("latitude", "" + lat);
                Log.d("longitude", "" + lng);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
        }
    }

    /**
     * mode check function: it will check for cycle or walk mode and draw path according to destination and user's current location
     */

    public void conditionFunction(String travel_mode, String alternate_mode){
        //mMap.clear(); // clearing the map first
        MarkerOptions options = new MarkerOptions();

        LatLng origin = currentLoc;
        LatLng dest = new LatLng(dst_lat ,dst_lng);

        // Getting URL to the Google Directions API
        String url = getUrl(origin, dest, travel_mode, alternate_mode);
        Log.d("onMapClick", url.toString());
        FetchUrl FetchUrl = new FetchUrl();

        // Start downloading json data from Google Directions API
        FetchUrl.execute(url);

        // Setting the position of the marker
        options.position(origin);
        options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
        options.title("Origin");
        mMap.addMarker(options);

        options.position(dest);
        options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
        options.title("Destination");
        mMap.addMarker(options);

        //move map camera
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(origin));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(dest));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15)); //search path zoom

    }

    /**
     *
     * @param requestURL we will provide a prepared url which will be used to find latlang of a place
     * @return
     */
    public String getLatLongByURL(String requestURL) {
        URL url;
        String response = "";
        try {
            url = new URL(requestURL);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(15000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded");
            conn.setDoOutput(true);
            int responseCode = conn.getResponseCode();

            if (responseCode == HttpsURLConnection.HTTP_OK) {
                String line;
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                while ((line = br.readLine()) != null) {
                    response += line;
                }
            } else {
                response = "";
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
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
                //Log.d("Restaurants data => ",jsonData[0].toString());
                RestDataParser restParser = new RestDataParser();
                //Log.d("ParserRestaurantTask", restParser.toString());

                // Starts parsing data
                restaurants = restParser.parse(jObject);

                //Log.d("ParserRestaurantTask","Executing restaurants");
                //Log.d("ParserRestaurantTask",restaurants.toString());

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
                    double lat          = Double.parseDouble(point.get("lat"));
                    double lng          = Double.parseDouble(point.get("lng"));
                    String restName     = point.get("name");
                    String restAddress  = point.get("vicinity");
                    LatLng position = new LatLng(lat, lng);
                    // Setting the position of the marker
                    options.position(position);
                    options.title(restName);
                    options.snippet(restAddress);
                    options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));  //For the restaurants location, the color of marker is Yellow
                }
                // Drawing marker in the Google Map on route
                if(options != null) {
                    mMap.addMarker(options);                  // Setting the position of the marker
                }
                else {
                    Log.d("onPostExecute","without any restaurants drawn");
                }
            }
        }
    }


    // Fetches data from url passed for restaurant search in near by API
    private class FetchConvenienceUrl extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... url) {
            // For storing data from web service
            String data = "";
            try {
                // Fetching the data from web service
                data = downloadUrl(url[0]);
                //Log.d("Background Task data", data.toString());
            } catch (Exception e) {
                //Log.d("Background Task", e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            // New data parser class to parse nearby data
            MapsActivity.ParserConvenienceTask parserRestTask = new MapsActivity.ParserConvenienceTask();
            // Invokes the thread for parsing the JSON data
            parserRestTask.execute(result);
        }
    }


    // Class for restaurants pin-ups
    private class ParserConvenienceTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {
            List<List<HashMap<String, String>>> convenience = null;
            JSONObject jObject;

            try {
                jObject = new JSONObject(jsonData[0]);
                //Log.d("Convenience data => ",jsonData[0].toString());
                RestDataParser restParser = new RestDataParser();
                //Log.d("ParserConvenienceTask", restParser.toString());

                // Starts parsing data
                convenience = restParser.parse(jObject);

                //Log.d("ParserConvenienceTask","Executing restaurants");
                //Log.d("ParserConvenienceTask",convenience.toString());

            } catch (Exception e) {
                //Log.d("ParserConvenienceTask",e.toString());
                e.printStackTrace();
            }
            return convenience;
        }

        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList<LatLng> points;

            // Creating MarkerOptions
            MarkerOptions options = new MarkerOptions();
            for (int i = 0; i < result.size(); i++) {
                List<HashMap<String, String>> convenience = result.get(i);             // Fetching i-th Convenience
                // Fetching all the points in i-th route
                for (int j = 0; j < convenience.size(); j++) {
                    HashMap<String, String> point = convenience.get(j);
                    double lat           = Double.parseDouble(point.get("lat"));
                    double lng           = Double.parseDouble(point.get("lng"));
                    String storeName     = point.get("name");
                    String storeAddress  = point.get("vicinity");
                    LatLng position      = new LatLng(lat, lng);
                    // Setting the position of the marker
                    options.position(position);
                    options.title(storeName);
                    options.snippet(storeAddress);
                    options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));  //For the convenience location, the color of marker is Orange
                }

                // Drawing marker in the Google Map on route
                if(options != null) {
                    mMap.addMarker(options);                  // Setting the position of the marker
                }
                else {
                    Log.d("onPostExecute","without any convenience store drawn");
                }
            }
        }
    }

    // bottom navigation
    private void setupBottomNavigationView(){
        Log.d(TAG, "BottomNavigationView: setup BottomNavigationView");
        BottomNavigationViewEx bottomNavigationViewEx=(BottomNavigationViewEx) findViewById(R.id.buttomNavViewbar);
        BottomNavigationViewHelper.setupBottomNavigationView(bottomNavigationViewEx);
        BottomNavigationViewHelper.enableNavigation(mcontext, bottomNavigationViewEx);
        Menu menu=bottomNavigationViewEx.getMenu();
        MenuItem menuItem=menu.getItem(ACTIVITY_NUM);

        menuItem.setChecked(true);
    }


//    protected void selectFragment(MenuItem item) {
//
//        item.setChecked(true);
//        switch (item.getItemId()) {
//            case R.id.home:
//                // Action to perform when Home Menu item is selected.
//                startActivity(new Intent(this, MapsActivity.class));
//                break;
//            case R.id.userprofile:
//                // Action to perform when Bag Menu item is selected.
//                startActivity(new Intent(this, log_in.class));
//                break;
//            case R.id.specialoffers:
//                // Action to perform when Account Menu item is selected.
//                startActivity(new Intent(this, SpecialOffer.class));
//                break;
//        }
//    }

}
