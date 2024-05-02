package com.devmobile.pooplemap.activities;

import static com.devmobile.pooplemap.MainActivity.getContextOfApplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.devmobile.pooplemap.MainActivity;
import com.devmobile.pooplemap.R;
import com.devmobile.pooplemap.db.jdbc.DatabaseHandlerImg;
import com.devmobile.pooplemap.db.jdbc.entities.UserProfilePicture;
import com.devmobile.pooplemap.db.sqilte.DatabaseHandlerSqlite;
import com.devmobile.pooplemap.db.sqilte.entities.ImagePictureSqlite;
import com.devmobile.pooplemap.db.sqilte.entities.UserSqlite;
import com.devmobile.pooplemap.forms.LoginForm;
import com.devmobile.pooplemap.network.services.AuthService;
import com.devmobile.pooplemap.network.services.UserService;
import com.devmobile.pooplemap.responses.TokenResponse;
import com.devmobile.pooplemap.responses.UserResponse;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.util.concurrent.CompletableFuture;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@AndroidEntryPoint
public class LoginActivity extends AppCompatActivity {
    @Inject DatabaseHandlerSqlite db;
    @Inject AuthService authService;
    @Inject UserService userService;
    DatabaseHandlerImg dbImg = new DatabaseHandlerImg();
    private EditText usernameEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private Button registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_test);

        // Initialize views
        usernameEditText = findViewById(R.id.username_input);
        passwordEditText = findViewById(R.id.password_input);
        loginButton = findViewById(R.id.login_button);
        registerButton = findViewById(R.id.register_button);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                loginUser(username, password);
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });


    }

    private void loginUser(String username, String password) {
        // Delete User and ImagePictureSqlite tables
        db.deleteUser();
        db.deleteImage();

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

                            System.out.println("FUCKING TOKEN: " + tokenResponse.getToken());
                            // Call User
                            callUser();
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

    public void callUser(){
        System.out.println("Call User");
        // Call for GetUser
        Call<UserResponse> call = userService.getUser();
        call.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                if (response.isSuccessful()) {
                    UserResponse userResponse = response.body();
                    if (userResponse != null) {
                        // Add the user to the database
                        UserSqlite user = new UserSqlite(userResponse.getId_user(), userResponse.getUsername(), userResponse.getEmail());
                        db.addUser(user);
                        System.out.println("User added to database" + user.getUsername());
                        addProfilePicture(user.getId());
                    }
                }
            }
            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                System.out.println("Error");
            }
        });
    }

    public void addProfilePicture(BigInteger userId){
        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 8)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
            CompletableFuture<UserProfilePicture> future = dbImg.getUserProfilePictureAsync(userId);
            future.thenAcceptAsync(userProfilePicture -> {
                if (userProfilePicture != null) {
                    // Save the image in the app directory
                    File file = new File(getExternalFilesDir(null), "profilePicture.jpg");
                    try {
                        FileOutputStream fos = new FileOutputStream(file);
                        fos.write(userProfilePicture.getPicture());
                        fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    // Save the image in the database
                    ImagePictureSqlite image = new ImagePictureSqlite(file.getPath(), "Description");
                    db.addImage(image);
                }
                navigateToMainActivity();
            });

        }
    }

    private void navigateToMainActivity() {
        startActivity(new Intent(this, MainActivity.class));
    }
}