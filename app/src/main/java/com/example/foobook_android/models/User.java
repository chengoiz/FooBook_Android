package com.example.foobook_android.models;

public class User {
    private String id; // Unique identifier
    private String username;
    private String displayname;
    private String profilepic;
    // You can also add friendsList and friendRequests if you plan to fetch those

    // Constructor
    public User(String id, String username, String displayname, String profilepic) {
        this.id = id;
        this.username = username;
        this.displayname = displayname;
        this.profilepic = profilepic;
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getDisplayname() { return displayname; }
    public void setDisplayname(String displayname) { this.displayname = displayname; }

    public String getProfilepic() { return profilepic; }
    public void setProfilepic(String profilepic) { this.profilepic = profilepic; }

    // Additional methods for friendsList and friendRequests can be added as needed
}
