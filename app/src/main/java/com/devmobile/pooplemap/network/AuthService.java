package com.devmobile.pooplemap.network;

import com.devmobile.pooplemap.forms.*;
import com.devmobile.pooplemap.models.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;

public interface AuthService {
    @POST("/auth/loginUser")
    public Call<String> loginUser(@Body LoginForm loginForm);
    @POST("/auth/registerUser")
    public Call<Integer> registerUser(@Body RegisterForm registerForm);
    @GET("/auth/verifyUser/{token}")
    public Call<String> verifyUser();



}
