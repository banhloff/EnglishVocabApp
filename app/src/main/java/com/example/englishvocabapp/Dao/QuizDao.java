package com.example.englishvocabapp.Dao;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.englishvocabapp.Entities.Quiz;
import com.example.englishvocabapp.Entities.QuizQuestionRef;

import java.util.List;

@Dao
public interface QuizDao {
    @Query("SELECT * FROM quizzes")
    List<Quiz> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(Quiz quiz);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(QuizQuestionRef quizQuestionRef);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(List<QuizQuestionRef> quizQuestionRefs);

    @Query("SELECT * FROM quiz_question WHERE quiz_id = :quizId")
    List<QuizQuestionRef> getQuizQuestionRefByQuizId(int quizId);

    @Delete
    void delete(Quiz quiz);

    @Query("DELETE FROM quiz_question WHERE quiz_id = :quizId")
    void deleteQuizQuestionRefsByQuizId(int quizId);

    @Query("DELETE FROM quizzes")
    public void deleteAllQuiz();

    @Query("DELETE FROM quiz_question")
    public void deleteAllQuizQuestion();

    @Update
    void update(Quiz quiz);

}
