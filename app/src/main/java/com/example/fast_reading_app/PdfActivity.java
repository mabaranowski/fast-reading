package com.example.fast_reading_app;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

public class PdfActivity extends AppCompatActivity {

    private ListView listView;
    private ListEntry listEntry;
    private TextView pathField;
    private EditText searchBar;

    private static final String DIRECTORY = "Documents";
    private ArrayList<ListEntry> pdfList;
    private ListAdapter listAdapter;
    private ImageButton backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pdf_files);
        findByIds();

        backButton.setOnClickListener(backToMain);
        listView.setOnItemClickListener(itemClick);

        String path = getRemovableStoragePath() + "/" + DIRECTORY;
        pathField.setText(path);
        pdfList = getListOfPdfs(path);

        listAdapter = new ListAdapter(PdfActivity.this, R.layout.list_record, pdfList);
        listView.setAdapter(listAdapter);

        searchBar.addTextChangedListener(searchFilter);
    }

    private ArrayList<ListEntry> getListOfPdfs(String path) {
        ArrayList<ListEntry> tmpPdfList = new ArrayList<>();

        File inFileList[] = new File(path).listFiles();
        for (File file : inFileList) {
            listEntry = new ListEntry();
            listEntry.setName(file.getName());
            listEntry.setPath(file.getAbsolutePath());

            tmpPdfList.add(listEntry);
        }

        return tmpPdfList;
    }

    private String getRemovableStoragePath() {
        String removableStoragePath = null;
        File fileList[] = new File("/storage/").listFiles();
        for (File file : fileList) {
            if (!file.getAbsolutePath().equalsIgnoreCase(Environment.getExternalStorageDirectory().getAbsolutePath())
                    && file.isDirectory() && file.canRead()) {
                removableStoragePath = file.getAbsolutePath();
                break;
            }
        }

        return removableStoragePath;
    }

    private void backToMain() {
        Intent intent = new Intent(PdfActivity.this, MainActivity.class);
        startActivity(intent);
    }

    private View.OnClickListener backToMain = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            startActivity(new Intent(PdfActivity.this, MainActivity.class));
        }
    };

    private AdapterView.OnItemClickListener itemClick = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            ListEntry pdf = pdfList.get(position);
            String title = pdf.getName();
            String path = pdf.getPath();
            String content = "";

            try {
                PdfReader reader = new PdfReader(path);
                int n = reader.getNumberOfPages();

                for (int i = 0; i < n ; i++) {
                    content += PdfTextExtractor.getTextFromPage(reader, i + 1).trim() + "\n";
                }

                reader.close();
            } catch (Exception e) {
                System.out.println(e);
            }

            FileOutputStream outputStream;
            try {
                outputStream = openFileOutput(title, Context.MODE_PRIVATE);
                outputStream.write(content.getBytes());
                outputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

            backToMain();
        }
    };

    private TextWatcher searchFilter = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            (PdfActivity.this).listAdapter.getFilter().filter(s.toString());
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    private void findByIds() {
        listView = findViewById(R.id.pdfListView);
        pathField = findViewById(R.id.pathText);
        searchBar = findViewById(R.id.searchBar02);
        backButton = findViewById(R.id.backButton02);
    }

}
