package com.devmobile.pooplemap.fragments;

import static com.devmobile.pooplemap.MainActivity.getContextOfApplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;

import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.devmobile.pooplemap.R;
import com.devmobile.pooplemap.activities.LoginActivity;
import com.devmobile.pooplemap.db.sqilte.DatabaseHandler;
import com.devmobile.pooplemap.models.User;
import com.devmobile.pooplemap.network.services.UserService;
import com.devmobile.pooplemap.responses.UserResponse;

import org.w3c.dom.Text;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@AndroidEntryPoint
public class ProfileFragment extends Fragment {

    @Inject UserService userService;
    @Inject DatabaseHandler db;
    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Call for GetUser
        Call<UserResponse> call = userService.getUser();
        call.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                if (response.isSuccessful()) {
                    UserResponse userResponse = response.body();
                    if (userResponse != null) {
                        TextView textUsername = getView().findViewById(R.id.username);
                        textUsername.setText(userResponse.getUsername());
                        TextView textEmail = getView().findViewById(R.id.user_email);
                        textEmail.setText(userResponse.getEmail());

                        // Add the user to the database
                        User user = new User();
                        user.setId(userResponse.getId_user());
                        user.setUsername(userResponse.getUsername());
                        user.setEmail(userResponse.getEmail());
                        db.addUser(user);
                    }
                }
            }
            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                System.out.println("Error");
            }
        });

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        // Add buttons and actions for the profile fragment
        AppCompatButton btnEditProfile = view.findViewById(R.id.edit_profile_button);
        RelativeLayout btnAboutUs = view.findViewById(R.id.about_us_layout);
        RelativeLayout btnContactUs = view.findViewById(R.id.contact_us_layout);
        RelativeLayout btnLogout = view.findViewById(R.id.log_out_layout);

        btnEditProfile.setOnClickListener(this::onClick);
        btnAboutUs.setOnClickListener(this::onClick);
        btnContactUs.setOnClickListener(this::onClick);
        btnLogout.setOnClickListener(this::onClick);

        // Add switch actions
        SwitchCompat switchNotification = view.findViewById(R.id.notification_switch);

        switchNotification.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                TextView textNotfificationStatus = view.findViewById(R.id.notfication_status);
                textNotfificationStatus.setText(R.string.on);
            } else {
                TextView textNotfificationStatus = view.findViewById(R.id.notfication_status);
                textNotfificationStatus.setText(R.string.off);
            }
        });

        SwitchCompat switchLanguage = view.findViewById(R.id.language_switch);
        switchLanguage.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                TextView textLanguage = view.findViewById(R.id.chosen_language);
                textLanguage.setText(R.string.english);
            } else {
                TextView textLanguage = view.findViewById(R.id.chosen_language);
                textLanguage.setText(R.string.french);
            }
        });
        return view;
    }

    // Add the actions for the buttons
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.edit_profile_button:
                // Open the edit profile activity
                onClickAnimation(view);
                System.out.println("Edit profile");
                break;
            case R.id.about_us_layout:
                // Open the about us activity
                // Print the about us information
                onClickAnimation(view);
                System.out.println("About us");
                break;
            case R.id.contact_us_layout:
                // Open the contact us activity
                onClickAnimation(view);

                System.out.println("Contact us");
                break;
            case R.id.log_out_layout:
                // Log out the user
                onClickAnimation(view);
                logOut();
                break;
        }
    }

    public void logOut() {
        // Remove the authorization token from the shared preferences
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContextOfApplication());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("authorizationToken");
        editor.apply();
        // Go to the login activity
        startActivity(new Intent(getContextOfApplication(), LoginActivity.class));
    }

    public void onClickAnimation(View view) {
        view.animate()
                .scaleX(0.9f)
                .scaleY(0.9f)
                .setDuration(200)
                .withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        // Restore the original size of the view after the animation completes
                        view.animate()
                                .scaleX(1.0f)
                                .scaleY(1.0f)
                                .setDuration(200)
                                .start();
                    }
                })
                .start();
    }

}