package com.example.englishvocabapp.Retrofit;

import com.example.englishvocabapp.Entities.Dictionary.DictionaryAPIResponse;

public interface OnFetchDataListener {
    void onFetchDictionaryData(DictionaryAPIResponse dictionaryAPIResponse, String message);
    void onError(String message);
}
