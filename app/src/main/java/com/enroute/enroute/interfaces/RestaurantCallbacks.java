package com.enroute.enroute.interfaces;

import com.enroute.enroute.model.Restaurant;

import java.util.ArrayList;

/**
 * This interface callbacks the a Restaurant instance from searching restaurant in Firebase
 */
public interface RestaurantCallbacks {

    void onRestaurantCallback(ArrayList<Restaurant> restaurants);

}
