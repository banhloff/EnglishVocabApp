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
public class Definitions {
    private String definition = "";
    private String example = "";
    private List<String> synonyms = null;
    private List<String> antonyms = null;
}
