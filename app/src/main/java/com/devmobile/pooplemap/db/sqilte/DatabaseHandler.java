package com.devmobile.pooplemap.db.sqilte;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.devmobile.pooplemap.models.User;

import java.math.BigInteger;

public class DatabaseHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "pooplemap";
    private static final String TABLE_USERS = "users";
    private static final String KEY_id = "id_user";
    private static final String KEY_username = "username";
    private static final String KEY_email = "email";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(android.database.sqlite.SQLiteDatabase db) {
        String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USERS + "("
                + KEY_id + " INTEGER PRIMARY KEY," + KEY_username + " TEXT,"
                + KEY_email + " TEXT" + ")";
        db.execSQL(CREATE_USERS_TABLE);
    }

    @Override
    public void onUpgrade(android.database.sqlite.SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(db);
    }

    public void addUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(KEY_username, user.getUsername());
        values.put(KEY_email, user.getEmail());

        db.insert(TABLE_USERS, null, values);
        db.close();
    }

    public User getCurrentUser() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS, new String[] { KEY_id,
                        KEY_username, KEY_email }, KEY_id + "=?",
                new String[] { String.valueOf(1) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        User user = new User();
        user.setId(BigInteger.valueOf(cursor.getInt(0)));
        user.setUsername(cursor.getString(1));
        user.setEmail(cursor.getString(2));

        return user;
    }

    public void deleteUser() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_USERS, KEY_id + " = ?",
                new String[] { String.valueOf(1) });
        db.close();
    }



}
