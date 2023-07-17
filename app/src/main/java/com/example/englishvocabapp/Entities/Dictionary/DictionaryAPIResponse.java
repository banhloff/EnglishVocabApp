package com.example.englishvocabapp.Entities.Dictionary;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DictionaryAPIResponse {
    private String word = "";
    private String phonetic = "";
    private List<Phonetics> phonetics = null;
    private String origin = "";
    private List<Meanings> meanings = null;
}
