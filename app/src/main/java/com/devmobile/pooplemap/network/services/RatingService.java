package com.devmobile.pooplemap.network.services;

import com.devmobile.pooplemap.models.Rating;
import com.devmobile.pooplemap.forms.*;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface RatingService {
    @POST("/rating/addRating/{idLocation}")
    public Call<Rating> addLocation(@Body RatingForm rating);
    @GET("/rating/getRating/{id}")
    public Call<Rating> getLocation(@Path("id") int idRating);
    @PATCH("/rating/patchRating/{id}")
    public Call<Rating> patchRating(@Body RatingForm patchRating);
}
