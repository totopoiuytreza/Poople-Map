package com.devmobile.pooplemap.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.devmobile.pooplemap.MainActivity;
import com.devmobile.pooplemap.R;
import com.devmobile.pooplemap.forms.RegisterForm;
import com.devmobile.pooplemap.network.services.AuthService;
import com.devmobile.pooplemap.responses.TokenResponse;


import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@AndroidEntryPoint
public class RegisterActivity extends AppCompatActivity{
    @Inject AuthService authService;

    public EditText usernameEditText;
    public EditText passwordEditText;
    public EditText emailEditText;
    public Button registerButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Initialize views
        usernameEditText = findViewById(R.id.register_username_input);
        passwordEditText = findViewById(R.id.register_password_input);
        emailEditText = findViewById(R.id.register_email_input);

        registerButton = findViewById(R.id.confirm_register_button);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                String email = emailEditText.getText().toString();
                registerUser(username, password, email);
            }
        });
    }

    public void registerUser(String username, String password, String email) {
        Call<Integer> call = authService.registerUser(new RegisterForm(email, username, password));
        call.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                if (response.isSuccessful()) {
                    // User registered
                    Toast.makeText(RegisterActivity.this, "User registered", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                } else {
                    // User not registered
                    Toast.makeText(RegisterActivity.this, "User not registered", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                // Error
                Toast.makeText(RegisterActivity.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
