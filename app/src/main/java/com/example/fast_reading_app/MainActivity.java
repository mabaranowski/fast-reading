package com.example.fast_reading_app;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.SparseBooleanArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
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
    private ListAdapter listAdapter;

    private Button addPdfButton;
    private Button addTextButton;
    private EditText searchBar;

    private ImageButton settingsButton;
    private ImageButton editButton;
    private ImageButton deleteButton;

    private Boolean deleteVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = findViewById(R.id.xmlListView);
        addPdfButton = findViewById(R.id.addPdf);
        addTextButton = findViewById(R.id.addText);
        searchBar = findViewById(R.id.searchBar);
        settingsButton = findViewById(R.id.settingsButton);
        editButton = findViewById(R.id.editButton);
        deleteButton = findViewById(R.id.deleteButton);

        if(savedInstanceState != null) {
            //Example: feedUrl = savedInstanceState.getString(STATE_URL);
        }

        View.OnClickListener addPdfActivity = (new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, PdfActivity.class));
            }
        });
        addPdfButton.setOnClickListener(addPdfActivity);

        View.OnClickListener addTextActivity = (new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, TextActivity.class));
            }
        });
        addTextButton.setOnClickListener(addTextActivity);

        View.OnClickListener openSettings = (new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, MainMenuActivity.class));
            }
        });
        settingsButton.setOnClickListener(openSettings);

        View.OnClickListener openEdit = (new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listView.setChoiceMode(listView.CHOICE_MODE_MULTIPLE);

                if(deleteVisible) {
                    deleteButton.setVisibility(View.INVISIBLE);
                    listAdapter.setCheckBoxVisible(false);
                } else {
                    deleteButton.setVisibility(View.VISIBLE);
                    listAdapter.setCheckBoxVisible(true);
                }

                deleteVisible = !deleteVisible;
                listAdapter.notifyDataSetChanged();
            }
        });
        editButton.setOnClickListener(openEdit);

        View.OnClickListener deleteFromList = (new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SparseBooleanArray checkedItemPositions = listAdapter.getItemStateArray();
                int itemCount = listView.getCount();

                for(int i = itemCount-1; i >= 0; i--){
                    if(checkedItemPositions.get(i)){
                        getApplicationContext().deleteFile(files.get(i).getName());
                    }
                }
                checkedItemPositions.clear();
                listAdapter.notifyDataSetChanged();
                refresh();
            }
        });
        deleteButton.setOnClickListener(deleteFromList);

        listEntry = new ListEntry();
        files = new ArrayList<>();

        String[] file = getApplicationContext().fileList();

        for (String name : file) {
            listEntry = new ListEntry();
            listEntry.setName(name);
            files.add(listEntry);
        }

        //File directory = context.getFilesDir();
        //getApplicationContext().deleteFile("Title01");

        listAdapter = new ListAdapter(MainActivity.this, R.layout.list_record, files);
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

        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                (MainActivity.this).listAdapter.getFilter().filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    private void refresh() {
        Intent refresh = new Intent(MainActivity.this, MainActivity.class);
        refresh.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(refresh);

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
        //Example outState.putString(STATE_URL, feedUrl);
        super.onSaveInstanceState(outState);
    }

}