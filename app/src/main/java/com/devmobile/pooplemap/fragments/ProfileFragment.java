package com.devmobile.pooplemap.fragments;

import static com.devmobile.pooplemap.MainActivity.getContextOfApplication;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.widget.AlertDialogLayout;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.devmobile.pooplemap.MainActivity;
import android.Manifest;
import android.content.pm.PackageManager;
import androidx.core.content.ContextCompat;
import com.devmobile.pooplemap.R;
import com.devmobile.pooplemap.activities.LoginActivity;
import com.devmobile.pooplemap.db.sqilte.DatabaseHandler;
import com.devmobile.pooplemap.models.User;
import com.devmobile.pooplemap.network.services.UserService;
import com.devmobile.pooplemap.responses.UserResponse;

import org.w3c.dom.Text;

import java.util.Locale;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@AndroidEntryPoint
public class ProfileFragment extends Fragment {

    @Inject UserService userService;
    @Inject DatabaseHandler db;
    private static final int CAMERA_REQUEST = 100;
    private static final int STORAGE_REQUEST = 200;
    private static final int IMAGEPICK_GALLERY_REQUEST = 300;
    private static final int IMAGE_PICKCAMERA_REQUEST = 400;

    String cameraPermission[];
    String storagePermission[];
    Uri imageuri;
    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        cameraPermission = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermission = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
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
        ImageView btnEditProfileIcon = view.findViewById(R.id.edit_profile_picture_button);

        btnEditProfile.setOnClickListener(this::onClick);
        btnAboutUs.setOnClickListener(this::onClick);
        btnContactUs.setOnClickListener(this::onClick);
        btnLogout.setOnClickListener(this::onClick);
        btnEditProfileIcon.setOnClickListener(this::onClick);

        // Add switch actions
        SwitchCompat switchNotification = view.findViewById(R.id.notification_switch);

        switchNotification.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                changeNotificationStatus(view, true);
            } else {
                changeNotificationStatus(view, false);
            }
        });

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContextOfApplication());
        boolean switchState = sharedPreferences.getBoolean("languageSwitchState", false);

        SwitchCompat switchLanguage = view.findViewById(R.id.language_switch);
        switchLanguage.setChecked(switchState);
        if(switchState){
            TextView textLanguage = view.findViewById(R.id.chosen_language);
            textLanguage.setText(R.string.english);
        }else{
            TextView textLanguage = view.findViewById(R.id.chosen_language);
            textLanguage.setText(R.string.french);
        }
        switchLanguage.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                // Change the language to base language
                changeLanguage(view, "");

            } else {
                // Change the language to French
                changeLanguage(view, "fr");
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

            case R.id.edit_profile_picture_button:
                // Open the edit profile activity
                onClickAnimation(view);
                showImagePicDialog();
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

    public void showImagePicDialog() {
        String options[] = {"Camera", "Gallery"};
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        builder.setTitle("Pick Image From");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Handle the actions for the camera and gallery options
                if (which == 0) {
                    // Camera option selected
                    //openCamera();
                } else if (which == 1) {
                    // Gallery option selected
                    //openGallery();
                }
            }
        });
        builder.show();
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

    public void changeNotificationStatus(View view, boolean isChecked) {
        if (isChecked) {
            TextView textNotfificationStatus = view.findViewById(R.id.notfication_status);
            textNotfificationStatus.setText(R.string.on);
        } else {
            TextView textNotfificationStatus = view.findViewById(R.id.notfication_status);
            textNotfificationStatus.setText(R.string.off);
        }
    }

    public void changeLanguage(View view, String language) {


        // Change the locale to French
        Locale locale = new Locale(language);
        Locale.setDefault(locale);

        // Create a configuration object and update the app's resources
        Configuration config = new Configuration();
        config.setLocale(locale);
        getResources().updateConfiguration(config, getResources().getDisplayMetrics());

        // Start the activity to apply the new language and store the switch state in SharedPreferences
        SwitchCompat switchLanguage = view.findViewById(R.id.language_switch);
        boolean switchState = switchLanguage.isChecked();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContextOfApplication());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("languageSwitchState", switchState);
        editor.apply();

        startActivity(new Intent(getContextOfApplication(), MainActivity.class).putExtra("switchState", switchState));
    }

    // checking storage permission ,if given then we can add something in our storage
    private Boolean checkStoragePermission() {
        boolean result = ContextCompat.checkSelfPermission(getContextOfApplication(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return result;
    }

    // requesting for storage permission
    private void requestStoragePermission() {
        requestPermissions(storagePermission, STORAGE_REQUEST);
    }

    // checking camera permission ,if given then we can click image using our camera
    private Boolean checkCameraPermission() {
        boolean result = ContextCompat.checkSelfPermission(getContextOfApplication(), Manifest.permission.CAMERA) == (PackageManager.PERMISSION_GRANTED);
        boolean result1 = ContextCompat.checkSelfPermission(getContextOfApplication(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return result && result1;
    }

    // requesting for camera permission if not given
    private void requestCameraPermission() {
        requestPermissions(cameraPermission, CAMERA_REQUEST);
    }

    // Here we will click a photo and then go to startactivityforresult for updating data
    private void pickFromCamera() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.Images.Media.TITLE, "Temp_pic");
        contentValues.put(MediaStore.Images.Media.DESCRIPTION, "Temp Description");
        imageuri = getContextOfApplication().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
        Intent camerIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        camerIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageuri);
        startActivityForResult(camerIntent, IMAGE_PICKCAMERA_REQUEST);
    }

    // We will select an image from gallery
    private void pickFromGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, IMAGEPICK_GALLERY_REQUEST);
    }

}