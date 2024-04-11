package com.devmobile.pooplemap;

import androidx.appcompat.app.AppCompatActivity;

import com.devmobile.pooplemap.forms.LoginForm;
import com.devmobile.pooplemap.network.ApiClient;
import com.devmobile.pooplemap.network.AuthService;
import com.devmobile.pooplemap.models.*;
import com.devmobile.pooplemap.network.UserService;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_test);
        Retrofit retrofit = ApiClient.getApiClient();
        AuthService authService = retrofit.create(AuthService.class);

        LoginForm loginForm = new LoginForm("totopoiuytreza", "toto");

        Call<String> call = authService.loginUser(loginForm);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    String token = response.body();
                    // Save token in shared preferences
                    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("authorizationToken", token);
                    editor.apply();


                } else {
                    // Handle error response
                }
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                // Handle network failure
            }
        });
    }
}