package com.devmobile.pooplemap.forms;

public class RatingForm {
    private int rating;
    private String comment;

    public RatingForm(int rating, String comment){
        this.rating = rating;
        this.comment = comment;
    }

    public RatingForm(int rating){
        this.rating = rating;
    }

    public RatingForm(String comment){
        this.comment = comment;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
