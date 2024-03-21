package com.devmobile.pooplemap;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String DbUri = getResources().getString(R.string.POSTGRESQL_ADDON_URI);
        String DbUser = getResources().getString(R.string.POSTGRESQL_ADDON_USER);
        String DbPass = getResources().getString(R.string.POSTGRESQL_ADDON_PASSWORD);

        DatabaseConnection db = new DatabaseConnection(DbUri, DbUser, DbPass);
    }
}