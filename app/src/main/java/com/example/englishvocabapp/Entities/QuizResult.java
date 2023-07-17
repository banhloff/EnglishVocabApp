package com.example.englishvocabapp.Entities;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity(tableName="quiz_result")
public class QuizResult implements Parcelable {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name="id", defaultValue = "0")
    private int id;
    @ColumnInfo(name="score", defaultValue = "0.0")
    private double score;
    @ColumnInfo(name="date", defaultValue = "0")
    private Date date;
    @NonNull
    @ColumnInfo(name = "correct_count")
    private int correctCount;
    @NonNull
    @ColumnInfo(name = "incorrect_count")
    private int incorrectCount;

    protected QuizResult(Parcel in) {
        id = in.readInt();
        score = in.readDouble();
        date = new Date(in.readLong());
        correctCount = in.readInt();
        incorrectCount = in.readInt();
    }

    public static final Creator<Quiz> CREATOR = new Creator<Quiz>() {
        @Override
        public Quiz createFromParcel(Parcel in) {
            return new Quiz(in);
        }

        @Override
        public Quiz[] newArray(int size) {
            return new Quiz[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeDouble(score);
        parcel.writeLong(date.getTime());
        parcel.writeInt(correctCount);
        parcel.writeInt(incorrectCount);

    }
}
