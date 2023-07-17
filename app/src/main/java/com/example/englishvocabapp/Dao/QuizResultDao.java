package com.example.englishvocabapp.Dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.englishvocabapp.Entities.QuizResult;

import java.util.List;

@Dao
public interface QuizResultDao {
    @Query("SELECT * FROM quiz_result ORDER BY date ASC")
    List<QuizResult> getAll();

    @Query("SELECT * FROM quiz_result WHERE date BETWEEN :begin AND :end ORDER BY date ASC")
    List<QuizResult> getAllInRange(Long begin, Long end);

    @Insert
    void insert(QuizResult quizResult);

    @Delete
    void delete(QuizResult quizResult);

    @Update
    void update(QuizResult quizResult);
}
