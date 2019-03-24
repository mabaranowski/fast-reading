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

        View.OnClickListener mainButtonListener = new View.OnClickListener() {
            ListIterator<String> iterator = splitText(randomText).listIterator();
            boolean hold = true;

            @Override
            public void onClick(View v) {
                if (!iterator.hasNext()) {
                    iterator = splitText(txt).listIterator();
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
                                    Thread.sleep(150);
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

    Intent intent = new Intent();
    String txt = intent.getStringExtra("CONTENT");
    String randomText = "Tesla All-Wheel Drive has two independent motors. " +
            "Unlike traditional all-wheel drive systems, these two motors digitally control torque to the front and rear wheels—for far better handling and traction control. " +
            "Your car can drive on either motor, so you never need to worry about getting stuck on the road. " +
            "If one motor stops working, you can safely continue to your destination with the second.";

    private ArrayList<String> splitText(String text) {
        list = new ArrayList<>(Arrays.asList(text.split(" ")));
        return list;
    }



}
