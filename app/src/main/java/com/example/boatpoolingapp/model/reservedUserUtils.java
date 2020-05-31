package com.example.boatpoolingapp.model;

import java.io.Serializable;

public class reservedUserUtils implements Serializable {

    String user_id;
    String name;
    String booking_status;

    public reservedUserUtils(String user_id, String name, String booking_status) {
        this.user_id = user_id;
        this.name = name;
        this.booking_status = booking_status;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBooking_status() {
        return booking_status;
    }

    public void setBooking_status(String booking_status) {
        this.booking_status = booking_status;
    }
}
