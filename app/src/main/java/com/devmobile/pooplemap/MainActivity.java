package com.devmobile.pooplemap;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import com.devmobile.pooplemap.databinding.ActivityMainBinding;
import com.devmobile.pooplemap.fragments.HomeFragment;
import com.devmobile.pooplemap.fragments.ProfileFragment;
import com.devmobile.pooplemap.fragments.SettingsFragment;


public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        // Set default fragment
        replaceFragment(new HomeFragment());
        binding.bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.Home:
                    replaceFragment(new HomeFragment());
                    break;
                case R.id.Profile:
                    replaceFragment(new ProfileFragment());
                    break;
                case R.id.Settings:
                    replaceFragment(new SettingsFragment());
                    break;
            }
            return true;
        });


    }
    private void replaceFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_layout, fragment);
        transaction.commit();
    }
}



// Test Case Login client
        /*
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
        */