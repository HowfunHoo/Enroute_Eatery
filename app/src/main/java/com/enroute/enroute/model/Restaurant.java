package com.enroute.enroute.model;

/**
 * This class is the model of restaurant info.
 */
public class Restaurant {

    public String rid;
    public String rname;
    public String rpicurl;
    public String raddress;
    public String rcity;
    public double rlat;
    public double rlng;
    public String raveragecost;
    public boolean rdalcard;
    public String rrate;
    public String rcuisines;
    public String rphone;
    public boolean rhasOffer;
    public String roffer;


    public Restaurant() {
    }

    public Restaurant(String rid, String rname, String rpicurl, String raddress, String rcity,
                      double rlat, double rlng, String raveragecost, boolean rdalcard, String rrate,
                      String rcuisines, String rphone, boolean rhasOffer, String roffer) {
        this.rid = rid;
        this.rname = rname;
        this.rpicurl = rpicurl;
        this.raddress = raddress;
        this.rcity = rcity;
        this.rlat = rlat;
        this.rlng = rlng;
        this.raveragecost = raveragecost;
        this.rdalcard = rdalcard;
        this.rrate = rrate;
        this.rcuisines = rcuisines;
        this.rphone = rphone;
        this.rhasOffer = rhasOffer;
        this.roffer = roffer;
    }

    public Restaurant(String rid, String rname, String raddress, String rcity, String raveragecost,
                      boolean rdalcard, String rcuisines, String rphone) {
        this.rid = rid;
        this.rname = rname;
        this.raddress = raddress;
        this.rcity = rcity;
        this.raveragecost = raveragecost;
        this.rdalcard = rdalcard;
        this.rcuisines = rcuisines;
        this.rphone = rphone;

        //Fake data for TEST
        this. rlat = 44.636581;
        this. rlng = -63.591656;
        this. rrate = "4.5";
        this. rhasOffer = true;
        this. roffer = "20% off for all meals";
        this. rpicurl = "https://www.zomato.com/new-york-city/otto-enoteca-pizzeria-greenwich-village/photos#tabtop";
    }

    public String getRid() {
        return rid;
    }

    public String getRname() {
        return rname;
    }

    public String getRpicurl() {
        return rpicurl;
    }

    public String getRaddress() {
        return raddress;
    }

    public String getRcity() {
        return rcity;
    }

    public double getRlat() {
        return rlat;
    }

    public double getRlng() {
        return rlng;
    }

    public String getRaveragecost() {
        return raveragecost;
    }

    public boolean getRdalcard() {
        return rdalcard;
    }

    public String getRrate() {
        return rrate;
    }

    public String getRcuisines() {
        return rcuisines;
    }

    public String getRphone() {
        return rphone;
    }

    public boolean isRhasOffer() {
        return rhasOffer;
    }

    public String getRoffer() {
        return roffer;
    }

    public void setRid(String rid) {
        this.rid = rid;
    }

    public void setRname(String rname) {
        this.rname = rname;
    }

    public void setRpicurl(String rpicurl) {
        this.rpicurl = rpicurl;
    }

    public void setRaddress(String raddress) {
        this.raddress = raddress;
    }

    public void setRcity(String rcity) {
        this.rcity = rcity;
    }

    public void setRlat(double rlat) {
        this.rlat = rlat;
    }

    public void setRlng(double rlng) {
        this.rlng = rlng;
    }

    public void setRaveragecost(String raveragecost) {
        this.raveragecost = raveragecost;
    }

    public void setRdalcard(boolean rdalcard) {
        this.rdalcard = rdalcard;
    }

    public void setRrate(String rrate) {
        this.rrate = rrate;
    }

    public void setRcuisines(String rcuisines) {
        this.rcuisines = rcuisines;
    }

    public void setRphone(String rphone) {
        this.rphone = rphone;
    }

    public void setRhasOffer(boolean rhasOffer) {
        this.rhasOffer = rhasOffer;
    }

    public void setRoffer(String roffer) {
        this.roffer = roffer;
    }
}
