package com.example.foobook_android.post;

import androidx.room.TypeConverter;

import com.google.gson.Gson;

// Utilizes Gson for converting Creator objects to JSON and vice versa, facilitating their storage in Room.
public class Converters {
    // Converts a Creator object into a JSON string.
    @TypeConverter
    public static String fromCreatorToJson(Creator creator) {
        return new Gson().toJson(creator);
    }

    // Converts a JSON string back into a Creator object.
    @TypeConverter
    public static Creator fromJsonToCreator(String json) {
        return new Gson().fromJson(json, Creator.class);
    }
}