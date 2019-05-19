package com.example.fast_reading_app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

public class ReadMenuActivity extends AppCompatActivity {

    private ImageButton cancelButton;
    private SeekBar seekBar;
    private TextView seekText;
    private Integer wpm;
    private Button submitButton;

    private Intent receiveIntent;
    private Intent readIntent;
    private String content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.read_menu);
        cancelButton = findViewById(R.id.cancelButton02);
        seekBar = findViewById(R.id.seekBar);
        seekText = findViewById(R.id.seekText);
        submitButton = findViewById(R.id.submitButton02);

        receiveIntent = getIntent();
        content = receiveIntent.getStringExtra("CONTENT");
        wpm = receiveIntent.getIntExtra("WPM", 250);
        seekText.setText(wpm.toString());
        seekBar.setProgress((wpm - 200) / 50);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                wpm = (progress * 50) + 200;
                seekText.setText(wpm.toString());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        View.OnClickListener backToRead = (new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                readIntent = new Intent(ReadMenuActivity.this, ReadActivity.class);
                readIntent.putExtra("CONTENT", content);
                readIntent.putExtra("WPM", wpm);
                startActivity(readIntent);
            }
        });
        cancelButton.setOnClickListener(backToRead);
        submitButton.setOnClickListener(backToRead);

    }
}
