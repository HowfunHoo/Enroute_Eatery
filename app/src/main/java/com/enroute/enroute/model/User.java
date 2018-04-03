package com.enroute.enroute.model;

/**
 * Created by youranzhang on 2018-03-31.
 */
//datamode of user

public class User {

    public String uname;
    public String uphone;
    public String uemail;
//    public String upassword;
//    public String uid;

    //user String to present preference
    public String upreference;



    public User() {
    }

//    public User(String uid,String uname, String uphone, String upreference, String uemail, String upassword) {
//        this.uid=uid;
//        this.uname=uname;
//        this.uphone=uphone;
//        this.uemail=uemail;
//        this.upassword=upassword;
//        this.upreference=upreference;
//    }
    public User(String uemail,String uname, String uphone,String upreference) {

        this.uname=uname;
        this.uphone=uphone;
        this.upreference=upreference;
        this.uemail=uemail;


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


    public void setUemail(String uemail){
        this.uemail=uemail;
    }

    public void setUname(String uname){
        this.uname=uname;
    }

    public void setUphone(String uphone){
        this.uphone=uphone;
    }


    public void setUpreference(String upreference){
        this.upreference=upreference;
    }
}