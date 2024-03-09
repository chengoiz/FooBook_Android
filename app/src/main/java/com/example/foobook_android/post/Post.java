package com.example.foobook_android.post;
import android.net.Uri;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.example.foobook_android.comment.Comment;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Post implements Serializable {
    public static final int PHOTO_PICKED = 1;
    public static final int NO_PHOTO = 0;
    public static final int JSON_FILE = 1;
    public static final int NOT_JSON_FILE = 0;
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private long id;

    @ColumnInfo(name = "userName")
    private String userName;

    @ColumnInfo(name = "timestamp")
    private String timestamp;

    @ColumnInfo(name = "content")
    private String content;

    @Ignore /** Ignoring this field for Room database **/
    private List<Comment> comments = new ArrayList<>();

    @ColumnInfo(name = "likesCount")
    private int likesCount;

    @ColumnInfo(name = "isLikedByCurrentUser")
    private boolean isLikedByCurrentUser;

    @ColumnInfo(name = "profileImage")
    private String profileImage;

    @ColumnInfo(name = "postImage")
    private String postImage;

    @ColumnInfo(name = "isImageSetByUser")
    private boolean isImageSetByUser;

    @ColumnInfo(name = "isPhotoPicked")
    private int isPhotoPicked;

    @ColumnInfo(name = "isJsonFile")
    private int isJsonFile;



    public Post(String userName, String timestamp, String content, String profileImage) {
        this.userName = userName;
        this.timestamp = timestamp;
        this.content = content;
        this.profileImage = profileImage;
        this.isJsonFile = 0;
        this.isPhotoPicked = NO_PHOTO;
    }
    public Post(String userName, String timestamp, String content,  String profileImage,
                String postImageUri) {
        this.userName = userName;
        this.timestamp = timestamp;
        this.content = content;
        this.profileImage = profileImage;
        this.postImage = postImageUri;
        this.isJsonFile = 0;
        this.isPhotoPicked = PHOTO_PICKED;
    }

    public boolean toggleLike() {
        isLikedByCurrentUser = !isLikedByCurrentUser;
        likesCount += isLikedByCurrentUser ? 1 : -1;
        return isLikedByCurrentUser;
    }

    // Getters
    public int getLikesCount() {
        return likesCount;
    }

    public boolean isLikedByCurrentUser() {
        return isLikedByCurrentUser;
    }
    public boolean isImageSetByUser() {
        return isImageSetByUser;
    }
    public int getIsPhotoPicked() {
        return isPhotoPicked;
    }

    public String getUserName() {
        return userName;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getContent() {
        return content;
    }


    public String getProfileImage() {
        return profileImage;
    }

    public String getPostImageUrl() {
        return postImage;
    }
    public Uri getPostImageUri(){
        return Uri.parse(postImage);
    }
    public String getPostImage(){
        return postImage;
    }
    public List<Comment> getComments() {
        return comments;
    }
    public int getIsJsonFile() {
        return this.isJsonFile;
    }

    // Setters
    public void setIsJsonFile(int isJsonFile) {
        this.isJsonFile = isJsonFile;
    }
    public void setIsPhotoPicked(int isPhotoPicked) {
        this.isPhotoPicked = isPhotoPicked;
    }

    public void setPostImage(String postImage) {
        this.postImage = postImage;
    }

    public void setImageSetByUser(boolean isImageSetByUser) {
        this.isImageSetByUser = isImageSetByUser;
    }



    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }
    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }
    public void setLikesCount(int likesCount) {
        this.likesCount = likesCount;
    }

    public void setIsLikedByCurrentUser(boolean isLikedByCurrentUser) {
        this.isLikedByCurrentUser = isLikedByCurrentUser;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
