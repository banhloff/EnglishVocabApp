package com.example.englishvocabapp.Activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.englishvocabapp.Dao.AppDatabase;
import com.example.englishvocabapp.Entities.Question;
import com.example.englishvocabapp.Entities.Quiz;
import com.example.englishvocabapp.Entities.QuizQuestionRef;
import com.example.englishvocabapp.Entities.QuizWithQuestions;
import com.example.englishvocabapp.R;
import com.example.englishvocabapp.Utils.Constants;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnFocusChange;

public class QuizQuestionActivity extends AppCompatActivity {
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
    AppDatabase db;
    Quiz quiz;
    ArrayList<QuizQuestionRef> quizQuestionRefs;
    QuizWithQuestions quizWithQuestions;
    Question currentQuestion;
    QuizQuestionRef currentRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_question);
        ButterKnife.bind(this);

        //retrieve settings
        Intent intent = getIntent();
        quizWithQuestions = (QuizWithQuestions) intent.getParcelableExtra(Constants.PARCEL_QUIZ_QUESTIONS);

        quizQuestionRefs = intent.getParcelableArrayListExtra(Constants.PARCEL_QUIZ_QUESTION_REFS);
        quiz = quizWithQuestions.getQuiz();

        txtQuestionTotal.setText(Integer.toString(quizWithQuestions.getQuestions().size()));
        //show 1st question
        currentQuestion = quizWithQuestions.getQuestions().get(0);
        changeToQuestion(0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_quiz, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        switch (item.getItemId()) {
            case R.id.menu_finish:
                setFinishDialog(builder);
                builder.create().show();
                break;
            case R.id.menu_exit:
                setExitDialog(builder);
                builder.create().show();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    void setFinishDialog(AlertDialog.Builder builder) {
        builder.setTitle("Finish Quiz");
        builder.setMessage("Do You Want To Finish Quiz?");
        builder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent i = new Intent(QuizQuestionActivity.this, AfterQuizActivity.class);
                        //if implement both Serializable & Parcelable -- cast into 1 of them
                        i.putExtra(Constants.PARCEL_QUIZ_QUESTIONS, quizWithQuestions);
                        i.putParcelableArrayListExtra(Constants.PARCEL_QUIZ_QUESTION_REFS, quizQuestionRefs);
                        startActivity(i);
                        finish();
                    }
                });
        builder.setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
    }

    void setExitDialog(AlertDialog.Builder builder) {
        builder.setTitle("Save Quiz");
        builder.setMessage("Do You Want To Resume Quiz Later?");
        builder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        db = AppDatabase.getInMemoryDatabase(QuizQuestionActivity.this);
                        long quizId = db.quizDao().insert(quiz);
                        for(QuizQuestionRef ref : quizQuestionRefs) {
                            ref.setQuizId((int) quizId);
                        }
                        db.quizDao().insert(quizQuestionRefs);
                        finish();
                    }
                });
        builder.setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                    }
                });

        builder.setNeutralButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
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
            if (input == null || input.trim().isEmpty()) {
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
        //get question content
        currentQuestion = quizWithQuestions.getQuestions().get(newQuestionIndex);

        txtQuestionContent.setText(currentQuestion.getQuestion());
        btnFirstAnswer.setText(currentQuestion.getAnswer1());
        btnSecondAnswer.setText(currentQuestion.getAnswer2());
        //get user choice
        currentRef = quizQuestionRefs.get(newQuestionIndex);
        //set answer buttons' bg color based on user choice
        switch (currentRef.getUserChoice()) {
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
        int questionIndex = quizWithQuestions.getQuestions().indexOf(currentQuestion);
        changeAnswerButtonColor(view, true);
        if (view.getId() == R.id.btn_answer1) {
            currentRef.setUserChoice(1);
            changeAnswerButtonColor(btnSecondAnswer, false);
        } else {
            currentRef.setUserChoice(2);
            changeAnswerButtonColor(btnFirstAnswer, false);
        }
        // if last question
        if (questionIndex == quizWithQuestions.getQuestions().size() - 1) {
            return;
        }
        // show next question
        questionIndex++;
        changeToQuestion(questionIndex);
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

    void changeAnswerButtonColor(View button, boolean picked) {

        if (!picked) {
            ((Button) button).setTextColor(
                    ContextCompat.getColor(getApplicationContext(), R.color.purple_500));
            button.setBackgroundTintList(
                    ContextCompat.getColorStateList(getApplicationContext(), R.color.white));
            return;
        }
        ((Button) button).setTextColor(
                ContextCompat.getColor(getApplicationContext(), R.color.white));
        button.setBackgroundTintList(
                ContextCompat.getColorStateList(getApplicationContext(), R.color.purple_500));
    }
}