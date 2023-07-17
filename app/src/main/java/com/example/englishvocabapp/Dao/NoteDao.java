package com.example.englishvocabapp.Dao;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.englishvocabapp.Entities.Note;

import java.util.List;

@Dao
public interface NoteDao {
    @Query("SELECT * FROM notes ORDER BY created_date DESC")
    List<Note> getAll();
    /**
     * Add Note to db, can return void or long, long[]
     * @param note
     * @return long id of inserted row
     */
    @Insert
    long insert(Note note);

    /**
     * Add Note to db, can return int or void
     * @param note
     * @return int number of rows deleted
     */
    @Delete
    int delete(Note note);

    /**
     * Update Note to db, can return int or void
     * @param note
     * @return int number of rows updated
     */
    @Update
    int update(Note note);
}
