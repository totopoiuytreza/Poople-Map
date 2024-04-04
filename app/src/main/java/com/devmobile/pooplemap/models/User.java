package com.devmobile.pooplemap.models;

import java.math.BigInteger;
import java.util.Date;

public class User {
    private BigInteger Id;
    private String Username;
    private String Email;
    private String Password;

    public BigInteger getId() {
        return Id;
    }

    public void setId(BigInteger id) {
        Id = id;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }
}
