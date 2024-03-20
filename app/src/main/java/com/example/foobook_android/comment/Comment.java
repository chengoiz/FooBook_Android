package com.example.foobook_android.comment;

public class Comment {
    // Fields to store the commenter's name, their comment text, and profile image URL.
    private String commenterName;
    private String commentText;
    private String commenterProfileImage;

    /**
     * Constructor for creating a new Comment object.
     *
     * @param commenterName The name of the commenter.
     * @param commentText The text of the comment.
     * @param commenterProfileImage The URL to the commenter's profile image.
     */
    public Comment(String commenterName, String commentText, String commenterProfileImage) {
        this.commenterName = commenterName;
        this.commentText = commentText;
        this.commenterProfileImage = commenterProfileImage;
    }

    // Getters and Setters for the Comment class properties.

    /**
     * Gets the name of the commenter.
     *
     * @return The name of the commenter.
     */
    public String getCommenterName() {
        return commenterName;
    }

    /**
     * Gets the URL of the commenter's profile image.
     *
     * @return The URL of the commenter's profile image.
     */
    public String getCommenterProfileImage() {
        return commenterProfileImage;
    }

    /**
     * Gets the comment text.
     *
     * @return The text of the comment.
     */
    public String getCommentText() {
        return commentText;
    }

    /**
     * Sets the name of the commenter.
     *
     * @param commenterName The new name of the commenter.
     */
    public void setCommenterName(String commenterName) {
        this.commenterName = commenterName;
    }

    /**
     * Sets the comment text.
     *
     * @param commentText The new text of the comment.
     */
    public void setCommentText(String commentText) {
        this.commentText = commentText;
    }

    /**
     * Sets the URL of the commenter's profile image.
     *
     * @param commenterProfileImage The new URL of the commenter's profile image.
     */
    public void setCommenterProfileImage(String commenterProfileImage) {
        this.commenterProfileImage = commenterProfileImage;
    }
}