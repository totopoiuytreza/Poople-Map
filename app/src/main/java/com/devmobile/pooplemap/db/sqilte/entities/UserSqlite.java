package com.devmobile.pooplemap.db.sqilte.entities;

import java.math.BigInteger;

public class UserSqlite {
    private BigInteger Id;
    private String Username;
    private String Email;

    public UserSqlite(BigInteger id, String username, String email) {
        Id = id;
        Username = username;
        Email = email;
    }

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

}
