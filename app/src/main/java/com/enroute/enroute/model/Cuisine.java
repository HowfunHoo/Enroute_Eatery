package com.enroute.enroute.model;

/**
 * This class is the model of cuisine info.
 */
public class Cuisine {

    public int cuisine_id;
    public String cuisine_name;

    public Cuisine(){

    }

    public Cuisine(int cuisine_id, String cuisine_name) {
        this.cuisine_id = cuisine_id;
        this.cuisine_name = cuisine_name;
    }

    public int getCuisine_id() {
        return cuisine_id;
    }

    public String getCuisine_name() {
        return cuisine_name;
    }

    public void setCuisine_id(int cuisine_id) {
        this.cuisine_id = cuisine_id;
    }

    public void setCuisine_name(String cuisine_name) {
        this.cuisine_name = cuisine_name;
    }
}
