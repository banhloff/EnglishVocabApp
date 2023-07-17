package com.example.englishvocabapp.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.englishvocabapp.Adapters.NoteRecyclerAdapter;
import com.example.englishvocabapp.Dao.AppDatabase;
import com.example.englishvocabapp.Entities.Note;
import com.example.englishvocabapp.R;
import com.example.englishvocabapp.Utils.Constants;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NoteListActivity extends AppCompatActivity implements NoteRecyclerAdapter.NoteItemClickListener {
    @BindView(R.id.rc_note)
    RecyclerView rcNotes;
    @BindView(R.id.fab_add)
    FloatingActionButton fabAdd;
    @BindView(R.id.txt_search)
    SearchView searchView;
    NoteRecyclerAdapter adapter;
    ArrayList<Note> notes;
    AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_list);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // data to populate the RecyclerView with
        db = AppDatabase.getInMemoryDatabase(this);
        notes = new ArrayList<>(db.noteDao().getAll());

        //-----------------
        // set up the RecyclerView
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rcNotes.setLayoutManager(layoutManager);
        adapter = new NoteRecyclerAdapter(this, notes);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(rcNotes.getContext(),
                layoutManager.getOrientation());
        rcNotes.addItemDecoration(dividerItemDecoration);
        adapter.setClickListener(this);
        rcNotes.setAdapter(adapter);

        // setup search view
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                adapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onItemClick(View view, int position) {
        Intent intent = new Intent(NoteListActivity.this, NoteDetailActivity.class);
        Note note = notes.get(position);
        //if implement both Serializable & Parcelable -- cast into 1 of them
        intent.putExtra(Constants.PARCEL_NOTE, note);
        startActivityForResult(intent, Constants.RC_NOTE_DETAILS);
    }

    @OnClick(R.id.fab_add)
    void onButtonAddClicked(View view) {
        Intent intent = new Intent(NoteListActivity.this, NoteEditActivity.class);
        Note newNote = new Note();
        newNote.setTitle("");
        Date now = new Date();
        newNote.setCreatedDate(now);
        intent.putExtra(Constants.PARCEL_NOTE, newNote);
        intent.putExtra(Constants.INTENT_EDIT_MODE, Constants.ADD_MODE);
        startActivityForResult(intent, Constants.RC_NOTE_ADD);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        int noteId;
        switch (requestCode) {
            case Constants.RC_NOTE_DETAILS:
                //back from details
                if (resultCode == Activity.RESULT_OK) {
                    // return from details with delete button
                    noteId = data.getIntExtra(Constants.INTENT_NOTEID_DELETE, -1);
                    for (Note note : notes) {
                        if (note.getId() == noteId) {
                            notes.remove(note);
                            break;
                        }
                    }
                    break;
                }
                if (resultCode == Activity.RESULT_CANCELED) {
                    // return from details with return button
                    boolean edited = data.getBooleanExtra(Constants.INTENT_NOTE_EDITED, false);
                    if (edited) {
                        Note note = (Note) data.getParcelableExtra(Constants.PARCEL_NOTE);
                        for (Note n : notes) {
                            if (n.getId() == note.getId()) {
                                n = note;
                                break;
                            }
                        }
                    }
                    break;
                }
                break;
            case Constants.RC_NOTE_ADD:
                //back from add
                if (resultCode == Activity.RESULT_OK) {
                    // return from save button
                    Note note = (Note) data.getParcelableExtra(Constants.PARCEL_NOTE);
                    notes.add(0, note);
                }
                if (resultCode == Activity.RESULT_CANCELED) {
                    // return from cancel button
                }
                break;
        }
        adapter.updateAll(notes);
        adapter.notifyDataSetChanged();
    }

}