package com.devmobile.pooplemap.network.services;

import com.devmobile.pooplemap.forms.*;
import com.devmobile.pooplemap.responses.TokenResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface AuthService {
    @POST("/auth/loginUser")
    public Call<TokenResponse> loginUser(@Body LoginForm loginForm);
    @POST("/auth/registerUser")
    public Call<Integer> registerUser(@Body RegisterForm registerForm);
    @GET("/auth/verifyUser/{token}")
    public Call<String> verifyUser();



}
