package com.example.foobook_android.comment;

public class Comment {
    // Fields to store the commenter's name, their comment text, and profile image URL.
    private final String commenterName;
    private String commentText;
    private String commenterProfilePic;
    /**
     * Constructor for creating a new Comment object.
     *
     * @param commenterName The name of the commenter.
     * @param commentText The text of the comment.
     * @param commenterProfilePic The profile pic of the commenter.
     */
    public Comment(String commenterName, String commenterProfilePic, String commentText) {
        this.commenterName = commenterName;
        this.commenterProfilePic = commenterProfilePic;
        this.commentText = commentText;
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
     * Gets the comment text.
     *
     * @return The text of the comment.
     */
    public String getCommentText() {
        return commentText;
    }

    public String getCommenterProfilePic() {
        return commenterProfilePic;
    }

    /**
     * Sets the comment text.
     *
     * @param commentText The new text of the comment.
     */
    public void setCommentText(String commentText) {
        this.commentText = commentText;
    }
}