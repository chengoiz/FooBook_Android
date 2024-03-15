package com.example.foobook_android.Api;
import com.example.foobook_android.models.User;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FriendListResponse {
    public List<User> getFriends() {
        return friendList;
    }

    public void setFriendList(List<User> friendList) {
        this.friendList = friendList;
    }

    @SerializedName("friendList")
    private List<User> friendList;
}
