package com.example.foobook_android.utility;

import android.content.Context;
import android.content.SharedPreferences;
import java.util.HashSet;
import java.util.Set;

// Utility class for managing user tokens and preferences.
public class TokenManager {

    // SharedPreferences keys
    private static final String PREF_NAME = "userDetails";
    private static final String TOKEN_KEY = "token";
    private static final String DISPLAY_NAME_KEY = "displayName";
    private static final String PROFILE_PIC_URL_KEY = "profilePicUrl";
    private static final String FRIEND_LIST_KEY = "friendList";
    private static final String USER_ID_KEY = "userId";
    private SharedPreferences sharedPreferences;

    public TokenManager(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }
    // Clears all data stored in SharedPreferences
    public void clearData() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear(); // This removes all data from SharedPreferences.
        editor.apply(); // Apply changes to commit them.
    }

    // Retrieves user token from SharedPreferences
    public String getToken() {
        return sharedPreferences.getString(TOKEN_KEY, null);
    }

    // Retrieves friend list from SharedPreferences
    public Set<String> getFriendList() {
        return sharedPreferences.getStringSet(FRIEND_LIST_KEY, new HashSet<>());
    }

    // Retrieves display name from SharedPreferences
    public String getDisplayName() {
        return sharedPreferences.getString(DISPLAY_NAME_KEY, "Unknown User");
    }

    // Retrieves profile picture URL from SharedPreferences
    public String getProfilePic() {
        return sharedPreferences.getString(PROFILE_PIC_URL_KEY, "default_profile_pic_url");
    }

    // Retrieves user ID from SharedPreferences
    public String getUserId() {
        return sharedPreferences.getString(USER_ID_KEY, "");
    }

    // Sets user token in SharedPreferences
    public void setToken(String token) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(TOKEN_KEY, token);
        editor.apply();
    }

    // Sets friend list in SharedPreferences
    public void setFriendList(Set<String> friendList) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putStringSet(FRIEND_LIST_KEY, friendList);
        editor.apply();
    }

    // Sets display name in SharedPreferences
    public void setDisplayName(String displayName) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(DISPLAY_NAME_KEY, displayName);
        editor.apply();
    }

    // Sets profile picture URL in SharedPreferences
    public void setProfilePic(String profilePicUrl) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(PROFILE_PIC_URL_KEY, profilePicUrl);
        editor.apply();
    }

    // Sets user ID in SharedPreferences
    public void setUserId(String userId) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(USER_ID_KEY, userId);
        editor.apply();
    }
}
