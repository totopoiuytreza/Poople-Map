package com.devmobile.pooplemap.network;
import retrofit2.Call;
import com.devmobile.pooplemap.models.*;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.Path;
import retrofit2.http.Query;
public interface UserService {

    @GET("/user/getUser")
    public Call<User> getUser();

    @PATCH("/user/patchUser")
    public Call<User> patchUser(@Body User patch);

}
