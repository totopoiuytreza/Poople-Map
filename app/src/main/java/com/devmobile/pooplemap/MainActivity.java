package com.devmobile.pooplemap;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import com.devmobile.pooplemap.databinding.ActivityMainBinding;
import com.devmobile.pooplemap.fragments.HomeFragment;
import com.devmobile.pooplemap.fragments.ProfileFragment;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    private FragmentManager fragmentManager;
    private Fragment currentFragment;
    private static final String FRAGMENT_TAG = "currentFragment";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        fragmentManager = getSupportFragmentManager();
        // Default fragment or current fragment
        if (savedInstanceState == null) {
            // Set default fragment
            replaceFragment(new HomeFragment());
        } else {
            // Restore the retained fragment
            currentFragment = fragmentManager.findFragmentByTag(FRAGMENT_TAG);
        }
        binding.bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.Home:
                    replaceFragment(new HomeFragment());
                    break;
                case R.id.Profile:
                    replaceFragment(new ProfileFragment());
                    break;
            }
            return true;
        });


    }
    // Go to previous fragment when back button is pressed
    @Override
    public void onBackPressed() {
        // Check if there are fragments in the back stack
        super.onBackPressed();
        if (fragmentManager.getBackStackEntryCount() > 0) {
            // Pop the back stack to navigate to the previous fragment
            fragmentManager.popBackStack();
        } else {
            // No fragments in the back stack, go to the home fragment
            replaceFragment(new HomeFragment());
        }
    }
    // Replace the current fragment with a new fragment
    private void replaceFragment(Fragment fragment) {
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_layout, fragment)
                .addToBackStack(null)
                .commit();
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