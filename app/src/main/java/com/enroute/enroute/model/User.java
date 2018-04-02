package com.enroute.enroute.model;

/**
 * Created by youranzhang on 2018-03-31.
 */
//datamode of user

public class User {

    public String uname;
    public String uphone;
    public String uemail;
    public String upassword;
    public String uid;

    //user String to present preference
    public String upreference;



    public User() {
    }

    public User(String uid,String uname, String uphone, String upreference, String uemail, String upassword) {
        this.uid=uid;
        this.uname=uname;
        this.uphone=uphone;
        this.uemail=uemail;
        this.upassword=upassword;
        this.upreference=upreference;
    }

    public String getUid() {
        return uid;
    }

    public String getUname() {
        return uname;
    }

    public String getUphone() {
        return uname;
    }

    public String getUemail() {
        return uname;
    }

    public String getUpassword() {
        return upassword;
    }

    public String getUpreference() {
        return upreference;
    }

    public void setUid(String uid){
        this.uid=uid;
    }

    public void setUname(String uname){
        this.uname=uname;
    }

    public void setUphone(String uphone){
        this.uphone=uphone;
    }

    public void setUemail(String uemail){
        this.uemail=uemail;
    }

    public void setUpassword(String upassword){
        this.upassword=upassword;
    }

    public void setUpreference(String upreference){
        this.upreference=upreference;
    }
}