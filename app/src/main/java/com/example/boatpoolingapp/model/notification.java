package com.example.boatpoolingapp.model;

public class notification {

   String User_id,description,_id,Time;

    public notification(String user_id, String description, String _id,String Time) {
        User_id = user_id;
        this.description = description;
        this._id = _id;
        this.Time=Time;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getUser_id() {
        return User_id;
    }

    public void setUser_id(String user_id) {
        User_id = user_id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
