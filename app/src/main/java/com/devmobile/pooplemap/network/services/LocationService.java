package com.devmobile.pooplemap.network.services;

import com.devmobile.pooplemap.models.Location;
import com.devmobile.pooplemap.forms.*;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface LocationService {
    @POST("/location/addLocation")
    public Call<Location> addLocation(@Body LocationForm location);
    @GET("/location/getLocations")
    public Call<List<Location>> getLocations();
    @GET("/location/getLocation/{id}")
    public Call<Location> getLocation(@Path("id") int locationId);
    @PATCH("/location/patchLocation/{id}")
    public Call<Location> patchLocation(@Body LocationForm patchLocation);
}
