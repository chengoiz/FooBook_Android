package com.example.foobook_android.models;

// Represents a user in the system with an ID, display name, and profile picture URL.
public class User {
    private String _id; // Unique identifier for the user.
    private String displayname; // The display name of the user.
    private final String profilepic; // URL or path to the user's profile picture.

    // Constructs a new User with the given ID, display name, and profile picture.
    public User(String _id, String displayname, String profilepic) {
        this._id = _id;
        this.displayname = displayname;
        this.profilepic = profilepic;
    }

    // Getters and setters for the user's properties.
    public String getId() { return _id; }
    public void setId(String id) { this._id = id; }
    public String getDisplayName() { return displayname; }
    public void setDisplayName(String displayname) { this.displayname = displayname; }
    public String getProfilePic() { return profilepic; }
}
