package com.devmobile.pooplemap.utils;

import static com.devmobile.pooplemap.MainActivity.getContextOfApplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;

import android.content.Context;

import android.content.res.Resources;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.widget.SwitchCompat;

import com.devmobile.pooplemap.MainActivity;
import com.devmobile.pooplemap.R;

import java.util.Locale;

public class LanguageUtils {

    public static void changeLanguage(View view, String language) {

        // Change the locale to selected language
        Locale locale = new Locale(language);
        Locale.setDefault(locale);

        // Create a configuration object and update the app's resources
        Configuration config = new Configuration();
        config.setLocale(locale);
        view.getContext().getResources().updateConfiguration(config, view.getContext().getResources().getDisplayMetrics());

        // Start the activity to apply the new language and store the switch state in SharedPreferences
        SwitchCompat switchLanguage = view.findViewById(R.id.language_switch);
        boolean switchState = switchLanguage.isChecked();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(view.getContext().getApplicationContext());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("languageSwitchState", switchState);
        editor.apply();

        view.getContext().startActivity(new Intent(view.getContext().getApplicationContext(), MainActivity.class));
        if (language.equals("fr")) {
            Toast.makeText(view.getContext().getApplicationContext(), "Langue changé à Français", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(view.getContext().getApplicationContext(), "Language changed to English", Toast.LENGTH_SHORT).show();
        }
    }

    public static boolean setLanguageSwitchState(View view) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(view.getContext().getApplicationContext());
        boolean switchState = sharedPreferences.getBoolean("languageSwitchState", false);

        TextView chosenLanguage = view.findViewById(R.id.chosen_language);
        if (switchState) {
            chosenLanguage.setText(R.string.french);
        } else {
            chosenLanguage.setText(R.string.english);
        }
        return switchState;
    }
}
