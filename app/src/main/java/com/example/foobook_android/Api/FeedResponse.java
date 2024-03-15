package com.example.foobook_android.Api;

import com.example.foobook_android.post.Post;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FeedResponse {
    @SerializedName("friendsPosts")
    private List<Post> friendsPosts;

    @SerializedName("nonFriendsPosts")
    private List<Post> nonFriendsPosts;

    // Getters and Setters
    public List<Post> getFriendsPosts() {
        return friendsPosts;
    }

    public void setFriendsPosts(List<Post> friendsPosts) {
        this.friendsPosts = friendsPosts;
    }

    public List<Post> getNonFriendsPosts() {
        return nonFriendsPosts;
    }

    public void setNonFriendsPosts(List<Post> nonFriendsPosts) {
        this.nonFriendsPosts = nonFriendsPosts;
    }
}
