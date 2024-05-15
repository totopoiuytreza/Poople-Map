package com.devmobile.pooplemap.utils;

import static com.devmobile.pooplemap.MainActivity.getContextOfApplication;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.exifinterface.media.ExifInterface;

import com.devmobile.pooplemap.db.jdbc.DatabaseHandlerImg;
import com.devmobile.pooplemap.db.sqilte.DatabaseHandlerSqlite;
import com.devmobile.pooplemap.db.sqilte.entities.ImagePictureSqlite;
import com.devmobile.pooplemap.db.sqilte.entities.UserSqlite;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.util.concurrent.CompletableFuture;

import javax.inject.Inject;

public class ProfileInfoUtils {

    public static void changeProfileInfo(TextView textUsername, TextView textEmail, UserSqlite user, ImageView profilePicture, ImagePictureSqlite imagePicture) {
        textUsername.setText(user.getUsername());
        textEmail.setText(user.getEmail());

        if(imagePicture!=null)
            addProfilePicture(profilePicture, imagePicture);

    }

    public static void addProfilePicture(ImageView profilePicture, ImagePictureSqlite imagePicture) {

        Uri imageUri = Uri.parse(imagePicture.getImagePath());
        int rotate = getCameraPhotoOrientation(getContextOfApplication(), imageUri, imagePicture.getImagePath());
        if(rotate!=0) {
            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            Bitmap bitmap = BitmapFactory.decodeFile(imagePicture.getImagePath(), bmOptions);
            bitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), bitmap.getConfig());
            Canvas canvas = new Canvas(bitmap);
            canvas.drawBitmap(bitmap, 0, 0, null);
            canvas.rotate(rotate, bitmap.getWidth() / 2, bitmap.getHeight() / 2);
            profilePicture.setImageBitmap(bitmap);
        }
        else {
            profilePicture.setImageURI(imageUri);
        }
    }
    public static int getCameraPhotoOrientation(Context context, Uri imageUri, String imagePath){
        int rotate = 0;
        try {
            context.getContentResolver().notifyChange(imageUri, null);
            File imageFile = new File(imagePath);
            ExifInterface exif = new ExifInterface(
                    imageFile.getAbsolutePath());
            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);

            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotate = 270;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotate = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotate = 90;
                    break;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return rotate;
    }
}
