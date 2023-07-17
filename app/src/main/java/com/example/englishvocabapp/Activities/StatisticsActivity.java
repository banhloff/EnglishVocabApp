package com.example.englishvocabapp.Activities;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.englishvocabapp.Dao.AppDatabase;
import com.example.englishvocabapp.Entities.QuizResult;
import com.example.englishvocabapp.R;
import com.example.englishvocabapp.Utils.Constants;
import com.example.englishvocabapp.Utils.CustomUtils;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class StatisticsActivity extends AppCompatActivity {
//    @BindView(R.id.fab_home)
//    FloatingActionButton fabHome;

    @BindView(R.id.date_pick)
    LinearLayout datePick;
    @BindView(R.id.txt_month)
    TextView txtMonth;
    @BindView(R.id.txt_year)
    TextView txtYear;
    @BindView(R.id.txt_noOfQuiz)
    TextView txtNoOfQuiz;
    @BindView(R.id.txt_correctCount)
    TextView txtCorrect;
    @BindView(R.id.txt_incorrectCount)
    TextView txtIncorrect;

    int pickedMonth;
    int pickedYear;
    Calendar calendar;
    BarChart chart;
    AppDatabase db;
    boolean isDateChanged;
    ArrayList<QuizResult> quizResults;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        db = AppDatabase.getInMemoryDatabase(this);
        chart = findViewById(R.id.barchart);
        quizResults = new ArrayList<>();

        //setup date pick
        Date now = new Date();
        calendar = Calendar.getInstance();
        calendar.setTime(now);
        pickedYear = calendar.get(Calendar.YEAR);
        // Calendar.MONTH 0 -> 11
        pickedMonth = calendar.get(Calendar.MONTH);

        txtMonth.setText(Constants.MONTH_NAMES[pickedMonth]);
        txtYear.setText(String.valueOf(pickedYear));

        //fetch data
        quizResults = getData(now);
        //populate txt views
        populateTextViews();

        //bar chart data set
        BarDataSet barDataSet = getScoreChartDataSet();
        chart.setData(new BarData(barDataSet));
        //bar chart appearance
        chart.getAxisLeft().setAxisMaximum(1);
        chart.getAxisLeft().setAxisMinimum(0);
        chart.getAxisRight().setAxisMaximum(1);
        chart.getAxisRight().setAxisMinimum(0);
        chart.animateY(2000);
        chart.getDescription().setText("");
    }

    void populateTextViews() {
        txtNoOfQuiz.setText(String.valueOf(quizResults.size()));
        int correctCount = 0;
        int incorrectCount = 0;
        for(QuizResult result : quizResults) {
            correctCount += result.getCorrectCount();
            incorrectCount += result.getIncorrectCount();
        }
        txtCorrect.setText(String.valueOf(correctCount));
        txtIncorrect.setText(String.valueOf(incorrectCount));
    }

    /**
     * get quiz results data from db
     * @param date
     * @return
     */
    ArrayList<QuizResult> getData(Date date) {
        Pair<Date, Date> range = CustomUtils.getMonthRangeByDate(date);
        Date begin = range.first;
        Date end = range.second;

        ArrayList<QuizResult> quizResults = new ArrayList<>(
                db.quizResultDao().getAllInRange(
                        CustomUtils.getUnixTimeStamp(begin), CustomUtils.getUnixTimeStamp(end)));
        return quizResults;
    }

    /**
     * Get new Dataset for bar chart
     * @return
     */
    private BarDataSet getScoreChartDataSet() {

        //plot data points
        List<BarEntry> scoreEntries = new ArrayList<>();
        //Calendar.DAY_OF_MONTH 1 -> ...
        int lastDayOfMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        ArrayList days = new ArrayList<>();
        for(int dayIndex = 0; dayIndex < lastDayOfMonth; dayIndex++) {
            boolean hasData = false;
            days.add(dayIndex + 1);
            for (QuizResult data : quizResults) {
                calendar.setTime(data.getDate());
                int quizDay = calendar.get(Calendar.DAY_OF_MONTH) - 1;
                if(quizDay == dayIndex) {
                    // x value - day, y value - score
                    scoreEntries.add(new BarEntry(dayIndex + 1, (float) data.getScore()));
                    hasData = true;
                    break;
                }
            }
            if(!hasData) {
                scoreEntries.add(new BarEntry(dayIndex + 1, 0f));
            }
        }
        BarDataSet barDataSet = new BarDataSet(scoreEntries, "Scores");
        barDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        barDataSet.setDrawValues(false);
        return barDataSet;
    }

    /**
     * open custom MonthPicker Dialog
     * @param view
     */
    @OnClick(R.id.date_pick)
    void onDatePickClicked(View view) {

        AlertDialog.Builder builder = new AlertDialog.Builder(StatisticsActivity.this);
        setDatePickDialog(builder);
        builder.create().show();
    }

    /**
     * set custom MonthPicker Dialog
     * @return
     */
    void setDatePickDialog(AlertDialog.Builder builder) {
        isDateChanged = false;
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.custom_date_picker, null);
        builder.setTitle("Pick A Month");
        builder.setView(dialogView);
        NumberPicker monthPicker = (NumberPicker) dialogView.findViewById(R.id.picker_month);
        monthPicker.setMinValue(0);
        monthPicker.setMaxValue(Constants.MONTH_NAMES.length -1);
        monthPicker.setValue(pickedMonth);
        NumberPicker yearPicker = (NumberPicker) dialogView.findViewById(R.id.picker_year);
        yearPicker.setMaxValue(calendar.get(Calendar.YEAR));
        yearPicker.setMinValue(calendar.get(Calendar.YEAR) - 5);
        yearPicker.setValue(pickedYear);
        monthPicker.setDisplayedValues(Constants.MONTH_NAMES);

        //picker wrap around or not?
        monthPicker.setWrapSelectorWheel(true);
        yearPicker.setWrapSelectorWheel(false);

        monthPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int oldVal, int newVal) {
                isDateChanged = true;
            }
        });
        yearPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int oldVal, int newVal) {
                isDateChanged = true;
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                dialog.cancel();
            }
        });
        builder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                if(!isDateChanged) {
                    return;
                }
                //update date
                pickedMonth = monthPicker.getValue();
                pickedYear = yearPicker.getValue();
                txtMonth.setText(Constants.MONTH_NAMES[pickedMonth]);
                txtYear.setText(String.valueOf(pickedYear));
                calendar.set(Calendar.YEAR, pickedYear);
                calendar.set(Calendar.MONTH, pickedMonth);
                CustomUtils.setTimeToEndOfDay(calendar);
                //fetch data
                quizResults = getData(calendar.getTime());
                //update txt views
                populateTextViews();
                //update bar dataset
                BarDataSet barDataSet = getScoreChartDataSet();
                chart.setData(new BarData(barDataSet));

                chart.notifyDataSetChanged();
                chart.invalidate();
                isDateChanged = false;
            }
        });
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

//    @OnClick(R.id.fab_home)
//    public void onButtonHomeClicked() {
//        finish();
//    }
}