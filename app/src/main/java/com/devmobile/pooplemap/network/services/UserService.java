package com.devmobile.pooplemap.network.services;
import retrofit2.Call;

import com.devmobile.pooplemap.forms.UserForm;
import com.devmobile.pooplemap.models.*;
import com.devmobile.pooplemap.responses.UserResponse;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PATCH;

public interface UserService {

    @GET("/user/getUser")
    public Call<UserResponse> getUser();

    @PATCH("/user/patchUser")
    public Call<UserResponse> patchUser(@Body UserForm patch);

}
