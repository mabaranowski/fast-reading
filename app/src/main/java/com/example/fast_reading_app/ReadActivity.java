package com.example.fast_reading_app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.ListIterator;

public class ReadActivity extends AppCompatActivity {

    private TextView mainTextView;
    private Button mainButton;
    private ArrayList<String> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.read_txt);
        mainTextView = findViewById(R.id.mainTextView);
        mainButton = findViewById(R.id.mainButton);

        Intent intent = getIntent();
        final String content = intent.getStringExtra("CONTENT");

        final int delay = 300;
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
