package com.example.fast_reading_app;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ListView listApps;
    private ArrayList<FeedEntry> applications;
    private FeedEntry feedEntry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listApps = findViewById(R.id.xmlListView);

        if(savedInstanceState != null) {
            //Example
            //feedUrl = savedInstanceState.getString(STATE_URL);
        }

        feedEntry = new FeedEntry();
        applications = new ArrayList<>();

        Context context = getApplicationContext();
        String[] file = context.fileList();

        for (String name : file) {
            feedEntry = new FeedEntry();
            feedEntry.setName(name);
            applications.add(feedEntry);
        }


//        File directory = context.getFilesDir();
//        File file = new File(directory, filename);

//        getApplicationContext().deleteFile("Title01");





        FeedAdapter feedAdapter = new FeedAdapter(MainActivity.this, R.layout.list_record, applications);
        listApps.setAdapter(feedAdapter);

        AdapterView.OnItemClickListener itemClick = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                FeedEntry tmp = applications.get(position);
                Intent intent = new Intent(getApplicationContext(), ReadActivity.class);

                String content = null;
                FileInputStream fileInputStream = null;

                try {
                    fileInputStream = getApplicationContext().openFileInput(tmp.getName());
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

                InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                try {
                    content = bufferedReader.readLine();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                intent.putExtra("CONTENT", content);
                startActivity(intent);

            }
        };
        listApps.setOnItemClickListener(itemClick);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_content_menu,  menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case(R.id.addPdf):
                break;
            case(R.id.addTxt):
                startActivity(new Intent(MainActivity.this, TextActivity.class));
                break;
            default:
                return super.onOptionsItemSelected(item);
        }

        return true;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //Example
        //outState.putString(STATE_URL, feedUrl);
        super.onSaveInstanceState(outState);
    }

    private void openReadActivity(String content) {
        Intent intent = new Intent(this, ReadActivity.class);
        intent.putExtra("CONTENT", content);
        startActivity(intent);
    }

}