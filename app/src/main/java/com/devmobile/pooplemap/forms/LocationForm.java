package com.devmobile.pooplemap.forms;

public class LocationForm {
    private String name;
    private float latitude;
    private float longitude;

    public LocationForm(String name, float latitude, float longitude){
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public LocationForm(String name){
        this.name = name;
    }

    public LocationForm(float latitude, float longitude){
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }
}
