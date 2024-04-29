package com.devmobile.pooplemap.fragments;

import static com.devmobile.pooplemap.MainActivity.getContextOfApplication;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;


import android.preference.PreferenceManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import android.Manifest;
import android.widget.Toast;

import com.devmobile.pooplemap.MainActivity;
import com.devmobile.pooplemap.R;
import com.devmobile.pooplemap.activities.CameraActivity;
import com.devmobile.pooplemap.activities.LoginActivity;
import com.devmobile.pooplemap.db.sqilte.DatabaseHandler;
import com.devmobile.pooplemap.db.sqilte.entities.ImagePictureSqlite;
import com.devmobile.pooplemap.db.sqilte.entities.UserSqlite;
import com.devmobile.pooplemap.models.User;
import com.devmobile.pooplemap.network.services.UserService;
import com.devmobile.pooplemap.responses.UserResponse;
import com.devmobile.pooplemap.utils.LanguageUtils;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@AndroidEntryPoint
public class ProfileFragment extends Fragment {

    @Inject UserService userService;
    @Inject DatabaseHandler db;
    private ActivityResultLauncher<String> requestCameraPermissionLauncher;
    private ActivityResultLauncher<String> requestGalleryPermissionLauncher;
    private ActivityResultLauncher<PickVisualMediaRequest> imagePickerLauncher;


    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        // Request camera permission
        requestCameraPermissionLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestPermission(),
                isGranted -> {
                    if (isGranted) {
                        // Permission has been granted. Start camera preview Activity.
                        Toast.makeText(requireActivity(), "Camera permission was granted. Starting preview.", Toast.LENGTH_SHORT).show();
                        startCamera();
                    } else {
                        // Permission request was denied.
                        Toast.makeText(requireActivity(), "Camera permission request was denied.", Toast.LENGTH_SHORT).show();
                    }
                });
        requestGalleryPermissionLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestPermission(),
                isGranted -> {
                    if (isGranted) {
                        // Permission has been granted. Start camera preview Activity.
                        Toast.makeText(requireActivity(), "Gallery permission was granted. Starting gallery.", Toast.LENGTH_SHORT).show();
                        startGallery();
                    } else {
                        // Permission request was denied.
                        Toast.makeText(requireActivity(), "Gallery permission request was denied.", Toast.LENGTH_SHORT).show();
                    }
                });

        imagePickerLauncher =
        registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {
            // Callback is invoked after the user selects a media item or closes the
            if (uri != null) {
                // Save the image in app storage
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(requireActivity().getContentResolver(), uri);
                    File imageFile = new File(requireActivity().getFilesDir(), "profile_picture.jpg");
                    FileOutputStream out = new FileOutputStream(imageFile);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
                    out.flush();
                    out.close();

                    // Delete the previous database image
                    db.deleteImage();

                    // Save the image path in the database
                    ImagePictureSqlite image = new ImagePictureSqlite(0, imageFile.getAbsolutePath(), "Profile picture");
                    db.addImage(image);

                    // Set the profile picture
                    ImageView profilePicture = requireView().findViewById(R.id.profile_picture);
                    profilePicture.setImageURI(uri);

                } catch (IOException e) {
                    e.printStackTrace();
                }

            } else {
                Toast.makeText(requireActivity(), "No media selected", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        // User information from the database
        TextView textUsername = view.findViewById(R.id.username);
        TextView textEmail = view.findViewById(R.id.user_email);
        UserSqlite user = db.getCurrentUser();
        if(user != null){
            textUsername.setText(user.getUsername());
            textEmail.setText(user.getEmail());
        }
        else{
            callUser();
        }

        // Profile picture from the database
        ImageView profilePicture = view.findViewById(R.id.profile_picture);
        // Set the profile picture from the database
        ImagePictureSqlite image = db.getCurrentImage();
        if(image != null){
            Uri imageUri = Uri.parse(image.getImagePath());
            profilePicture.setImageURI(imageUri);
        }



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

        boolean switchState = LanguageUtils.setLanguageSwitchState(view);
        SwitchCompat switchLanguage = view.findViewById(R.id.language_switch);
        switchLanguage.setChecked(switchState);
        switchLanguage.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                // Change the language to French
                LanguageUtils.changeLanguage(view, "fr");

            } else {
                // Change the language to base language
                LanguageUtils.changeLanguage(view, "");
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
                requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_layout, new EditProfileFragment()).commit();
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
    public void callUser(){
        // Call for GetUser
        Call<UserResponse> call = userService.getUser();
        call.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                if (response.isSuccessful()) {
                    UserResponse userResponse = response.body();
                    if (userResponse != null) {
                        TextView textUsername = requireView().findViewById(R.id.username);
                        textUsername.setText(userResponse.getUsername());
                        TextView textEmail = requireView().findViewById(R.id.user_email);
                        textEmail.setText(userResponse.getEmail());

                        // Add the user to the database
                        UserSqlite user = new UserSqlite(userResponse.getId_user(), userResponse.getUsername(), userResponse.getEmail());
                        db.addUser(user);
                    }
                }
            }
            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                System.out.println("Error");
            }
        });
    }

    public void requestGalleryPermission() {
        if (ActivityCompat.checkSelfPermission(getContextOfApplication(), Manifest.permission.READ_MEDIA_IMAGES)
                == PackageManager.PERMISSION_GRANTED) {
            // Permission is already available, start camera preview
            Toast.makeText(getContextOfApplication(),
                    "Gallery permission is available. Starting preview.",
                    Toast.LENGTH_SHORT).show();
            startGallery();
        } else {
            // Permission is missing and must be requested.
            requestGalleryPermissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES);
        }
    }


    private void requestCameraPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(),
                Manifest.permission.CAMERA)) {
            Toast.makeText(getContextOfApplication(), "Camera access is required to display the camera preview. Please allow the permission.",
                    Toast.LENGTH_SHORT).show();
            // Provide an additional rationale to the user. This would happen if the user denied the
            // request previously, but didn't check the "Don't ask again" checkbox.
            requestCameraPermissionLauncher.launch(Manifest.permission.CAMERA);

        } else {
            Toast.makeText(getContextOfApplication(), "Camera permission is not available. Requesting camera permission.",
                    Toast.LENGTH_SHORT).show();
            // Request the permission. The result will be received in onRequestPermissionResult().
            requestCameraPermissionLauncher.launch(Manifest.permission.CAMERA);
        }
    }
    private void startCamera() {
        Intent intent = new Intent(getContextOfApplication(), CameraActivity.class);
        startActivity(intent);
    }

    public void startGallery() {
        imagePickerLauncher.launch(new PickVisualMediaRequest.Builder()
                .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                .build());
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
                    // Camera
                    if (ActivityCompat.checkSelfPermission(getContextOfApplication(), Manifest.permission.CAMERA)
                            == PackageManager.PERMISSION_GRANTED) {
                        // Permission is already available, start camera preview
                        Toast.makeText(getContextOfApplication(),
                                "Camera permission is available. Starting preview.",
                                Toast.LENGTH_SHORT).show();
                        startCamera();
                    } else {
                        // Permission is missing and must be requested.
                        requestCameraPermission();
                    }

                } else if (which == 1) {
                    // Gallery
                    if(ActivityCompat.checkSelfPermission(getContextOfApplication(), Manifest.permission.READ_MEDIA_IMAGES)
                            == PackageManager.PERMISSION_GRANTED){
                        // Permission is already available, start gallery
                        Toast.makeText(getContextOfApplication(),
                                "Gallery permission is available. Starting gallery.",
                                Toast.LENGTH_SHORT).show();
                        startGallery();
                    } else {
                        // Permission is missing and must be requested.
                        requestGalleryPermission();
                    }

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

        // Remove the user from the database
        db.deleteUser();
        db.deleteImage();

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

    //TODO: Change the notification status
    public void changeNotificationStatus(View view, boolean isChecked) {
        if (isChecked) {
            TextView textNotfificationStatus = view.findViewById(R.id.notfication_status);
            textNotfificationStatus.setText(R.string.on);
        } else {
            TextView textNotfificationStatus = view.findViewById(R.id.notfication_status);
            textNotfificationStatus.setText(R.string.off);
        }
    }
}