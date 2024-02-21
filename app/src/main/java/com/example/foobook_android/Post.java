package com.example.foobook_android;

import org.w3c.dom.Comment;

import java.util.ArrayList;
import java.util.List;

public class Post {
    private String userName;
    private String timestamp;
    private String content;
    private String profileImage;
    private String postImage;
    private List<Comment> comments = new ArrayList<>();
    private int likesCount;
    private boolean isLikedByCurrentUser;


    public Post(String userName, String timestamp, String content, String profileImage) {
        this.userName = userName;
        this.timestamp = timestamp;
        this.content = content;
        this.profileImage = profileImage;
    }
    public boolean toggleLike() {
        // Toggle the like state
        isLikedByCurrentUser = !isLikedByCurrentUser;

        // Update likes count based on the new state
        if (isLikedByCurrentUser) {
            likesCount++;
        } else {
            likesCount--;
        }

        // Return the new like state
        return isLikedByCurrentUser;
    }


    public int getLikesCount() {
        return likesCount;
    }

    public boolean isLikedByCurrentUser() {
        return isLikedByCurrentUser;
    }

    // Getters
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

    public String getPostImage() {
        return postImage;
    }
    public List<Comment> getComments() {
        return comments;
    }

    // Setters
    public void setPostImage(String postImage) {
        this.postImage = postImage;
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
}
