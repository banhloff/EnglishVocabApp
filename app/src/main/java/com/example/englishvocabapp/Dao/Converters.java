package com.example.englishvocabapp.Dao;

import androidx.room.TypeConverter;

import java.util.Date;

public class Converters {
    // Date.getTime() return milliseconds
    // UnixTime stores as seconds
    @TypeConverter
    public static Date fromTimestamp(Long value) {
        return value == null ? null : new Date(value * 1000);
    }

    @TypeConverter
    public static Long dateToTimestamp(Date date) {
        return date == null ? null : ((Long)date.getTime() / 1000);
    }
}