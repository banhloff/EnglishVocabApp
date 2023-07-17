package com.example.englishvocabapp.Activities;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.englishvocabapp.Entities.Note;
import com.example.englishvocabapp.R;
import com.example.englishvocabapp.Utils.Constants;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NoteDetailActivity extends AppCompatActivity {

    @BindView(R.id.txt_detail_title)
    TextView txtTitle;
    @BindView(R.id.txt_detail_content)
    TextView txtContent;
    @BindView(R.id.txt_detail_date)
    TextView txtDate;
    @BindView(R.id.fab_edit)
    FloatingActionButton fabEdit;
    @BindView(R.id.fab_delete)
    FloatingActionButton fabDelete;
    @BindView(R.id.fab_more)
    FloatingActionButton fabMore;
    boolean expanded;
    Note note;
    boolean edited;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_detail);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        expanded = false;
        edited = false;
        Intent i = getIntent();
        note = (Note) i.getParcelableExtra(Constants.PARCEL_NOTE);
        txtTitle.setText(note.getTitle());
        txtContent.setText(note.getContent());
        DateFormat dateFormat = new SimpleDateFormat(Constants.FORMAT_DATETIME);
        String strDate = dateFormat.format(note.getCreatedDate());
        txtDate.setText(strDate);
    }

    @OnClick(R.id.fab_more)
    void onButtonMoreClicked(View view) {
        if (expanded) {
            fabDelete.setVisibility(View.GONE);
            fabEdit.setVisibility(View.GONE);
            expanded = false;
        } else {
            fabDelete.setVisibility(View.VISIBLE);
            fabEdit.setVisibility(View.VISIBLE);
            expanded = true;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent();
                if (edited) {
                    intent.putExtra(Constants.INTENT_NOTE_EDITED, true);
                    intent.putExtra(Constants.PARCEL_NOTE, note);
                }
                setResult(RESULT_CANCELED, intent);
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.fab_edit)
    void onButtonEditClicked(View view) {
        Intent i = new Intent(NoteDetailActivity.this, NoteEditActivity.class);
        i.putExtra(Constants.PARCEL_NOTE, note);
        i.putExtra(Constants.INTENT_EDIT_MODE, Constants.EDIT_MODE);
        startActivityForResult(i, Constants.RC_NOTE_EDIT);
    }

    @OnClick(R.id.fab_delete)
    void onButtonDeleteClicked(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete Note?");
        builder.setMessage("Do You Want To Delete Note?");
        builder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent i = new Intent();
                        i.putExtra(Constants.INTENT_NOTEID_DELETE, note.getId());
                        setResult(RESULT_OK, i);
                        finish();
                    }
                });
        builder.setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        builder.create().show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        int noteId;
        switch (requestCode) {
            case Constants.RC_NOTE_EDIT:
                //back from details
                if (resultCode == Activity.RESULT_OK) {
                    // return from save button
                    Note newNote = (Note) data.getParcelableExtra(Constants.PARCEL_NOTE);
                    note = newNote;
                    txtTitle.setText(note.getTitle());
                    txtContent.setText(note.getContent());
                }
                if (resultCode == Activity.RESULT_CANCELED) {
                    // return from cancel button
                }
                break;
        }
    }

}