package com.enroute.enroute;

/**
 * Programmer: Nirav Jadeja
 *
 * This class is for storing the data on firebase database
 * it stores user name, email and restaurant type from the user
 *
 * Reference: CSCI 3130: Software Engineering 2018 - Assignment 3 by Julaino Franz
 */

import com.google.firebase.database.Exclude;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Contact implements Serializable {

    public  String name;
    public  String email;
    public  String rType;

    public Contact() {
        // Default constructor required for calls to DataSnapshot.getValue
    }

    public Contact(String name, String email, String rType){
        this.name = name;
        this.email = email;
        this.rType = rType;
    }

    @Exclude
    public Map<String, Object> toMap(){
        HashMap<String, Object> result = new HashMap<>();
        result.put("name", name);
        result.put("email", email);
        result.put("rType", rType);
        return result;
    }
}
