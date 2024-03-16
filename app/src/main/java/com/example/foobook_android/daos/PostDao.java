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

@Dao
public interface PostDao {
    @Query("SELECT * FROM post ORDER BY timestamp DESC LIMIT 25")
    LiveData<List<Post>> getLatestPosts();
    @Query("SELECT * FROM post")
    List<Post> index();
    @Insert
    void insert(Post... posts);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Post> posts);

    @Query("DELETE FROM post")
    void deleteAll();
    @Update
    void update(Post... posts);


    @Delete
    void delete(Post... posts);
    @Query("DELETE FROM post WHERE postId = :postId")
    void deleteById(String postId);
    @Query("SELECT * FROM Post WHERE postId = :postId")
    LiveData<Post> getPostById(String postId);


    //TODO: Add methods to fetch posts as per requirements ------- need to create isFriend
//    @Query("SELECT * FROM post WHERE isFriend = 1 ORDER BY timestamp DESC LIMIT 20")
//    List<Post> getLatestFriendPosts();
//
//    @Query("SELECT * FROM post WHERE isFriend = 0 ORDER BY timestamp DESC LIMIT 5")
//    List<Post> getLatestNonFriendPosts();
}