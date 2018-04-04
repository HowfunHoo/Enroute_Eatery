package com.enroute.enroute;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.JsonObjectRequest;
import com.enroute.enroute.ZomatoHelpers.ZomatoHelper;
import com.enroute.enroute.interfaces.CuisineCallbacks;
import com.enroute.enroute.model.Cuisine;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class WaitForCuisinesActivity extends AppCompatActivity {

    //set navigation parameter
    private static final String TAG = "WaitForCuisinesActivity";
    private Context mcontext=WaitForCuisinesActivity.this;
    private static final int ACTIVITY_NUM=1;

    ArrayList<Cuisine> cuisines = new ArrayList<Cuisine>();

    //SharedPreference
    public SharedPreferences sharedPreferences;

    FirebaseAuth firebaseAuth;

    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wait_for_cuisines);
        setupBottomNavigationView();

        String preference= "";

        //firebase
        firebaseAuth= FirebaseAuth.getInstance();

        //get current user
        FirebaseUser currentUser= firebaseAuth.getCurrentUser();

        //Determine if the user has logged in
        if (currentUser == null){
            Toast.makeText(WaitForCuisinesActivity.this,
                    "We need your info. to recommend restaurants for you.", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(WaitForCuisinesActivity.this,
                    UserLoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();

        }else {
            sharedPreferences = getSharedPreferences("cur_user",0);

            preference = sharedPreferences.getString("cur_uprefer", "default");

            if (preference.equals("")){
                textView.setText("Seems like you haven't selected your preference");
            }
        }


        final String[] preferred_cuisines = preference.split(",");
//        final int[] preferred_cuisineIds = new int[preferred_cuisines.length];
        final int[] preferred_cuisineIds = new int[preferred_cuisines.length];

        final ZomatoHelper zomatoHelper = new ZomatoHelper();

        zomatoHelper.getCuisines(getApplicationContext(), new CuisineCallbacks() {
            @Override
            public void onCuisineCallbacks(JSONObject JSONObjectResult) {

                try{

                    JSONArray cuisinesJSONArray = JSONObjectResult.getJSONArray("cuisines");

                    //LOG TEST
                    Log.d("cuisinesJSONArray", cuisinesJSONArray.toString());

                    for (int i = 0; i < cuisinesJSONArray.length(); i++){

                        Cuisine cuisine = new Cuisine();

                        JSONObject cuisineData = cuisinesJSONArray.getJSONObject(i).getJSONObject("cuisine");

                        cuisine.setCuisine_id(cuisineData.getInt("cuisine_id"));
                        cuisine.setCuisine_name(cuisineData.getString("cuisine_name"));

                        cuisines.add(cuisine);

//                        latch.countDown();

                        //LOG TEST
                        Log.d("cuisines.size()", String.valueOf(cuisines.size()));
                        for (int j=0; j<cuisines.size(); j++){
                            Log.d("ArrayList-cuisines", cuisines.get(j).getCuisine_name());
                        }

//                        //Match preferred cuisine names with the IDs
                        for (int j=0; j<cuisines.size(); j++){
                            for (int x=0; x<preferred_cuisines.length; x++){
                                if (cuisines.get(j).getCuisine_name().equals(preferred_cuisines[x])){
                                    preferred_cuisineIds[x] = cuisines.get(j).getCuisine_id();
                                }
                            }
                        }

                        Intent intent =new Intent(WaitForCuisinesActivity.this, RestaurantRecommendationActivity.class);
                        intent.putExtra("preferred_cuisineIds", preferred_cuisineIds);
                        startActivity(intent);

//                        //LOG TEST
                        for (int y = 0; y<preferred_cuisineIds.length; y++){
                            Log.d("preferred_cuisineIds", String.valueOf(preferred_cuisineIds[y]));
                        }

                        //TODO
//                        getRestaurants(preferred_cuisineIds);



                    }

                }catch (JSONException e){
                    Log.d("ERROR", "Error (JSONException): " + e.toString());
                }


        }

        });
    }
    private void setupBottomNavigationView(){

        Log.d(TAG, "BottomNavigationView: setup BottomNavigationView");

        BottomNavigationViewEx bottomNavigationViewEx=(BottomNavigationViewEx) findViewById(R.id.buttomNavViewbar);
        BottomNavigationViewHelper.setupBottomNavigationView(bottomNavigationViewEx);
        BottomNavigationViewHelper.enableNavigation(mcontext, bottomNavigationViewEx);

        Menu menu=bottomNavigationViewEx.getMenu();
        MenuItem menuItem=menu.getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);

    }
}
