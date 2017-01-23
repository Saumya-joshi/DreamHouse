package com.example.pri2si17.dreamhouse;

/**
 * Created by Saumya Joshi on 12/6/2016.
 */

public class Friend {

    String name;
    String friend_id;
    double latitude;
    double longitude;

    public Friend(String name, String friend_id, double latitude, double longitude) {
        this.name = name;
        this.friend_id = friend_id;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getFriendName() {
        return name;
    }

    public String getFriend_id() {
        return friend_id;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }
}
