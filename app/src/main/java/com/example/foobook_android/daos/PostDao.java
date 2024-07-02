package com.example.foobook_android.daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;
import com.example.foobook_android.post.Post;
import java.util.List;

// Defines the data access object (DAO) for posts. This interface outlines methods
// for interacting with the Post entities in the database.
@Dao
public interface PostDao {

    // Retrieves the latest 25 posts, ordered by timestamp in descending order.
    @Query("SELECT * FROM post ORDER BY timestamp DESC LIMIT 25")
    LiveData<List<Post>> getLatestPosts();

    // Inserts one or more posts into the database. Duplicates are ignored.
    @Insert
    void insert(Post... posts);

    // Inserts a list of posts into the database, replacing any existing entries with the same ID.
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Post> posts);

    // Deletes all posts from the database.
    @Query("DELETE FROM post")
    void deleteAll();

    // Updates one or more posts in the database.
    @Update
    void update(Post... posts);

    // Deletes one or more specific posts from the database.
    @Delete
    void delete(Post... posts);

    // Deletes a post by its ID.
    @Query("DELETE FROM post WHERE postId = :postId")
    void deleteById(String postId);

    // Retrieves a post by its ID.
    @Query("SELECT * FROM Post WHERE postId = :postId")
    LiveData<Post> getPostById(String postId);

}