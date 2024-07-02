package com.example.foobook_android.Api;

import com.example.foobook_android.post.Post;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FeedResponse {
    @SerializedName("friendsPosts")
    private final List<Post> friendsPosts;

    @SerializedName("nonFriendsPosts")
    private final List<Post> nonFriendsPosts;

    public FeedResponse(List<Post> friendsPosts, List<Post> nonFriendsPosts) {
        this.friendsPosts = friendsPosts;
        this.nonFriendsPosts = nonFriendsPosts;
    }

    // Getters and Setters
    public List<Post> getFriendsPosts() {
        return friendsPosts;
    }

    public List<Post> getNonFriendsPosts() {
        return nonFriendsPosts;
    }

}
