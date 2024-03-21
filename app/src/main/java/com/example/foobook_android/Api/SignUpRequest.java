
package com.example.foobook_android.Api;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity
public class SignUpRequest implements Serializable {
    private String username;
    private String password;
    private String displayname;
    private String profilePic;

    @PrimaryKey(autoGenerate = true)
    private int id;
    public SignUpRequest(String username, String password, String displayname, String profilePic) {
        this.username = username;
        this.password = password;
        this.displayname = displayname;
        this.profilePic = profilePic;
    }

    public SignUpRequest(String username, String password, String displayname) {
        this.username = username;
        this.password = password;
        this.displayname = displayname;
        this.profilePic = "drawable/defaultpic.png";
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDisplayname() {
        return displayname;
    }

    public void setDisplayname(String displayname) {
        this.displayname = displayname;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
