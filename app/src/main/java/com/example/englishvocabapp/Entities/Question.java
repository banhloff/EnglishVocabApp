package com.example.englishvocabapp.Entities;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity(tableName = "questions"
//        foreignKeys = {@ForeignKey(entity = Quiz.class, //parent class
//                parentColumns = "id", // parent id columns
//                childColumns = "quiz_id", // ref id columns at child table
//                onDelete = ForeignKey.CASCADE),
//        } // , @ForeignKey(...),...
)
public class Question implements Parcelable {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name="id", defaultValue = "0")
    @NonNull
    private int id;
    @ColumnInfo(name="question")
    private String question;
    @ColumnInfo(name="first_answer")
    private String answer1;
    @ColumnInfo(name="second_answer")
    private String answer2;
    @NonNull
    @ColumnInfo(name="correct_choice", defaultValue = "0")
    private int correctChoice;
    @NonNull
    @ColumnInfo(name="level", defaultValue = "1")
    private int level;
    protected Question(Parcel in) {
        id = in.readInt();
        question = in.readString();
        answer1 = in.readString();
        answer2 = in.readString();
        correctChoice = in.readInt();
        level = in.readInt();
    }

    public static final Creator<Question> CREATOR = new Creator<Question>() {
        @Override
        public Question createFromParcel(Parcel in) {
            return new Question(in);
        }

        @Override
        public Question[] newArray(int size) {
            return new Question[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(question);
        parcel.writeString(answer1);
        parcel.writeString(answer2);
        parcel.writeInt(correctChoice);
        parcel.writeInt(level);
    }
}
