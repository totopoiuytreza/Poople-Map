package com.devmobile.pooplemap;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

import com.devmobile.pooplemap.databinding.ActivityMainBinding;
import com.devmobile.pooplemap.fragments.HomeFragment;
import com.devmobile.pooplemap.fragments.ProfileFragment;
import com.devmobile.pooplemap.network.ApiClient;
import com.devmobile.pooplemap.activities.LoginActivity;

import dagger.hilt.android.AndroidEntryPoint;
import retrofit2.Retrofit;

@AndroidEntryPoint
public class MainActivity extends AppCompatActivity {

    private static Context appContext;
    private ActivityMainBinding binding;
    private FragmentManager fragmentManager;
    private Fragment currentFragment;
    private static final String FRAGMENT_TAG = "currentFragment";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        appContext = getApplicationContext();

        // Shared preferences
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String token = sharedPreferences.getString("authorizationToken", null);
        if (token != null) {
            // User is logged in
            onLogin(savedInstanceState);
        } else {
            // User is not logged in
            // Go to login activity
            startActivity(new Intent(MainActivity.this, LoginActivity.class));

        }
    }

    public void onLogin(Bundle savedInstanceState) {
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
    public static Context getContextOfApplication(){
        return appContext;
    }

}