package com.devmobile.pooplemap.models;

import java.math.BigInteger;

public class Location {
    private int Id;
    private String Name;
    private float Latitude;
    private float Longitude;
    private BigInteger Id_User;

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public Float getLatitude() {
        return Latitude;
    }

    public void setLatitude(Float latitude) {
        Latitude = latitude;
    }

    public Float getLongitude() {
        return Longitude;
    }

    public void setLongitude(Float longitude) {
        Longitude = longitude;
    }

    public BigInteger getId_User() {
        return Id_User;
    }

    public void setId_User(BigInteger id_User) {
        Id_User = id_User;
    }
}
