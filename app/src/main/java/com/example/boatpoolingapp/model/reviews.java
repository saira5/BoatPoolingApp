package com.example.boatpoolingapp.model;

public class reviews {
    String _id,rider_User_id,rider_name,review,ride_id,driver_id,driver_name;

    public reviews(String _id, String rider_User_id, String rider_name, String review, String ride_id, String driver_id,String driver_name) {
        this._id = _id;
        this.rider_User_id = rider_User_id;
        this.rider_name = rider_name;
        this.review = review;
        this.ride_id = ride_id;
        this.driver_id = driver_id;
        this.driver_name=driver_name;
    }

    public String getDriver_name() {
        return driver_name;
    }

    public void setDriver_name(String driver_name) {
        this.driver_name = driver_name;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getRider_User_id() {
        return rider_User_id;
    }

    public void setRider_User_id(String rider_User_id) {
        this.rider_User_id = rider_User_id;
    }

    public String getRider_name() {
        return rider_name;
    }

    public void setRider_name(String rider_name) {
        this.rider_name = rider_name;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public String getRide_id() {
        return ride_id;
    }

    public void setRide_id(String ride_id) {
        this.ride_id = ride_id;
    }

    public String getDriver_id() {
        return driver_id;
    }

    public void setDriver_id(String driver_id) {
        this.driver_id = driver_id;
    }
}
