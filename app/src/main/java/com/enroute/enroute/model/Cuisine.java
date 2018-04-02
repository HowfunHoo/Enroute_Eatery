package com.enroute.enroute.model;

public class Cuisine {

    public String cuisine_id;
    public String cuisine_name;

    public Cuisine(){

    }

    public Cuisine(String cuisine_id, String cuisine_name) {
        this.cuisine_id = cuisine_id;
        this.cuisine_name = cuisine_name;
    }

    public String getCuisine_id() {
        return cuisine_id;
    }

    public String getCuisine_name() {
        return cuisine_name;
    }

    public void setCuisine_id(String cuisine_id) {
        this.cuisine_id = cuisine_id;
    }

    public void setCuisine_name(String cuisine_name) {
        this.cuisine_name = cuisine_name;
    }
}
