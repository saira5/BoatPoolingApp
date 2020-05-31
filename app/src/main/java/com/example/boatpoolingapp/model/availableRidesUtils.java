package com.example.boatpoolingapp.model;

public class availableRidesUtils {
    String _id;
    String reserved_users;
    String user_id;
    String dearture_city;
    String ride_type;
    String seats;
    String departure_time;
    String Name;
    String booking_status;

    public availableRidesUtils(String _id, String user_id, String dearture_city, String seats, String departure_time,String Name) {
        this._id = _id;
        this.user_id = user_id;
        this.dearture_city = dearture_city;
        this.seats = seats;
        this.departure_time = departure_time;
        this.Name=Name;

    }

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }



    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getReserved_users() {
        return reserved_users;
    }

    public void setReserved_users(String reserved_users) {
        this.reserved_users = reserved_users;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getDearture_city() {
        return dearture_city;
    }

    public void setDearture_city(String dearture_city) {
        this.dearture_city = dearture_city;
    }

    public String getRide_type() {
        return ride_type;
    }

    public void setRide_type(String ride_type) {
        this.ride_type = ride_type;
    }

    public String getSeats() {
        return seats;
    }

    public void setSeats(String seats) {
        this.seats = seats;
    }

    public String getDeparture_time() {
        return departure_time;
    }

    public void setDeparture_time(String departure_time) {
        this.departure_time = departure_time;
    }


}
