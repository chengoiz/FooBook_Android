package com.example.foobook_android.post;

import androidx.room.TypeConverter;

import com.google.gson.Gson;

public class Converters {
    @TypeConverter
    public static String fromCreatorToJson(Creator creator) {
        return new Gson().toJson(creator);
    }

    @TypeConverter
    public static Creator fromJsonToCreator(String json) {
        return new Gson().fromJson(json, Creator.class);
    }
}
