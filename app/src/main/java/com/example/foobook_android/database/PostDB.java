package com.example.foobook_android.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.foobook_android.daos.PostDao;
import com.example.foobook_android.post.Converters;
import com.example.foobook_android.post.Post;


// Defines the Room database for the application. It includes the Post entity
// and uses Converters for data types that Room doesn't support natively.
@Database(entities = {Post.class}, version = 13)
@TypeConverters({Converters.class})
public abstract class PostDB extends RoomDatabase {
    private static volatile PostDB INSTANCE;
    private static final String DATABASE_NAME = "post_database";

    public abstract PostDao postDao();

    // Implements the singleton pattern to ensure only one instance of the database is created.
    public static PostDB getInstance(final Context context) {
        if (INSTANCE == null) {
            synchronized (PostDB.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            PostDB.class, DATABASE_NAME).fallbackToDestructiveMigration().build();
                }
            }
        }
        return INSTANCE;
    }
}