package com.example.foobook_android.Api;

import com.google.gson.annotations.SerializedName;

// Represents the response received from toggling a like on a post. It includes the updated
// like count and the action performed ("liked" or "unliked").
public class ToggleLikeResponse {

    // The total number of likes after the action was performed.
    @SerializedName("likeCount")
    int likesCount;

    // The action that was performed, can be "liked" or "unliked".
    @SerializedName("action")
    String action;

    // Returns the current count of likes on the post.
    public int getLikesCount () {
        return likesCount;
    }

    // Returns the action that was performed during the toggle.
    public String getAction() {
        return action;
    }
}
