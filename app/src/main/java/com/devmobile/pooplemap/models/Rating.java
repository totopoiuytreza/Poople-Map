package com.devmobile.pooplemap.models;

import java.math.BigInteger;

public class Rating {
    private int Id;
    private int Rating_Score;
    private String Comments;
    private int Id_Location;

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public int getRating_Score() {
        return Rating_Score;
    }

    public void setRating_Score(int rating_Score) {
        Rating_Score = rating_Score;
    }

    public String getComments() {
        return Comments;
    }

    public void setComments(String comments) {
        Comments = comments;
    }

    public int getId_Location() {
        return Id_Location;
    }

    public void setId_Location(int id_Location) {
        Id_Location = id_Location;
    }
}
