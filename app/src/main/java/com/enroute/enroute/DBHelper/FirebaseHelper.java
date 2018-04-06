package com.enroute.enroute.DBHelper;

import android.util.Log;

import com.enroute.enroute.interfaces.RestaurantCallbacks;
import com.enroute.enroute.interfaces.UserCallbacks;
import com.enroute.enroute.model.Business;
import com.enroute.enroute.model.Restaurant;
import com.enroute.enroute.model.User;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * This helper is to handle the operations with the Firebase Database
 * @author Haofan Hou
 */
public class FirebaseHelper {
    private DatabaseReference db;
    private ArrayList<Restaurant> restaurants = new ArrayList<>();
    private ArrayList<User> user=new ArrayList<>();
    public FirebaseHelper(DatabaseReference db) {
        this.db = db;
    }

    /**
     *  A method to save a restaurant's info. into Firebase db
     *
     * @param restaurant A instance of Restaurant model
     * @return The status if saved successfully
     */
    public Boolean saveRestaurant(Restaurant restaurant)
    {
        Boolean saved = null;
        if(restaurant==null)
        {
            saved = false;
        }else
        {
            try
            {
                db.child("Restaurant").push().setValue(restaurant);
                saved =true;

            }catch (DatabaseException e)
            {
                e.printStackTrace();
                saved =false;
            }
        }

        return saved;
    }

    /**
     * A method to save a business's info. into Firebase db
     *
     * @param business A instance of Business model
     * @return The status if saved successfully
     */
    public Boolean saveBusiness(Business business)
    {
        Boolean saved = null;
        if(business==null)
        {
            saved = false;
        }else
        {
            try
            {
                db.child("Business").push().setValue(business);
                saved =true;

            }catch (DatabaseException e)
            {
                e.printStackTrace();
                saved =false;
            }
        }

        return saved;
    }

    /**
     * A method to retrieve restaurant info. from Firebase db
     *
     * @param restaurantCallbacks A instance of Restaurant model
     */
    public void retrieveRestaurant(final RestaurantCallbacks restaurantCallbacks) {

        db.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                for (DataSnapshot ds : dataSnapshot.getChildren()) {

                    Restaurant restaurant = ds.getValue(Restaurant.class);

                    if (restaurant != null && restaurant.getRid() != null) {
                        restaurants.add(restaurant);
                    }

                }

                restaurantCallbacks.onRestaurantCallback(restaurants);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                for (DataSnapshot ds : dataSnapshot.getChildren()) {

                    Restaurant restaurant = ds.getValue(Restaurant.class);

                    if (restaurant != null && restaurant.getRid() != null) {
                        restaurants.add(restaurant);
                    }


                }

                restaurantCallbacks.onRestaurantCallback(restaurants);

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


    /**
     * A mehod to retrieve user info. from Firebase db
     * @param Uemail the email of the user
     * @param userCallbacks A instance of User model
     */
    public void retrieveUser(final String Uemail, final UserCallbacks userCallbacks){

        //db.child("User").orderByChild("uemail").equalTo(Uemail).

        db.child("User").orderByChild("uemail").equalTo(Uemail).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot ds : dataSnapshot.getChildren()) {

                    User user = ds.getValue(User.class);

                    //LOG TEST
                    Log.d("User-name", user.getUname());

                    userCallbacks.onUserCallback(user);

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}

