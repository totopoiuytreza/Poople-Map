package com.devmobile.pooplemap.activities;

import static com.devmobile.pooplemap.MainActivity.getContextOfApplication;

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
import com.devmobile.pooplemap.forms.LoginForm;
import com.devmobile.pooplemap.network.services.AuthService;
import com.devmobile.pooplemap.responses.TokenResponse;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@AndroidEntryPoint
public class LoginActivity extends AppCompatActivity {
    @Inject
    AuthService authService;
    private EditText usernameEditText;
    private EditText passwordEditText;
    private Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_test);

        // Initialize views
        usernameEditText = findViewById(R.id.username_input);
        passwordEditText = findViewById(R.id.password_input);
        loginButton = findViewById(R.id.login_button);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                loginUser(username, password);
            }
        });
    }

    private void loginUser(String username, String password) {
        LoginForm loginForm = new LoginForm(username, password);
        Call<TokenResponse> call = authService.loginUser(loginForm);
        call.enqueue(new Callback<TokenResponse>() {
            @Override
            public void onResponse(Call<TokenResponse> call, Response<TokenResponse> response) {
                if(response.isSuccessful()){
                    try {
                        TokenResponse tokenResponse = response.body();
                        if (tokenResponse != null) {
                            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContextOfApplication());
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("authorizationToken", tokenResponse.getToken());
                            editor.apply();
                            navigateToMainActivity();
                        }
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }else{
                    Toast.makeText(LoginActivity.this, "Wrong credentials", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<TokenResponse> call, Throwable t) {
                System.out.println("Login failed with error: " + t.getMessage());
            }
        });
    }

    private void navigateToMainActivity() {
        startActivity(new Intent(this, MainActivity.class));
    }
}