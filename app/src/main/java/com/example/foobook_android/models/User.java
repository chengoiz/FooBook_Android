package com.example.foobook_android.models;

public class User {
    private String _id; // Unique identifier
    private String displayname;
    private String profilepic;

    // Constructor
    public User(String _id, String displayname, String profilepic) {
        this._id = _id;
        this.displayname = displayname;
        this.profilepic = profilepic;
    }

    // Getters and Setters
    public String getId() { return _id; }
    public void setId(String id) { this._id = id; }
    public String getDisplayname() { return displayname; }
    public void setDisplayname(String displayname) { this.displayname = displayname; }

    public String getProfilepic() { return profilepic; }
    public void setProfilepic(String profilepic) { this.profilepic = profilepic; }
}