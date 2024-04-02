package com.example.foobook_android.comment;

import android.content.Context;

import com.example.foobook_android.utility.TokenManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

// Manages a collection of comments for various posts. This class uses a static map
// to associate posts with their comments, enabling quick access and management of comments.
public class CommentsDataHolder {
    private static final HashMap<Integer, List<Comment>> commentsMap = new HashMap<>();

    // Adds a comment to the list associated with a specific post position.
    // If the post has no comments yet, initializes a new list.
    public static void addComment(int postPosition, Comment comment) {
        if (!commentsMap.containsKey(postPosition)) {
            commentsMap.put(postPosition, new ArrayList<>());
        }
        Objects.requireNonNull(commentsMap.get(postPosition)).add(comment);
    }

    // Retrieves the list of comments for a specific post position.
    // Returns an empty list if the post has no comments.
    public static List<Comment> getComments(int postPosition) {
        return commentsMap.getOrDefault(postPosition, new ArrayList<>());
    }

    // Removes a comment from a post based on the comment's position in the list.
    // Does nothing if the post or comment position does not exist.
    public static void removeComment(int postPosition, int commentPosition) {
        // Check if the post exists in the map
        if (commentsMap.containsKey(postPosition)) {
            List<Comment> comments = commentsMap.get(postPosition);
            // Check if the comment position is valid
            if (commentPosition >= 0 && commentPosition < Objects.requireNonNull(comments).size()) {
                comments.remove(commentPosition);

            }
        }
    }

    // Updates the name and profile picture for all comments across all posts.
    // This method iterates through all comments and updates them with new user details.
    public static void updateCommenterName(String newName, String newProfilePic, Context context) {
        TokenManager tokenManager = new TokenManager(context);
        String userDisplayName = tokenManager.getDisplayName();
        String userProfilePic = tokenManager.getProfilePic();
        for (List<Comment> comments : commentsMap.values()) {
            for (Comment comment : comments) {
                if (comment.getCommenterName().equals(userDisplayName)
                        && comment.getCommenterProfilePic().equals(userProfilePic)) {
                    comment.setCommenterName(newName);
                    comment.setCommenterProfilePic(newProfilePic);
                }
            }
        }
    }

    public static void deleteAllComments() {
        commentsMap.clear();
    }

    // Counts the number of comments associated with a specific post position.
    // Returns 0 if the post has no comments.
    public static int getCommentCount(int postPosition) {
        List<Comment> comments = commentsMap.getOrDefault(postPosition, new ArrayList<>());
        assert comments != null;
        return comments.size();
    }
}