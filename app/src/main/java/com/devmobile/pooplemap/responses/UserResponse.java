package com.devmobile.pooplemap.responses;

import java.math.BigInteger;

public class UserResponse {
    private BigInteger id_user;
    private String username;
    private String email;

    public UserResponse(BigInteger id_user, String username, String email) {
        this.id_user = id_user;
        this.username = username;
        this.email = email;
    }

    public BigInteger getId_user() {
        return id_user;
    }

    public void setId_user(BigInteger id_user) {
        this.id_user = id_user;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


}
