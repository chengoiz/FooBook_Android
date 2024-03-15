package com.example.foobook_android.post;

import com.google.gson.annotations.SerializedName;

public class Creator {
    @SerializedName("_id")
    private String id;

    @SerializedName("displayname")
    private String displayName;

    @SerializedName("profilepic")
    private String profilePic;

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    // Getters and setters
}
