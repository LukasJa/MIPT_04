package com.example.new_project_04;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private final List<String> notesList = new ArrayList<>();
    private ArrayAdapter<String> notesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "MainActivity onCreate called");

        ListView listView = findViewById(R.id.listView);
        notesAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, notesList);
        listView.setAdapter(notesAdapter);

        loadNotes();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_add_note) {
            startActivity(new Intent(this, AddNoteActivity.class));
            return true;
        } else if (id == R.id.action_delete_note) {
            startActivity(new Intent(this, DeleteNoteActivity.class));
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        notesList.clear();
        loadNotes();
    }

    private void loadNotes() {
        String filename = "notes.txt";
        try {
            FileInputStream fis = openFileInput(filename);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            String line;
            while ((line = br.readLine()) != null) {
                notesList.add(line);
            }
            fis.close();
            notesAdapter.notifyDataSetChanged();
            Log.d(TAG, "Notes loaded successfully");
        } catch (Exception e) {
            Log.e(TAG, "Failed to load notes", e);
        }
    }
}
