package com.example.foobook_android.Api;

import com.example.foobook_android.models.User;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FriendRequestResponse {
    public List<User> getFriendRequests() {
        return friendRequests;
    }

    public void setFriendRequests(List<User> friendRequests) {
        this.friendRequests = friendRequests;
    }

    @SerializedName("friendRequests")
    private List<User> friendRequests;
}
