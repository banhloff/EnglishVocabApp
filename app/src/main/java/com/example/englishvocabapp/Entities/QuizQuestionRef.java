package com.example.englishvocabapp.Entities;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity(primaryKeys = {"quiz_id", "question_id"}, tableName = "quiz_question",
        foreignKeys = {@ForeignKey(entity = Quiz.class,
                parentColumns = "id",
                childColumns = "quiz_id",
                onDelete = ForeignKey.CASCADE),
                @ForeignKey(entity = Question.class,
                        parentColumns = "id",
                        childColumns = "question_id",
                        onDelete = ForeignKey.CASCADE)})
public class QuizQuestionRef implements Parcelable {
    @ColumnInfo(name="quiz_id", index = true, defaultValue = "0")
    @NonNull
    private int quizId;
    @NonNull
    @ColumnInfo(name="question_id", index = true, defaultValue = "0")
    private int questionId;
    @NonNull
    @ColumnInfo(name="user_choice", defaultValue = "0")
    private int userChoice;

    protected QuizQuestionRef(Parcel in) {
        quizId = in.readInt();
        questionId = in.readInt();
        userChoice = in.readInt();
    }

    public static final Creator<QuizQuestionRef> CREATOR = new Creator<QuizQuestionRef>() {
        @Override
        public QuizQuestionRef createFromParcel(Parcel in) {
            return new QuizQuestionRef(in);
        }

        @Override
        public QuizQuestionRef[] newArray(int size) {
            return new QuizQuestionRef[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeInt(quizId);
        parcel.writeInt(questionId);
        parcel.writeInt(userChoice);
        // or writeList
    }
}
