package com.enroute.enroute;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.enroute.enroute.Adapters.SpecialOffersAdapter;
import com.enroute.enroute.DBHelper.FirebaseHelper;
import com.enroute.enroute.interfaces.RestaurantCallbacks;
import com.enroute.enroute.model.Restaurant;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import java.util.ArrayList;

/**
 * This activity is to list the special offers
 *
 * @author Haofan Hou
 */
public class SpecialOffersActivity extends AppCompatActivity {

    DatabaseReference db;
    FirebaseHelper firebasehelper;
    SpecialOffersAdapter adapter;
    ListView lv_offers;

//    private static final String TAG = "SpecialOffersActivity";
    private Context mcontext=SpecialOffersActivity.this;
    private static final int ACTIVITY_NUM=2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_special_offers);
        setupBottomNavigationView();
        lv_offers = (ListView) findViewById(R.id.lv_offers);

        //Initialize Firebase Database
        db= FirebaseDatabase.getInstance().getReference();

        firebasehelper=new FirebaseHelper(db);

        //retrieve restaurant
        firebasehelper.retrieveRestaurant(new RestaurantCallbacks() {
            @Override
            public void onRestaurantCallback(ArrayList<Restaurant> restaurants) {

                adapter = new SpecialOffersAdapter(getApplicationContext(), restaurants);
                lv_offers.setAdapter(adapter);

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
