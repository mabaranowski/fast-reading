package com.example.fast_reading_app;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageButton;

import java.util.Locale;

public class MainMenuActivity extends AppCompatActivity {

    private ImageButton cancelButton;
    private ImageButton englishButton;
    private ImageButton polishButton;
    private Locale myLocale;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu);
        findByIds();

        cancelButton.setOnClickListener(cancelClick);
        englishButton.setOnClickListener(englishClick);
        polishButton.setOnClickListener(polishClick);
    }

    private void setLocale(String locale) {
            myLocale = new Locale(locale);

            Resources res = getResources();
            DisplayMetrics dm = res.getDisplayMetrics();
            Configuration conf = res.getConfiguration();
            conf.locale = myLocale;
            res.updateConfiguration(conf, dm);

            refresh(locale);
    }

    private void refresh(String currentLang) {
        Intent refresh = new Intent(MainMenuActivity.this, MainMenuActivity.class);
        refresh.putExtra(currentLang, currentLang);
        refresh.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(refresh);
    }

    private View.OnClickListener cancelClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            startActivity(new Intent(MainMenuActivity.this, MainActivity.class));
        }
    };

    private View.OnClickListener englishClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            setLocale("en");
        }
    };

    private View.OnClickListener polishClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            setLocale("pl");
        }
    };

    private void findByIds() {
        cancelButton = findViewById(R.id.backButton03);
        englishButton = findViewById(R.id.englishButton);
        polishButton = findViewById(R.id.polishButton);
    }

}
