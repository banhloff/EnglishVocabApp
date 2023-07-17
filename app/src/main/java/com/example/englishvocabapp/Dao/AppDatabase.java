package com.example.englishvocabapp.Dao;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.englishvocabapp.Entities.Note;
import com.example.englishvocabapp.Entities.Question;
import com.example.englishvocabapp.Entities.Quiz;
import com.example.englishvocabapp.Entities.QuizQuestionRef;
import com.example.englishvocabapp.Entities.QuizResult;

@Database(entities = {Note.class, Quiz.class,
        Question.class, QuizQuestionRef.class, QuizResult.class},
        version = AppDatabase.LATEST_VERSION,
        exportSchema = true)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {
    public static final String DATABASE_NAME = "EnglishVocabDB";
    public static final int LATEST_VERSION = 1;
    private static AppDatabase instance;

    public abstract NoteDao noteDao();
    public abstract QuestionDao questionDao();
    public abstract QuizDao quizDao();
    public abstract QuizResultDao quizResultDao();

    public static AppDatabase getInMemoryDatabase(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context, AppDatabase.class, DATABASE_NAME)
                	.createFromAsset("EnglishVocabDB.db")
			.fallbackToDestructiveMigration()
			.allowMainThreadQueries().build();
        }
        return instance;
    }

    public static void destroyInstance() {
        instance = null;
    }
}
