package com.devmobile.pooplemap.fragments;

import static com.devmobile.pooplemap.MainActivity.getContextOfApplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import com.devmobile.pooplemap.MainActivity;
import com.devmobile.pooplemap.R;
import com.devmobile.pooplemap.db.sqilte.DatabaseHandlerSqlite;
import com.devmobile.pooplemap.db.sqilte.entities.UserSqlite;
import com.devmobile.pooplemap.forms.UserForm;
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
    @Inject
    DatabaseHandlerSqlite db;
    public EditProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_edit_profile, container, false);


        UserSqlite user = db.getCurrentUser();
        TextView username = view.findViewById(R.id.edit_username_input);
        TextView email = view.findViewById(R.id.edit_email_input);

        username.setText(user.getUsername());
        email.setText(user.getEmail());


        AppCompatButton updateProfileButton = view.findViewById(R.id.update_profile_button);
        updateProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserForm userToPatch = new UserForm(username.getText().toString(), email.getText().toString());
                callUpdateUser(userToPatch);
                // Go back to the profile fragment
                Intent intent = new Intent(getContextOfApplication(), MainActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }

    private void callUpdateUser(UserForm user) {
        // Call the update user service
        Call<UserResponse> call = userService.patchUser(user);
        call.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                if (response.isSuccessful()) {
                    UserResponse userResponse = response.body();
                    if (userResponse != null) {
                        String username = userResponse.getUsername();
                        String email = userResponse.getEmail();
                        // Update the user in the database
                        UserSqlite userSqlite = new UserSqlite(null, username, email);
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
