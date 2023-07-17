package com.example.englishvocabapp.Entities;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.Embedded;
import androidx.room.Junction;
import androidx.room.Relation;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class QuizWithQuestions implements Parcelable {
    @Embedded
    private Quiz quiz;
    @Relation(
            parentColumn = "quiz_id",
            entityColumn = "question_id",
            associateBy = @Junction(QuizQuestionRef.class)
    )
    public List<Question> questions;

    protected QuizWithQuestions(Parcel in) {
        quiz = in.readParcelable(Quiz.class.getClassLoader());
        questions = new ArrayList<>();
        in.readTypedList(questions, Question.CREATOR);
    }

    public static final Creator<QuizWithQuestions> CREATOR = new Creator<QuizWithQuestions>() {
        @Override
        public QuizWithQuestions createFromParcel(Parcel in) {
            return new QuizWithQuestions(in);
        }

        @Override
        public QuizWithQuestions[] newArray(int size) {
            return new QuizWithQuestions[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeParcelable(quiz, i);
        // or writeList
        parcel.writeTypedList(questions);
    }
}
