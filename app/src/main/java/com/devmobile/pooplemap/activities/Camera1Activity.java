package com.devmobile.pooplemap.activities;

import android.hardware.Camera;
import android.os.Bundle;
import android.os.Environment;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.devmobile.pooplemap.R;
import com.devmobile.pooplemap.db.sqilte.DatabaseHandlerSqlite;
import com.devmobile.pooplemap.db.sqilte.entities.ImagePictureSqlite;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
@AndroidEntryPoint
public class Camera1Activity extends AppCompatActivity {
    @Inject
    DatabaseHandlerSqlite db;
    private Camera camera;
    private SurfaceView surfaceView;
    private SurfaceHolder surfaceHolder;
    private boolean isCameraOpen = false;
    ImageView capture;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_preview);
        capture = findViewById(R.id.capture);
        surfaceView = findViewById(R.id.cameraPreview);
        surfaceHolder = surfaceView.getHolder();
        capture.setOnClickListener(v -> {
            captureImage();
        });
        surfaceHolder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(@NonNull SurfaceHolder holder) {
                openCamera();
            }

            @Override
            public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {
            }

            @Override
            public void surfaceDestroyed(@NonNull SurfaceHolder holder) {
                closeCamera();
            }
        });
    }

    private void openCamera() {
        if (!isCameraOpen) {
            camera = Camera.open();
            try {
                camera.setPreviewDisplay(surfaceHolder);
                camera.startPreview();
                isCameraOpen = true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void closeCamera() {
        if (isCameraOpen) {
            camera.stopPreview();
            camera.release();
            isCameraOpen = false;
        }
    }

    private void captureImage() {
        if (isCameraOpen) {
            camera.takePicture(null, null, new Camera.PictureCallback() {
                @Override
                public void onPictureTaken(byte[] data, Camera camera) {
                    // Save the image to the device's storage
                    saveImageToStorage(data);

                }
            });
        }
    }

    private void saveImageToStorage(byte[] data) {

        try{
            File imageFile = new File(getExternalFilesDir(null), "image_" + System.currentTimeMillis() + ".jpg");
            FileOutputStream out = new FileOutputStream(imageFile);
            out.write(data);
            out.flush();
            out.close();
            // Delete the previous database image
            db.deleteImage();

            // Save the image to the database
            ImagePictureSqlite image = new ImagePictureSqlite(imageFile.getPath(), "Description");
            db.addImage(image);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
