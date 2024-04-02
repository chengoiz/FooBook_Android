
package com.example.foobook_android.Api;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import java.io.Serializable;


// Defines the data structure for a sign-up request. This class is marked as an Entity
// to be stored in a Room database, making it part of your app's local data model.
@Entity
public class SignUpRequest implements Serializable {
    private String username; // The desired username for the new account.
    private String password; // The password for the new account.
    private String displayname; // The display name for the new account.
    private String profilePic; // Path to the profile picture for the new account.

    // Primary key for the entity in the Room database. Auto-generate this value.
    @PrimaryKey(autoGenerate = true)
    private int id;
    // Main constructor used when all properties including profile picture are provided.
    public SignUpRequest(String username, String password, String displayname, String profilePic) {
        this.username = username;
        this.password = password;
        this.displayname = displayname;
        this.profilePic = profilePic;
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
