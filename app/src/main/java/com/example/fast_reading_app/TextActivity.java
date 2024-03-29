package com.example.fast_reading_app;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import java.io.FileOutputStream;

public class TextActivity extends AppCompatActivity {

    private Button submitButton;
    private EditText editText;
    private EditText titleText;
    private ImageButton backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_txt);
        findByIds();

        backButton.setOnClickListener(backToMain);
        submitButton.setOnClickListener(submitTextAction);
    }

    private View.OnClickListener submitTextAction = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String title = titleText.getText().toString();
            String content = editText.getText().toString();

            FileOutputStream outputStream;
            try {
                outputStream = openFileOutput(title, Context.MODE_PRIVATE);
                outputStream.write(content.getBytes());
                outputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        }
    };

    private View.OnClickListener backToMain = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            startActivity(new Intent(TextActivity.this, MainActivity.class));
        }
    };

    private void findByIds() {
        submitButton = findViewById(R.id.submitButton);
        editText = findViewById(R.id.editText);
        titleText = findViewById(R.id.titleText);
        backButton = findViewById(R.id.backButton02);
    }

}
