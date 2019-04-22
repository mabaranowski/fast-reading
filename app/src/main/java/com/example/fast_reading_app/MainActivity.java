package com.example.fast_reading_app;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ListView listView;
    private ListEntry listEntry;
    private ArrayList<ListEntry> files;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = findViewById(R.id.xmlListView);

        if(savedInstanceState != null) {
            //Example
            //feedUrl = savedInstanceState.getString(STATE_URL);
        }

        listEntry = new ListEntry();
        files = new ArrayList<>();

        Context context = getApplicationContext();
        String[] file = context.fileList();

        for (String name : file) {
            listEntry = new ListEntry();
            listEntry.setName(name);
            files.add(listEntry);
        }

        //File directory = context.getFilesDir();
        //getApplicationContext().deleteFile("Title01");

        ListAdapter listAdapter = new ListAdapter(MainActivity.this, R.layout.list_record, files);
        listView.setAdapter(listAdapter);

        AdapterView.OnItemClickListener itemClick = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ListEntry tmp = files.get(position);
                Intent intent = new Intent(getApplicationContext(), ReadActivity.class);

                StringBuilder content = new StringBuilder();
                FileInputStream fileInputStream = null;

                try {
                    fileInputStream = getApplicationContext().openFileInput(tmp.getName());
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

                InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                try {
                    for (String line; (line = bufferedReader.readLine()) != null; ) {
                        content.append(line).append('\n');
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                intent.putExtra("CONTENT", content.toString());
                startActivity(intent);

            }
        };
        listView.setOnItemClickListener(itemClick);

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
                startActivity(new Intent(MainActivity.this, PdfActivity.class));
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

}