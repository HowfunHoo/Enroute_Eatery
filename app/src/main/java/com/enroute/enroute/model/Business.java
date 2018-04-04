package com.enroute.enroute.model;

/**
 * This class is the model of restaurant info.
 */
public class Business {

    public String bname;
    public String bphone;
    public String bemail;

    public Business() {
    }

    public Business(String bemail, String bname, String bphone) {

        this.bname = bname;
        this.bphone = bphone;
        this.bemail = bemail;

    }

    public String getBemail() {
        return bemail;
    }

    public String getBname() {
        return bname;
    }

    public String getBphone() {
        return bphone;
    }

    public void setBname(String bname) {
        this.bname = bname;
    }

    public void setBphone(String bphone) {
        this.bphone = bphone;
    }

    public void setBemail(String bemail) {
        this.bemail = bemail;
    }

}