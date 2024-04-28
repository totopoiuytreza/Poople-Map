package com.devmobile.pooplemap.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import com.devmobile.pooplemap.R;
import com.devmobile.pooplemap.db.sqilte.DatabaseHandler;
import com.devmobile.pooplemap.db.sqilte.entities.UserSqlite;
import com.devmobile.pooplemap.models.User;
import com.devmobile.pooplemap.network.services.UserService;
import com.devmobile.pooplemap.responses.UserResponse;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@AndroidEntryPoint
public class EditProfileFragment extends Fragment {

    @Inject UserService userService;
    @Inject DatabaseHandler db;
    public EditProfileFragment() {
        // Required empty public constructor
    }

    public static EditProfileFragment newInstance() {
        EditProfileFragment fragment = new EditProfileFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        UserSqlite user = db.getCurrentUser();
        TextView username = getView().findViewById(R.id.edit_username_input);
        TextView email = getView().findViewById(R.id.edit_email_input);

        username.setText(user.getUsername());
        email.setText(user.getEmail());

        AppCompatButton updateProfileButton = getView().findViewById(R.id.update_profile_button);
        updateProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User userToPatch = new User(null, username.getText().toString(), email.getText().toString(), null);
                callUpdateUser(userToPatch);
            }
        });

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_profile, container, false);
    }

    private void callUpdateUser(User user) {
        // Call the update user service
        Call<UserResponse> call = userService.patchUser(user);
        call.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                if (response.isSuccessful()) {
                    UserResponse userResponse = response.body();
                    if (userResponse != null) {
                        // Update the user in the database
                        TextView username = getView().findViewById(R.id.edit_username_input);
                        TextView email = getView().findViewById(R.id.edit_email_input);
                        UserSqlite userSqlite = new UserSqlite(null, username.getText().toString(), email.getText().toString());
                        db.updateUser(userSqlite);
                    }
                }
            }
            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                System.out.println("Error");
            }
        });
    }
}
