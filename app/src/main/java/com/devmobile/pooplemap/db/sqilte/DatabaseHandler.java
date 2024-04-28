package com.devmobile.pooplemap.db.sqilte;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.devmobile.pooplemap.db.sqilte.entities.UserSqlite;
import com.devmobile.pooplemap.models.User;
import com.devmobile.pooplemap.db.sqilte.entities.ImagePictureSqlite;

import java.math.BigInteger;

public class DatabaseHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "pooplemap";
    private static final String TABLE_USERS = "users";
    private static final String KEY_id = "id_user";
    private static final String KEY_username = "username";
    private static final String KEY_email = "email";

    private static final String TABLE_IMAGES = "images";
    private static final String KEY_image_id = "image_id";
    private static final String KEY_image_path = "image_path";
    private static final String KEY_image_description = "image_description";


    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(android.database.sqlite.SQLiteDatabase db) {
        String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USERS + "("
                + KEY_id + " INTEGER PRIMARY KEY," + KEY_username + " TEXT,"
                + KEY_email + " TEXT" + ")";

        db.execSQL(CREATE_USERS_TABLE);
        String CREATE_IMAGES_TABLE = "CREATE TABLE " + TABLE_IMAGES + "("
                + KEY_image_id + " INTEGER PRIMARY KEY," + KEY_image_path + " TEXT,"
                + KEY_image_description + " TEXT" + ")";
        db.execSQL(CREATE_IMAGES_TABLE);
    }

    @Override
    public void onUpgrade(android.database.sqlite.SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_IMAGES);
        onCreate(db);
    }

    public void addUser(UserSqlite user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(KEY_username, user.getUsername());
        values.put(KEY_email, user.getEmail());

        db.insert(TABLE_USERS, null, values);
        db.close();
    }

    public UserSqlite getCurrentUser() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS, new String[] { KEY_id,
                        KEY_username, KEY_email,  }, KEY_id + "=?",
                new String[] { String.valueOf(1) }, null, null, null, null);

        UserSqlite user = null; // Initialize the user variable to null

        if (cursor != null && cursor.moveToFirst()) {
            user = new UserSqlite(BigInteger.valueOf(cursor.getInt(0)), cursor.getString(1), cursor.getString(2));
        }
        if (cursor != null) {
            cursor.close(); // Close the cursor to release resources
        }

        return user;
    }

    public void updateUser(UserSqlite user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(KEY_username, user.getUsername());
        values.put(KEY_email, user.getEmail());

        db.update(TABLE_USERS, values, KEY_id + " = ?",
                new String[] { String.valueOf(1) });
        db.close();
    }

    public void deleteUser() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_USERS, KEY_id + " = ?",
                new String[] { String.valueOf(1) });
        db.close();
    }

    public void addImage(ImagePictureSqlite image) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(KEY_image_path, image.getImagePath());
        values.put(KEY_image_description, image.getDescription());
        // Add more values for additional columns

        db.insert(TABLE_IMAGES, null, values);
        db.close();
    }

    public ImagePictureSqlite getCurrentImage() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_IMAGES, new String[] { KEY_image_id,
                        KEY_image_path, KEY_image_description }, KEY_image_id + "=?",
                new String[] { String.valueOf(1) }, null, null, null, null);
        ImagePictureSqlite image = null; // Initialize the image variable to null

        if (cursor != null && cursor.moveToFirst()) {
            image = new ImagePictureSqlite(cursor.getInt(0), cursor.getString(1), cursor.getString(2));
        }

        if (cursor != null) {
            cursor.close(); // Close the cursor to release resources
        }

        return image;
    }

    public void deleteImage() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_IMAGES, KEY_image_id + " = ?",
                new String[] { String.valueOf(1) });
        db.close();
    }



}
