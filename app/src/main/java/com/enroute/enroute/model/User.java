package com.enroute.enroute.model;

/**
 * This class is the model of user info.
 */
public class User {

    public String uname;
    public String uphone;
    public String uemail;
    public String upreference;


    public User() {
    }

    public User(String uemail,String uname, String uphone,String upreference) {

        this.uname = uname;
        this.uphone = uphone;
        this.upreference = upreference;
        this.uemail = uemail;

    }

    public String getUemail() {
        return uemail;
    }

    public String getUname() {
        return uname;
    }

    public String getUphone() {
        return uphone;
    }

    public String getUpreference() {
        return upreference;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public void setUphone(String uphone) {
        this.uphone = uphone;
    }

    public void setUemail(String uemail) {
        this.uemail = uemail;
    }

    public void setUpreference(String upreference) {
        this.upreference = upreference;
    }
}