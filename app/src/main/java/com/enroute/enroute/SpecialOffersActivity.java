package com.enroute.enroute;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.enroute.enroute.Adapters.SpecialOffersAdapter;
import com.enroute.enroute.DBHelper.FirebaseHelper;
import com.enroute.enroute.interfaces.RestaurantCallbacks;
import com.enroute.enroute.model.Restaurant;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class SpecialOffersActivity extends AppCompatActivity {

    DatabaseReference db;
    FirebaseHelper firebasehelper;
    SpecialOffersAdapter adapter;
    ListView lv_offers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_special_offers);

        lv_offers = (ListView) findViewById(R.id.lv_offers);

        //Initialize Firebase Database
        db= FirebaseDatabase.getInstance().getReference();

        firebasehelper=new FirebaseHelper(db);

        //TEST

        firebasehelper.retrieveRestaurant(new RestaurantCallbacks() {
            @Override
            public void onRestaurantCallback(ArrayList<Restaurant> restaurants) {

                adapter = new SpecialOffersAdapter(getApplicationContext(), restaurants);
                lv_offers.setAdapter(adapter);

            }
        });


    }
}
