package com.devmobile.pooplemap.models;

import java.math.BigInteger;
import java.util.Date;

public class Session {
    private int Id;
    private String Token;
    private Date ValidUntil;
    private BigInteger Id_User;

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getToken() {
        return Token;
    }

    public void setToken(String token) {
        Token = token;
    }

    public Date getValidUntil() {
        return ValidUntil;
    }

    public void setValidUntil(Date validUntil) {
        ValidUntil = validUntil;
    }

    public BigInteger getId_User() {
        return Id_User;
    }

    public void setId_User(BigInteger id_User) {
        Id_User = id_User;
    }


}
