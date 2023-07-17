package com.example.englishvocabapp.Utils;

import java.text.DecimalFormat;

public class Constants {
    //datetime format
    public static final String FORMAT_DATETIME = "yyyy/MM/dd HH:mm:ss";
    public static final String FORMAT_DATE = "yyyy/MM/dd";

    public static final String[] MONTH_NAMES = {"January", "February", "March", "April", "May", "June",
            "July", "August", "September", "October", "November", "December"};
    //parcel name
    public static final String PARCEL_QUIZ_QUESTIONS = "quizWithQuestions";
    public static final String PARCEL_NOTE = "note";
    public static final String PARCEL_DATE = "date";
    public static final String PARCEL_QUIZ_QUESTION_REFS="quizQuestionRefs";
    //intent extra name
    public static final String INTENT_EDIT_MODE = "Mode";
    public static final String INTENT_NOTE_TITLE = "title";
    public static final String INTENT_NOTE_CONTENT = "content";
    public static final String INTENT_NOTEID_DELETE = "delete";
    public static final String INTENT_NOTE_EDITED = "edited";
    public static final String INTENT_WORD = "word";

    //edit mode
    public static final String ADD_MODE = "Add";
    public static final String EDIT_MODE = "Edit";

    public static final int NOTE_TITLE_LENGTH = 10;
    public static final DecimalFormat twoDecimalFormat = new DecimalFormat("#.00");

    //requestCode
    public static final int RC_NOTE_DETAILS = 1000;
    public static final int RC_NOTE_EDIT = 1001;
    public static final int RC_NOTE_ADD = 1002;
    public static final int RC_NOTE_EXPORT = 1003;

    //api baseUrl
    public static final String DICTIONARY_BASEURL = "https://api.dictionaryapi.dev/api/v2/";
}
