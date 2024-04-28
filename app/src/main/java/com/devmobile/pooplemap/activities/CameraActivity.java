package com.devmobile.pooplemap.activities;

import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import android.graphics.Bitmap;

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
import com.devmobile.pooplemap.db.sqilte.DatabaseHandler;
import com.devmobile.pooplemap.db.sqilte.entities.ImagePictureSqlite;
import com.google.common.util.concurrent.ListenableFuture;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class CameraActivity extends AppCompatActivity {

    @Inject DatabaseHandler db;
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


                        /*
                        // Load the captured image into a Bitmap
                        Bitmap capturedBitmap = BitmapFactory.decodeFile(file.getAbsolutePath());

                        // Create a circular bitmap for the profile picture
                        Bitmap profileBitmap = getRoundedBitmap(capturedBitmap);

                        // Save the circular profile picture to a file
                        saveBitmapToFile(profileBitmap, file);

                         */

                        // Delete the previous database image
                        db.deleteImage();

                        // Save the image to the database
                        ImagePictureSqlite image = new ImagePictureSqlite(0, file.getPath(), "Description");
                        db.addImage(image);

                        //Toast.makeText(getApplicationContext(), "Image saved at: " + file.getPath(), Toast.LENGTH_SHORT).show();
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



    private Bitmap getRoundedBitmap(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int diameter = Math.min(width, height);

        Bitmap output = Bitmap.createBitmap(diameter, diameter, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, diameter, diameter);

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        canvas.drawCircle(diameter / 2, diameter / 2, diameter / 2, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }

    private void saveBitmapToFile(Bitmap bitmap, File file) {
        try {
            FileOutputStream outputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private int aspectRatio(int width, int height) {
        double previewRatio = (double) Math.max(width, height) / Math.min(width, height);
        if (Math.abs(previewRatio - 4.0 / 3.0) <= Math.abs(previewRatio - 16.0 / 9.0)) {
            return AspectRatio.RATIO_4_3;
        }
        return AspectRatio.RATIO_16_9;
    }
}
