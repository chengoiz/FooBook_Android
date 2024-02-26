package com.example.foobook_android.comment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CommentsDataHolder {
    private static final HashMap<Integer, List<Comment>> commentsMap = new HashMap<>();

    // Method to add a comment to a post
    public static void addComment(int postPosition, Comment comment) {
        if (!commentsMap.containsKey(postPosition)) {
            commentsMap.put(postPosition, new ArrayList<>());
        }
        commentsMap.get(postPosition).add(comment);
    }

    // Method to get comments for a post
    public static List<Comment> getComments(int postPosition) {
        return commentsMap.getOrDefault(postPosition, new ArrayList<>());
    }

    public static void removeComment(int postPosition, int commentPosition) {
        // Check if the post exists in the map
        if (commentsMap.containsKey(postPosition)) {
            List<Comment> comments = commentsMap.get(postPosition);
            // Check if the comment position is valid
            if (commentPosition >= 0 && commentPosition < comments.size()) {
                comments.remove(commentPosition);

            }
        }
    }

    // Method to get comment count
    public static int getCommentCount(int postPosition) {
        List<Comment> comments = commentsMap.getOrDefault(postPosition, new ArrayList<>());
        assert comments != null;
        return comments.size();
    }

    // Method to handle post remove
    public static void onPostDeleted(int deletedPostPosition) {
        // Remove the comments of the deleted post
        commentsMap.remove(deletedPostPosition);

        // Adjust the positions for remaining posts' comments
        HashMap<Integer, List<Comment>> updatedCommentsMap = new HashMap<>();
        commentsMap.forEach((postPosition, comments) -> {
            if (postPosition > deletedPostPosition) {
                updatedCommentsMap.put(postPosition - 1, comments);
            } else {
                updatedCommentsMap.put(postPosition, comments);
            }
        });

        // Apply the updated map
        commentsMap.clear();
        commentsMap.putAll(updatedCommentsMap);
    }
}