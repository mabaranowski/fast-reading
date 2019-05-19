package com.example.fast_reading_app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.ListIterator;

public class ReadActivity extends AppCompatActivity {

    private TextView mainTextView;
    private Button mainButton;
    private ArrayList<String> list;
    private ImageButton backButton;
    private ImageButton settingsButton;

    private Intent receiveIntent;
    private Intent optionsIntent;
    private String content;
    private Integer wpm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.read_txt);
        mainTextView = findViewById(R.id.mainTextView);
        mainButton = findViewById(R.id.mainButton);
        backButton = findViewById(R.id.backButton02);
        settingsButton = findViewById(R.id.settingsButton02);

        View.OnClickListener backToHome = (new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ReadActivity.this, MainActivity.class));
            }
        });
        backButton.setOnClickListener(backToHome);


        View.OnClickListener speedSettings = (new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                optionsIntent = new Intent(ReadActivity.this, ReadMenuActivity.class);
                optionsIntent.putExtra("CONTENT", content);
                optionsIntent.putExtra("WPM", wpm);
                startActivity(optionsIntent);
            }
        });
        settingsButton.setOnClickListener(speedSettings);

        receiveIntent = getIntent();
        content = receiveIntent.getStringExtra("CONTENT");
        wpm = receiveIntent.getIntExtra("WPM", 250);

        final int delay = (60 * 1000) / wpm;
        View.OnClickListener mainButtonListener = new View.OnClickListener() {
            ListIterator<String> iterator = splitText(content).listIterator();
            boolean hold = true;

            @Override
            public void onClick(View v) {
                if (!iterator.hasNext()) {
                    iterator = splitText(content).listIterator();
                    hold = true;
                }

                if (hold) {
                    hold = false;
                    new Thread() {
                        public void run() {
                            while (iterator.hasNext() && !hold) {
                                try {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            mainTextView.setText(iterator.next());
                                        }
                                    });
                                    Thread.sleep(delay);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }.start();
                } else {
                    hold = true;
                }

            }
        };
        mainButton.setOnClickListener(mainButtonListener);

    }

    private ArrayList<String> splitText(String text) {
        list = new ArrayList<>(Arrays.asList(text.split(" ")));
        return list;
    }

}
