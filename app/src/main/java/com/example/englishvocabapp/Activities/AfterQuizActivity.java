package com.example.englishvocabapp.Activities;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.englishvocabapp.Dao.AppDatabase;
import com.example.englishvocabapp.Entities.Note;
import com.example.englishvocabapp.Entities.Question;
import com.example.englishvocabapp.Entities.QuizQuestionRef;
import com.example.englishvocabapp.Entities.QuizResult;
import com.example.englishvocabapp.Entities.QuizWithQuestions;
import com.example.englishvocabapp.R;
import com.example.englishvocabapp.Utils.Constants;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AfterQuizActivity extends AppCompatActivity {

    @BindView(R.id.txt_score)
    TextView txtScore;
    @BindView(R.id.fab_export)
    FloatingActionButton fabExport;
    @BindView(R.id.btn_review)
    Button btnReview;
    double score;
    QuizWithQuestions quizWithQuestions;
    ArrayList<QuizQuestionRef> quizQuestionRefs;
    QuizResult quizResult;
    AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_after_quiz);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        quizWithQuestions = (QuizWithQuestions) intent.getParcelableExtra(Constants.PARCEL_QUIZ_QUESTIONS);
        quizQuestionRefs = intent.getParcelableArrayListExtra(Constants.PARCEL_QUIZ_QUESTION_REFS);
        int correctCount = 0;
        int incorrectCount = 0;
        int i = 0;
        for (Question question : quizWithQuestions.getQuestions()) {
            if (quizQuestionRefs.get(i).getUserChoice() == question.getCorrectChoice()) {
                correctCount++;
                i++;
                continue;
            }
            incorrectCount++;
            i++;
        }
        score = (double)correctCount / quizWithQuestions.getQuestions().size();
        txtScore.setText(Constants.twoDecimalFormat.format(score * 100) + "%");

        quizResult = new QuizResult();
        Date now = new Date();
        //score 0.0 -> 1.0
        quizResult.setScore(score);
        quizResult.setDate(now);
        quizResult.setCorrectCount(correctCount);
        quizResult.setIncorrectCount(incorrectCount);
        db = AppDatabase.getInMemoryDatabase(this);
        db.quizResultDao().insert(quizResult);
    }
    @OnClick(R.id.btn_review)
    void onButtonReviewClicked(View view) {
        Intent i = new Intent(AfterQuizActivity.this, QuizReviewActivity.class);
        //if implement both Serializable & Parcelable -- cast into 1 of them
        i.putExtra(Constants.PARCEL_QUIZ_QUESTIONS, quizWithQuestions);
        startActivity(i);
    }

    @OnClick(R.id.fab_export)
    void onButtonExportClicked() {
        Intent intent = new Intent(AfterQuizActivity.this, NoteEditActivity.class);
        Note newNote = new Note();
        newNote.setTitle("");
        StringBuilder str = new StringBuilder();
        int index = 0;
        for(Question question : quizWithQuestions.getQuestions()) {
            str.append(" Question ").append(index + 1).append(": ");
            str.append(question.getQuestion()).append("\n");
            str.append("Answer: ");
            if(question.getCorrectChoice() == 1) {
                str.append(question.getAnswer1());
            } else {
                str.append(question.getAnswer2());
            }
            str.append("\n");
            index++;
        }
        newNote.setContent(str.toString());
        Date now = new Date();
        newNote.setCreatedDate(now);
        //add quiz detail as content
        intent.putExtra(Constants.PARCEL_NOTE, newNote);
        intent.putExtra(Constants.INTENT_EDIT_MODE, Constants.ADD_MODE);
        startActivityForResult(intent, Constants.RC_NOTE_EXPORT);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        int noteId;
        switch (requestCode) {
            case Constants.RC_NOTE_EXPORT:
                //back from add
                if (resultCode == Activity.RESULT_OK) {
                    // export - save Note success
                    AlertDialog.Builder builder = new AlertDialog.Builder(AfterQuizActivity.this);
                    builder.setTitle("Export Success!");
                    builder.setNeutralButton("OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
                }
                if (resultCode == Activity.RESULT_CANCELED) {
                    //do nothing
                }
                break;
        }
    }
}