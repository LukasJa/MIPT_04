package com.example.new_project_04;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class DeleteNoteActivity extends AppCompatActivity {

    private static final String TAG = "DeleteNoteActivity";

    private ArrayAdapter<String> notesAdapter;
    private final List<String> notesList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_note);

        Spinner noteSpinner = findViewById(R.id.noteSpinner);
        Button deleteButton = findViewById(R.id.deleteButton);

        notesAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, notesList);
        notesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        noteSpinner.setAdapter(notesAdapter);

        loadNoteNames();

        deleteButton.setOnClickListener(v -> {
            String selectedNoteName = (String) noteSpinner.getSelectedItem();
            deleteNote(selectedNoteName);
            // Reload note names after deleting
            notesList.clear();
            loadNoteNames();
        });
    }

    private void loadNoteNames() {
        String filename = "notes.txt";
        try {
            FileInputStream fis = openFileInput(filename);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length > 0) {
                    notesList.add(parts[0]);
                }
            }
            fis.close();
            notesAdapter.notifyDataSetChanged();
        } catch (IOException e) {
            Log.e(TAG, "Failed to load note names", e);
        }
    }

    private void deleteNote(String noteName) {
        String filename = "notes.txt";
        String tempFilename = "temp_notes.txt";
        try {
            FileInputStream fis = openFileInput(filename);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            FileOutputStream fos = openFileOutput(tempFilename, Context.MODE_PRIVATE);
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length > 0 && !parts[0].equals(noteName)) {
                    fos.write((line + "\n").getBytes());
                }
            }
            fis.close();
            fos.close();
            deleteFile(filename);
            boolean renamed = renameFile(tempFilename, filename);
            if (!renamed) {
                Log.e(TAG, "Failed to rename file from " + tempFilename + " to " + filename);
            }
        } catch (IOException e) {
            Log.e(TAG, "Failed to delete note", e);
        }
    }

    private boolean renameFile(String from, String to) {
        java.io.File fileFrom = new java.io.File(getFilesDir(), from);
        java.io.File fileTo = new java.io.File(getFilesDir(), to);
        return fileFrom.renameTo(fileTo);
    }
}
