package com.example.englishvocabapp.Utils;

import android.util.Pair;

import java.util.Calendar;
import java.util.Date;

public class CustomUtils {
    // Date.getTime() return milliseconds
    // UnixTime stores as seconds
    public static Long getUnixTimeStamp(Date date) {
        return ((Long) date.getTime() / 1000);
    }

    public static Pair<Date, Date> getMonthRangeByDate(Date date) {
        Date beginning, end;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        calendar.set(Calendar.DAY_OF_MONTH,
                calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
        setTimeToBeginningOfDay(calendar);
        beginning = calendar.getTime();

        calendar.set(Calendar.DAY_OF_MONTH,
                calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        setTimeToEndOfDay(calendar);
        end = calendar.getTime();


        return new Pair<>(beginning, end);
    }

    public static void setTimeToBeginningOfDay(Calendar calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
    }

    public static void setTimeToEndOfDay(Calendar calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
    }
}
