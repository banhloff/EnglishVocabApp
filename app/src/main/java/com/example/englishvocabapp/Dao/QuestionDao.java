package com.example.englishvocabapp.Dao;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.englishvocabapp.Entities.Question;

import java.util.List;

@Dao
public interface QuestionDao {
    @Query("SELECT * FROM questions")
    List<Question> getAll();

    /**
     * Get Questions' ids by Level
     * @param level
     * @return
     */
    @Query("SELECT id FROM questions WHERE level=:level")
    List<Long> getQuestionIdsByLevel(int level);

    @Query("SELECT * FROM questions WHERE id IN(:ids)")
    List<Question> getQuestionsByIds(List<Integer> ids);

    /**
     * Get a number of Questions by level
     * @param level
     * @param number
     * @return
     */
    @Query("SELECT * FROM questions WHERE level=:level LIMIT :number")
    List<Question> getQuestionsByLevel(int level, int number);

    /**
     * Get all Questions by level
     * @param level
     * @return
     */
    @Query("SELECT * FROM questions WHERE level=:level")
    List<Question> getQuestionsByLevel(int level);

    /**
     * Add Note to db, can return void or long, long[]
     *
     * @param question
     * @return long id of inserted row
     */
    @Insert
    long insert(Question question);

    /**
     * Add Note to db, can return int or void
     *
     * @param question
     * @return int number of rows deleted
     */
    @Delete
    int delete(Question question);

    /**
     * Update Note to db, can return int or void
     *
     * @param question
     * @return int number of rows updated
     */
    @Update
    int update(Question question);

    /**
     * get questions in a saved quiz
     * @param quizId
     * @return
     */
    @Query("SELECT * FROM questions INNER JOIN quiz_question " +
            "ON questions.id = quiz_question.question_id " +
            "WHERE quiz_id = :quizId")
    public List<Question> getQuestionsByQuizId(int quizId);
}
