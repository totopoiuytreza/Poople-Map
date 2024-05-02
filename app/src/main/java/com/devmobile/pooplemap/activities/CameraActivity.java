package com.devmobile.pooplemap.activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.AspectRatio;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.content.ContextCompat;

import com.devmobile.pooplemap.MainActivity;
import com.devmobile.pooplemap.R;
import com.devmobile.pooplemap.db.jdbc.DatabaseHandlerImg;
import com.devmobile.pooplemap.db.jdbc.entities.UserProfilePicture;
import com.devmobile.pooplemap.db.sqilte.DatabaseHandlerSqlite;
import com.devmobile.pooplemap.db.sqilte.entities.ImagePictureSqlite;
import com.google.common.util.concurrent.ListenableFuture;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class CameraActivity extends AppCompatActivity {
    DatabaseHandlerImg dbImg = new DatabaseHandlerImg();

    @Inject
    DatabaseHandlerSqlite db;
    private PreviewView cameraPreview;
    ImageView capture, flipCamera;
    int cameraFacing = CameraSelector.LENS_FACING_BACK;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_preview);

        cameraPreview = findViewById(R.id.cameraPreview);
        capture = findViewById(R.id.capture);
        flipCamera = findViewById(R.id.flipCamera);

        startCamera(cameraFacing);
        flipCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cameraFacing == CameraSelector.LENS_FACING_BACK) {
                    cameraFacing = CameraSelector.LENS_FACING_FRONT;
                } else {
                    cameraFacing = CameraSelector.LENS_FACING_BACK;
                }
                startCamera(cameraFacing);
            }
        });

    }

    private void startCamera(int cameraFacing) {
        // Start the camera preview
        int aspectRatio = aspectRatio(cameraPreview.getWidth(), cameraPreview.getHeight());
        ListenableFuture<ProcessCameraProvider> listenableFuture = ProcessCameraProvider.getInstance(this);
        listenableFuture.addListener(() -> {
            try {
                ProcessCameraProvider cameraProvider = (ProcessCameraProvider) listenableFuture.get();

                Preview preview = new Preview.Builder().setTargetAspectRatio(aspectRatio).build();

                ImageCapture imageCapture = new ImageCapture.Builder().setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
                        .setTargetRotation(getWindowManager().getDefaultDisplay().getRotation()).build();

                CameraSelector cameraSelector = new CameraSelector.Builder()
                        .requireLensFacing(cameraFacing).build();

                cameraProvider.unbindAll();

                Camera camera = cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture);

                capture.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        takePicture(imageCapture);
                    }
                });

                preview.setSurfaceProvider(cameraPreview.getSurfaceProvider());
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
        }, ContextCompat.getMainExecutor(this));
    }

    public void takePicture(ImageCapture imageCapture) {
        final File file = new File(getExternalFilesDir(null), System.currentTimeMillis() + ".jpg");
        ImageCapture.OutputFileOptions outputFileOptions = new ImageCapture.OutputFileOptions.Builder(file).build();
        imageCapture.takePicture(outputFileOptions, Executors.newCachedThreadPool(), new ImageCapture.OnImageSavedCallback() {
            @Override
            public void onImageSaved(@NonNull ImageCapture.OutputFileResults outputFileResults) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        // Delete the previous database image
                        db.deleteImage();

                        // Save the image to the database
                        ImagePictureSqlite image = new ImagePictureSqlite(file.getPath(), "Description");
                        db.addImage(image);

                        // Save the image to the database with JDBC
                        BigInteger userId = db.getCurrentUser().getId();
                        byte[] imageBytes = null;
                        try {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                imageBytes = Files.readAllBytes(file.toPath());
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        UserProfilePicture userProfilePicture = new UserProfilePicture(userId, imageBytes, "Description");
                        int SDK_INT = android.os.Build.VERSION.SDK_INT;
                        if (SDK_INT > 8)
                        {
                            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                                    .permitAll().build();
                            StrictMode.setThreadPolicy(policy);
                            dbImg.insertUserProfilePicture(userProfilePicture);
                        }
                    }
                });
                startActivity(new Intent(CameraActivity.this, MainActivity.class));
            }

            @Override
            public void onError(@NonNull ImageCaptureException exception) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "Failed to save: " + exception.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
                startCamera(cameraFacing);
            }
        });
    }

    private int aspectRatio(int width, int height) {
        double previewRatio = (double) Math.max(width, height) / Math.min(width, height);
        if (Math.abs(previewRatio - 4.0 / 3.0) <= Math.abs(previewRatio - 16.0 / 9.0)) {
            return AspectRatio.RATIO_4_3;
        }
        return AspectRatio.RATIO_16_9;
    }
}
