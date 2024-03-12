package com.example.foobook_android.utility;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UserDetails {
    @SerializedName("displayName") // This tells Gson which JSON key to map to this field
    private String name;
    @SerializedName("profilePic")
    private String imageUrl;

    // Constructor, Getters, and Setters
    public UserDetails(String name, String imageUrl) {
        this.name = name;
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfilePic() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}