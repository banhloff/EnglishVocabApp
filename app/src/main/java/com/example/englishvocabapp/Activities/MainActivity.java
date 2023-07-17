package com.example.englishvocabapp.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.example.englishvocabapp.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.btn_notes)
    LinearLayout btnNotes;
    @BindView(R.id.btn_quiz)
    LinearLayout btnQuiz;
    @BindView(R.id.btn_statistics)
    LinearLayout btnStatistics;
    @BindView(R.id.btn_dictionary)
    LinearLayout btnDictionary;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // bind the view using ButterKnife
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btn_notes)
    public void onButtonNotesClick(View view) {
        startActivity(new Intent(MainActivity.this, NoteListActivity.class));
    }
    @OnClick(R.id.btn_quiz)
    public void onButtonQuizClick(View view) {
        startActivity(new Intent(MainActivity.this, QuizSettingActivity.class));
    }
    @OnClick(R.id.btn_statistics)
    public void onButtonStatisticsClick(View view) {
        startActivity(new Intent(MainActivity.this, StatisticsActivity.class));
    }
    @OnClick(R.id.btn_dictionary)
    public void onButtonDictionaryClick(View view) {
        startActivity(new Intent(MainActivity.this, DictionaryActivity.class));
    }
}