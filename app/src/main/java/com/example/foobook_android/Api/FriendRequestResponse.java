package com.example.foobook_android.Api;

import com.example.foobook_android.models.User;
import com.google.gson.annotations.SerializedName;
import java.util.List;

// Handles the response for friend request queries. This class is used to deserialize
// JSON responses from the server that contain a list of friend requests.
public class FriendRequestResponse {

    // Serialized name annotation is used to map the JSON field "friendRequests" to this variable.
    @SerializedName("friendRequests")
    private List<User> friendRequests;

    // Returns the list of friend requests.
    // Each User object represents a single friend request.z
    public List<User> getFriendRequests() {
        return friendRequests;
    }

    // Sets the list of friend requests.
    // Used when updating the list with a new set of friend requests.
    public void setFriendRequests(List<User> friendRequests) {
        this.friendRequests = friendRequests;
    }
}
