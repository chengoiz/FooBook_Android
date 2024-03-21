package com.example.foobook_android.utility;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UserDetails {
    // This tells Gson which JSON key to map to this field
    @SerializedName("displayname")
    private String displayname;
    @SerializedName("profilepic")
    private final String profilepic;
    @SerializedName("friendsList")
    private final List<String> friendsList;

    // Constructor, Getters, and Setters
    public UserDetails(String displayName, String profilePic, List<String> friendsList) {
        this.displayname = displayName;
        this.profilepic = profilePic;
        this.friendsList = friendsList;
    }

    // Getters
    public String getDisplayName() {
        return displayname;
    }

    public String getProfilePic() {
        return profilepic;
    }

    public List<String> getFriendList() {
        return friendsList;
    }

    // Setters
    public void setDisplayName(String displayName) {
        this.displayname = displayName;
    }

}