package com.example.foobook_android.utility;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UserDetails {
    @SerializedName("displayname") // This tells Gson which JSON key to map to this field
    private String displayname;
    @SerializedName("profilepic")
    private String profilepic;
    @SerializedName("friendsList")
    private List<String> friendsList;

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

    public void setProfilePicUrl(String profilePicUrl) {
        this.profilepic = profilePicUrl;
    }

    public void setFriendList(List<String> friendList) {
        this.friendsList = friendList;
    }

}