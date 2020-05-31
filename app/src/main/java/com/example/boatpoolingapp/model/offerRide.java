package com.example.boatpoolingapp.model;

public class offerRide {
    String User_id;
    String dearture_city;
    String departure_time;
    String Ride_type;
    String seats;

    public offerRide(String user_id, String dearture_city, String departure_time, String ride_type, String seats) {
        User_id = user_id;
        this.dearture_city = dearture_city;
        this.departure_time = departure_time;
        Ride_type = ride_type;
        this.seats = seats;
    }

    public String getUser_id() {
        return User_id;
    }

    public void setUser_id(String user_id) {
        User_id = user_id;
    }

    public String getDearture_city() {
        return dearture_city;
    }

    public void setDearture_city(String dearture_city) {
        this.dearture_city = dearture_city;
    }

    public String getDeparture_time() {
        return departure_time;
    }

    public void setDeparture_time(String departure_time) {
        this.departure_time = departure_time;
    }

    public String getRide_type() {
        return Ride_type;
    }

    public void setRide_type(String ride_type) {
        Ride_type = ride_type;
    }

    public String getSeats() {
        return seats;
    }

    public void setSeats(String seats) {
        this.seats = seats;
    }
}
