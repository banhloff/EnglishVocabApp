package com.example.englishvocabapp.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.englishvocabapp.Dao.AppDatabase;
import com.example.englishvocabapp.Entities.Note;
import com.example.englishvocabapp.R;
import com.example.englishvocabapp.Utils.Constants;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NoteEditActivity extends AppCompatActivity {
    @BindView(R.id.fab_save)
    FloatingActionButton fabSave;
    @BindView(R.id.fab_cancel)
    FloatingActionButton fabCancel;
    @BindView(R.id.txt_edit_title)
    EditText txtTitle;
    @BindView(R.id.txt_edit_content)
    EditText txtContent;
    Note note;
    String mode;
    AppDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_edit);
        ButterKnife.bind(this);
        db = AppDatabase.getInMemoryDatabase(this);
        Intent intent = getIntent();
        mode = intent.getStringExtra(Constants.INTENT_EDIT_MODE);
        note = (Note) intent.getParcelableExtra(Constants.PARCEL_NOTE);
        txtTitle.setText(note.getTitle());
        txtContent.setText(note.getContent());
        if(mode.equals(Constants.ADD_MODE)) {
        }
    }

    @OnClick(R.id.fab_save)
    void onSaveClicked(View view) {
        Intent i = new Intent();
        String title = String.valueOf(txtTitle.getText());
        if(title.trim().length() == 0) {
            Toast.makeText(NoteEditActivity.this, "Insert Note Title", Toast.LENGTH_SHORT);
            return;
        }
        note.setTitle(String.valueOf(txtTitle.getText()));
        note.setContent(String.valueOf(txtContent.getText()));
        Date now = new Date();
        if(mode.equals(Constants.ADD_MODE)) {
            note.setCreatedDate(now);
            db.noteDao().insert(note);
        } else if (mode.equals(Constants.EDIT_MODE)) {
            db.noteDao().update(note);
        }
        i.putExtra(Constants.PARCEL_NOTE, note);
        setResult(RESULT_OK, i);
        finish();
    }

    @OnClick(R.id.fab_cancel)
    void onCancelClicked(View view) {
        Intent i = new Intent();
        setResult(RESULT_CANCELED, i);
        finish();
    }
}