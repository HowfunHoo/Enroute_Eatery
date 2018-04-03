package com.enroute.enroute.model;

/**
 * Created by youranzhang on 2018-03-31.
 */
//datamode of user

public class User {

    public String uname;
    public String uphone;
    public String uemail;
    public String upreference;

//    public String upassword;
//    public String uid;

    //user String to present preference
//    public String upreference;


    public User() {
    }


    public User(String uname, String uphone, String uemail, String upreference) {
        this.uname = uname;
        this.uphone = uphone;
        this.uemail = uemail;
        this.upreference = upreference;
    }

    public User(String uname, String uphone) {
        this.uname = uname;
        this.uphone = uphone;
    }

    public String getUname() {
        return uname;
    }

    public String getUphone() {
        return uphone;
    }

    public String getUemail() {
        return uemail;
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