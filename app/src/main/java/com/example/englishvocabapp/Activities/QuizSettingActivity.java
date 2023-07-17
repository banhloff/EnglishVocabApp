package com.example.englishvocabapp.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.englishvocabapp.Dao.AppDatabase;
import com.example.englishvocabapp.Entities.Question;
import com.example.englishvocabapp.Entities.Quiz;
import com.example.englishvocabapp.Entities.QuizQuestionRef;
import com.example.englishvocabapp.Entities.QuizWithQuestions;
import com.example.englishvocabapp.R;
import com.example.englishvocabapp.Utils.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.BindArray;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class QuizSettingActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    @BindView(R.id.btn_startQuiz)
    Button btnStartQuiz;
    @BindView(R.id.spinner_level)
    Spinner spinnerLevel;
    @BindView(R.id.spinner_length)
    Spinner spinnerLength;

    @BindArray(R.array.quiz_level)
    String[] levels;
    @BindArray(R.array.quiz_length)
    String[] lengths;
    int pickedLevel;
    int pickedLength;
    AppDatabase db;
    QuizWithQuestions quizWithQuestions;
    ArrayList<QuizQuestionRef> quizQuestionRefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_setting);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        db = AppDatabase.getInMemoryDatabase(this);
        List<Quiz> quizzes = db.quizDao().getAll();

        if(quizzes.size() > 0 && quizzes != null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(QuizSettingActivity.this);
            builder.setTitle("Resume Quiz");
            builder.setMessage("Do You Want Resume previous Quiz?");
            builder.setPositiveButton("Yes",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Intent i = new Intent(QuizSettingActivity.this, QuizQuestionActivity.class);
                            //if implement both Serializable & Parcelable -- cast into 1 of them
                            //setup quizWithQuestions
                            Quiz quiz = quizzes.get(0);
                            List<Question> questions = db.questionDao().getQuestionsByQuizId(quiz.getId());
                            quizWithQuestions = new QuizWithQuestions();
                            quizWithQuestions.setQuiz(quiz);
                            quizWithQuestions.setQuestions(questions);
                            //setup quizQuestionRefs
                            quizQuestionRefs = new ArrayList<>(
                                    db.quizDao().getQuizQuestionRefByQuizId
                                            (quiz.getId()));

                            //delete quiz, quiz_question
                            db.quizDao().deleteAllQuiz();
                            db.quizDao().deleteAllQuizQuestion();
                            i.putExtra(Constants.PARCEL_QUIZ_QUESTIONS, quizWithQuestions);
                            i.putParcelableArrayListExtra(Constants.PARCEL_QUIZ_QUESTION_REFS, quizQuestionRefs);
                            startActivity(i);
                            finish();
                        }
                    });
            builder.setNegativeButton("No",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            db.quizDao().deleteAllQuiz();
                            db.quizDao().deleteAllQuizQuestion();
                            dialog.cancel();
                        }
                    });
            builder.create().show();
        }
        //Set adapter for spinner
        setAdapterForSpinner(spinnerLevel, levels);
        setAdapterForSpinner(spinnerLength, lengths);

    }

    void setAdapterForSpinner(Spinner spinner, String[] array) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>
                (this, android.R.layout.simple_spinner_item,
                        array);
        adapter.setDropDownViewResource(R.layout.spinner_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        switch (adapterView.getId()) {
            case R.id.spinner_level:
                Toast.makeText(this, "picked type: " + levels[i], Toast.LENGTH_SHORT).show();
                pickedLevel = i;
                break;
            case R.id.spinner_length:
                Toast.makeText(this, "picked type: " + lengths[i], Toast.LENGTH_SHORT).show();
                pickedLength = i;
                break;
        }
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
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @OnClick(R.id.btn_startQuiz)
    public void onButtonStartQuizClick(View view) {
        if (pickedLevel == 0) {
            Toast.makeText(this,"pick quiz level!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (pickedLength == 0) {
            Toast.makeText(this,"pick quiz length!", Toast.LENGTH_SHORT).show();
            return;
        }
        quizWithQuestions = new QuizWithQuestions();
        Quiz quiz = new Quiz();
        quizWithQuestions.setQuiz(quiz);
        int level = Integer.parseInt(levels[pickedLevel]);
        int length = Integer.parseInt(lengths[pickedLength]);
        quizWithQuestions.getQuiz().setLevel(level);

        //create quiz's questions based on settings
        List<Question> questions = new ArrayList<>();
        List<Long> ids = db.questionDao().getQuestionIdsByLevel(level);
        List<Integer> selectedIds = new ArrayList<>();
        if(ids.size() > length) {
            for (int i = 0; i < length; i++) {
                // get next random question id
                int rnd = new Random().nextInt(ids.size());
                Integer intId;
                try {
                    intId = ids.get(rnd) == null ? null : Math.toIntExact(ids.get(rnd));
                }catch (Exception e) {
                    intId = -1;
                }
                selectedIds.add(intId);
                // remove so it doesn't get selected again
                ids.remove(rnd);
            }
            // get questions with selected ids
            questions = db.questionDao().getQuestionsByIds(selectedIds);
        } else {
            // get all instead
            questions = db.questionDao().getQuestionsByLevel(level);
        }
        // set chosen questions to quizWithQuestions
        quizWithQuestions.setQuestions(questions);

        quizQuestionRefs = new ArrayList<>();
        for (Question question : questions) {
            quizQuestionRefs.add(new QuizQuestionRef(quiz.getId(), question.getId(), 0));
        }
        Intent intent = new Intent(QuizSettingActivity.this, QuizQuestionActivity.class);
        intent.putExtra(Constants.PARCEL_QUIZ_QUESTIONS, quizWithQuestions);
        intent.putParcelableArrayListExtra(Constants.PARCEL_QUIZ_QUESTION_REFS, quizQuestionRefs);

        startActivity(intent);
        finish();
    }

}