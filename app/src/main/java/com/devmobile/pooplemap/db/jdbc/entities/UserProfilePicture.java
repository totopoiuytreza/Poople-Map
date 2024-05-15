package com.devmobile.pooplemap.db.jdbc.entities;

import java.math.BigInteger;

public class UserProfilePicture {
    private BigInteger id_user;
    private byte[] picture;
    private String description;

    public UserProfilePicture(BigInteger id_user, byte[] picture, String description) {
        this.id_user = id_user;
        this.picture = picture;
        this.description = description;
    }

    public BigInteger getIdUser() {
        return id_user;
    }

    public byte[] getPicture() {
        return picture;
    }

    public String getDescription() {
        return description;
    }

    public void setIdUser(BigInteger id_user) {
        this.id_user = id_user;
    }

    public void setPicture(byte[] picture) {
        this.picture = picture;
    }


    public void setDescription(String description) {
        this.description = description;
    }

}
