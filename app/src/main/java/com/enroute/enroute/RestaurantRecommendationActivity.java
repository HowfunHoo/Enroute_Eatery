package com.enroute.enroute;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.enroute.enroute.Adapters.RestaurantRecommendAdapter;
import com.enroute.enroute.ZomatoHelpers.ZomatoHelper;
import com.enroute.enroute.interfaces.RestaurantCallbacks;
import com.enroute.enroute.model.Restaurant;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import java.util.ArrayList;


/**
 * This Activity is to list the restaurants based on the current user's preferred cuisines.
 * When a listed restaurant is clicked, the app will direct to the map and show the route to
 * the clicked restaurant
 *
 * @author Haofan Hou
 */

public class RestaurantRecommendationActivity extends AppCompatActivity  {

    //set navigation parameter
    private static final String TAG = "RestaurantRecommendationActivity";
    private Context mcontext=RestaurantRecommendationActivity.this;
    private static final int ACTIVITY_NUM=1;

    //Adapter
    RestaurantRecommendAdapter adapter;

    ListView lv_suggest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_recommendation);

        setupBottomNavigationView();

        lv_suggest = (ListView)findViewById(R.id.lv_suggest);

        final ZomatoHelper zomatoHelper = new ZomatoHelper();

        //Receive cuisine IDs from WaitForCuisinesActivity
        Bundle bundle = this.getIntent().getExtras();
        int[] preferred_cuisineIds = new int[0];
        if (bundle != null) {
            preferred_cuisineIds = bundle.getIntArray("preferred_cuisineIds");
        }

        //LOG TEST
        if (preferred_cuisineIds != null) {
            for (int preferred_cuisineId : preferred_cuisineIds) {
                Log.d("preferred_cuisineId", String.valueOf(preferred_cuisineId));
            }
        }else {
            System.out.println("preferred_cuisineId is null!");
        }

        //Get restaurants based on preferences
        zomatoHelper.getRestaurants(getApplicationContext(), preferred_cuisineIds, new RestaurantCallbacks() {
            @Override
            public void onRestaurantCallback(ArrayList<Restaurant> restaurants) {
                //LOG TEST
                for (int i =0; i<restaurants.size();i++){
                    Log.d("gottenRestaurants", restaurants.get(i).getRname());
                }

                adapter = new RestaurantRecommendAdapter(RestaurantRecommendationActivity.this, restaurants);
                lv_suggest.setAdapter(adapter);
            }
        });

    }

    /**
     * A method to display the bottom navigation bar
     */
    private void setupBottomNavigationView(){

        BottomNavigationViewEx bottomNavigationViewEx=(BottomNavigationViewEx) findViewById(R.id.buttomNavViewbar);
        BottomNavigationViewHelper.setupBottomNavigationView(bottomNavigationViewEx);
        BottomNavigationViewHelper.enableNavigation(mcontext, bottomNavigationViewEx);

        Menu menu=bottomNavigationViewEx.getMenu();
        MenuItem menuItem=menu.getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);

    }

}
