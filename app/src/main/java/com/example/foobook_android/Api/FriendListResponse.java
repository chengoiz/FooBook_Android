package com.example.foobook_android.Api;
import com.example.foobook_android.models.User;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FriendListResponse {
    public FriendListResponse(List<User> friendList) {
        this.friendList = friendList;
    }

    public List<User> getFriends() {
        return friendList;
    }

    @SerializedName("friendList")
    private final List<User> friendList;

}
