package com.example.foobook_android.Api;

import com.google.gson.annotations.SerializedName;

public class ToggleLikeResponse {

    @SerializedName("likeCount")
    int likesCount;
    @SerializedName("action")
    String action;

    public int getLikesCount () {
        return likesCount;
    }
    public String getAction() {
        return action;
    }
}
