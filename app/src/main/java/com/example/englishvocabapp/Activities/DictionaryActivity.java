package com.example.englishvocabapp.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.englishvocabapp.Adapters.MeaningRecyclerAdapter;
import com.example.englishvocabapp.Adapters.PhoneticRecyclerAdapter;
import com.example.englishvocabapp.Entities.Dictionary.Definitions;
import com.example.englishvocabapp.Entities.Dictionary.DictionaryAPIResponse;
import com.example.englishvocabapp.Entities.Dictionary.Meanings;
import com.example.englishvocabapp.R;
import com.example.englishvocabapp.Retrofit.OnFetchDataListener;
import com.example.englishvocabapp.Retrofit.RequestManager;
import com.example.englishvocabapp.Utils.Constants;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DictionaryActivity extends AppCompatActivity implements RequestManager.AlertError {
    @BindView(R.id.txt_search)
    SearchView searchView;
    @BindView(R.id.txt_word)
    TextView txtWord;
    @BindView(R.id.recycler_phonetics)
    RecyclerView recyclerPhonetics;
    @BindView(R.id.recycler_meanings)
    RecyclerView recyclerMeanings;
    @BindView(R.id.layout_screen)
    RelativeLayout layoutScreen;
//    @BindView(R.id.fab_home)
//    FloatingActionButton fabHome;

    ProgressBar progressBar;
    PhoneticRecyclerAdapter phoneticRecyclerAdapter;
    MeaningRecyclerAdapter meaningRecyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dictionary);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //setup
        setUpScreen();


        // setup search view
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

//                getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
//                        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                if (query != null && !query.trim().isEmpty()) {
                    progressBar.setVisibility(View.VISIBLE);
                    RequestManager requestManager =
                            new RequestManager(getApplicationContext(), Constants.DICTIONARY_BASEURL, DictionaryActivity.this);
                    requestManager.getWordMeanings(listener, query);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        //search for word in case linking from QuizReviewActivity
        Intent i = getIntent();
        String word = i.getStringExtra(Constants.INTENT_WORD);
        if (word != null && !word.trim().isEmpty()) {
            progressBar.setVisibility(View.VISIBLE);
            RequestManager requestManager =
                    new RequestManager(getApplicationContext(), Constants.DICTIONARY_BASEURL, DictionaryActivity.this);
            requestManager.getWordMeanings(listener, word);
        }

    }

    private final OnFetchDataListener listener = new OnFetchDataListener() {
        @Override
        public void onFetchDictionaryData(DictionaryAPIResponse dictionaryAPIResponse, String message) {
//            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            progressBar.setVisibility(View.GONE);
            if (dictionaryAPIResponse == null) {
                Toast.makeText(DictionaryActivity.this, "Nothing Found!", Toast.LENGTH_SHORT).show();
                return;
            }
            showData(dictionaryAPIResponse);
        }

        @Override
        public void onError(String message) {
            progressBar.setVisibility(View.GONE);
            Toast.makeText(DictionaryActivity.this, message, Toast.LENGTH_SHORT).show();
        }
    };

    private void showData(DictionaryAPIResponse dictionaryAPIResponse) {
        txtWord.setText("Word: " + dictionaryAPIResponse.getWord());

        recyclerPhonetics.setHasFixedSize(true);
        recyclerPhonetics.setLayoutManager(new GridLayoutManager(this, 1));
        phoneticRecyclerAdapter = new PhoneticRecyclerAdapter(
                this, dictionaryAPIResponse.getPhonetics());
        recyclerPhonetics.setAdapter(phoneticRecyclerAdapter);

        recyclerMeanings.setHasFixedSize(true);
        recyclerMeanings.setLayoutManager(new GridLayoutManager(this, 1));
        meaningRecyclerAdapter = new MeaningRecyclerAdapter(
                this, dictionaryAPIResponse.getMeanings());
        recyclerMeanings.setAdapter(meaningRecyclerAdapter);
    }

    @Override
    public void showAlertError() {
        AlertDialog.Builder builder = new AlertDialog.Builder(DictionaryActivity.this);
        builder.setTitle("Error");
        builder.setMessage("Maybe Word doesn't exist!");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        builder.create().show();
    }

    void setUpScreen() {
        DictionaryAPIResponse dictionaryAPIResponse = new DictionaryAPIResponse();
        dictionaryAPIResponse.setPhonetics(new ArrayList<>());

        dictionaryAPIResponse.setMeanings(new ArrayList<>());
        Meanings meanings = new Meanings();
        meanings.setDefinitions(new ArrayList<>());
        Definitions definitions = new Definitions();
        definitions.setSynonyms(new ArrayList<>());
        definitions.setAntonyms(new ArrayList<>());
        dictionaryAPIResponse.getMeanings().add(meanings);

        recyclerPhonetics.setHasFixedSize(true);
        recyclerPhonetics.setLayoutManager(new GridLayoutManager(this, 1));
        phoneticRecyclerAdapter = new PhoneticRecyclerAdapter(
                this, dictionaryAPIResponse.getPhonetics());
        recyclerPhonetics.setAdapter(phoneticRecyclerAdapter);

        recyclerMeanings.setHasFixedSize(true);
        recyclerMeanings.setLayoutManager(new GridLayoutManager(this, 1));
        meaningRecyclerAdapter = new MeaningRecyclerAdapter(
                this, dictionaryAPIResponse.getMeanings());
        recyclerMeanings.setAdapter(meaningRecyclerAdapter);

        //progress bar
        progressBar = new ProgressBar(DictionaryActivity.this, null, android.R.attr.progressBarStyleSmall);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(100, 100);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        layoutScreen.addView(progressBar, params);
        progressBar.setVisibility(View.GONE);

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