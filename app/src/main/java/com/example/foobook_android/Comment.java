package com.example.foobook_android;

public class Comment {
    private String commenterName;
    private String commentText;
    private String commenterProfileImage;


    // Constructor
    public Comment(String commenterName, String commentText, String commenterProfileImage) {
        this.commenterName = commenterName;
        this.commentText = commentText;
        this.commenterProfileImage = commenterProfileImage;
    }
    // Getters
    public String getCommenterName() {
        return commenterName;
    }

    public String getCommenterProfileImage() {
        return commenterProfileImage;
    }
    public String getCommentText() {
        return commentText;
    }

    // Setters
    public void setCommenterName(String commenterName) {
        this.commenterName = commenterName;
    }

    public void setCommentText(String commentText) {
        this.commentText = commentText;
    }

    public void setCommenterProfileImage(String commenterProfileImage) {
        this.commenterProfileImage = commenterProfileImage;
    }

}