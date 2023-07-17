package com.example.englishvocabapp.Activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.englishvocabapp.Entities.Question;
import com.example.englishvocabapp.Entities.QuizWithQuestions;
import com.example.englishvocabapp.R;
import com.example.englishvocabapp.Utils.Constants;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnFocusChange;

public class QuizReviewActivity extends AppCompatActivity {
    @BindView(R.id.fab_prev)
    FloatingActionButton fabPrev;
    @BindView(R.id.fab_next)
    FloatingActionButton fabNext;
    @BindView(R.id.btn_answer1)
    Button btnFirstAnswer;
    @BindView(R.id.btn_answer2)
    Button btnSecondAnswer;
    @BindView(R.id.txt_questionNo)
    EditText txtQuestionNo;
    @BindView(R.id.txt_questionTotal)
    TextView txtQuestionTotal;
    @BindView(R.id.txt_questionContent)
    TextView txtQuestionContent;

    QuizWithQuestions quizWithQuestions;
    Question currentQuestion;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_review);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent i = getIntent();
        quizWithQuestions = (QuizWithQuestions) i.getParcelableExtra(Constants.PARCEL_QUIZ_QUESTIONS);
        txtQuestionTotal.setText(Integer.toString(quizWithQuestions.getQuestions().size()));
        //show 1st question
        currentQuestion = quizWithQuestions.getQuestions().get(0);
        changeToQuestion(0);
    }

    // lose focus when touch/click outside edittext
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int) event.getRawX(), (int) event.getRawY())) {
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent(event);
    }

    @OnFocusChange(R.id.txt_questionNo)
    void onFocusQuestionNoChange(boolean hasFocus) {
        if (!hasFocus) {
            String input = String.valueOf(txtQuestionNo.getText());
            if(input == null || input.trim().isEmpty()){
                changeToQuestion(0);
                return;
            }
            int newQuestionIndex = Integer.parseInt(input) - 1;
            // if input <= 0, move to first question
            if (newQuestionIndex < 0) {
                changeToQuestion(0);
                return;
            }
            if (newQuestionIndex >= quizWithQuestions.getQuestions().size()) {
                // else if input > size, move to last question
                newQuestionIndex = quizWithQuestions.getQuestions().size() - 1;
            }
            changeToQuestion(newQuestionIndex);
        }
    }

    void changeToQuestion(int newQuestionIndex) {
        txtQuestionNo.setText(Integer.toString(newQuestionIndex + 1));
        currentQuestion = quizWithQuestions.getQuestions().get(newQuestionIndex);
        txtQuestionContent.setText(currentQuestion.getQuestion());
        btnFirstAnswer.setText(currentQuestion.getAnswer1());
        btnSecondAnswer.setText(currentQuestion.getAnswer2());

        //set answer buttons' bg color
        switch (currentQuestion.getCorrectChoice()) {
            case 1:
                changeAnswerButtonColor(btnFirstAnswer, true);
                changeAnswerButtonColor(btnSecondAnswer, false);
                break;
            case 2:
                changeAnswerButtonColor(btnFirstAnswer, false);
                changeAnswerButtonColor(btnSecondAnswer, true);
                break;
            default:
                changeAnswerButtonColor(btnFirstAnswer, false);
                changeAnswerButtonColor(btnSecondAnswer, false);
                break;
        }
    }

    @OnClick({R.id.btn_answer1, R.id.btn_answer2})
    void onAnswerClicked(View view) {
        String word = ((Button)view).getText().toString();
        Intent i = new Intent(QuizReviewActivity.this, DictionaryActivity.class);
        i.putExtra(Constants.INTENT_WORD, word);
        startActivity(i);
    }

    @OnClick({R.id.fab_prev, R.id.fab_next})
    void onChangeQuestionButtonClicked(View view) {
        int questionIndex = Integer.parseInt(String.valueOf(txtQuestionNo.getText())) - 1;

        if (view.getId() == R.id.fab_prev) {
            // if first question
            if (questionIndex == 0) {
                return;
            }
            changeToQuestion(questionIndex - 1);
            return;
        }
        //if last question
        if (questionIndex == quizWithQuestions.getQuestions().size() - 1) {
            return;
        }
        changeToQuestion(questionIndex + 1);
    }

    void changeAnswerButtonColor(View button, boolean isCorrect) {

        if (!isCorrect) {
            button.setBackgroundTintList(
                    ContextCompat.getColorStateList(getApplicationContext(), R.color.purple_500));
            return;
        }
        button.setBackgroundTintList(
                ContextCompat.getColorStateList(getApplicationContext(), R.color.fab_bg));
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

    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }
}