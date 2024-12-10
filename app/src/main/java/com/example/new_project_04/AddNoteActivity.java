package com.example.new_project_04;

import android.content.Context;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import java.io.FileOutputStream;
import java.io.IOException;

public class AddNoteActivity extends AppCompatActivity {

    private static final String TAG = "AddNoteActivity";

    private EditText noteNameEditText;
    private EditText noteContentEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        noteNameEditText = findViewById(R.id.noteNameEditText);
        noteContentEditText = findViewById(R.id.noteContentEditText);
        Button saveButton = findViewById(R.id.saveButton);

        saveButton.setOnClickListener(v -> {
            String noteName = noteNameEditText.getText().toString();
            String noteContent = noteContentEditText.getText().toString();

            if (!noteName.isEmpty() && !noteContent.isEmpty()) {
                saveNote(noteName, noteContent);
                Log.d(TAG, "Note saved: " + noteName);
                finish();
            } else {
                Toast.makeText(AddNoteActivity.this, "Please enter both note name and content.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveNote(String noteName, String noteContent) {
        String filename = "notes.txt";
        try {
            FileOutputStream fos = openFileOutput(filename, Context.MODE_APPEND);
            fos.write((noteName + "|" + noteContent + "\n").getBytes());
            fos.close();
        } catch (IOException e) {
            Log.e(TAG, "Failed to save note", e);
        }
    }
}
