package com.example.foobook_android.daos;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.foobook_android.post.Post;

import java.util.List;

@Dao
public interface PostDao {
    @Query("SELECT * FROM post ORDER BY timestamp DESC LIMIT 25")
    List<Post> getLatestPosts();
    @Query("SELECT * FROM post")
    List<Post> index();

    @Query("SELECT * FROM post WHERE id = :id")
    Post get(int id);

    @Insert
    void insert(Post... posts);

    @Update
    void update(Post... posts);

    @Delete
    void delete(Post... posts);
    @Query("SELECT * FROM post WHERE id = :id")
    Post get(long id);

    // Add methods to fetch posts as per requirements ------- need to create isFriend
//    @Query("SELECT * FROM post WHERE isFriend = 1 ORDER BY timestamp DESC LIMIT 20")
//    List<Post> getLatestFriendPosts();
//
//    @Query("SELECT * FROM post WHERE isFriend = 0 ORDER BY timestamp DESC LIMIT 5")
//    List<Post> getLatestNonFriendPosts();
}