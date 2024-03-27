package com.example.foobook_android.post;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

@Entity
public class Post implements Serializable {
    public static final int PHOTO_PICKED = 1;
    public static final int NO_PHOTO = 0;

    @NonNull
    @PrimaryKey
    @SerializedName("_id")
    private String postId;
    @ColumnInfo(name = "userName")
    private final String userName;

    @ColumnInfo(name = "timestamp")
    private final String timestamp;

    @ColumnInfo(name = "text")
    private String text;

    @ColumnInfo(name = "likeCount")
    @SerializedName("likeCount")
    private int likesCount;

    @ColumnInfo(name = "userLiked")
    @SerializedName("userLiked")
    private boolean userLiked;

    @SerializedName("canEdit")
    private boolean canEdit;

    @ColumnInfo(name = "profileImage")
    private final String profileImage;

    @ColumnInfo(name = "imageUrl")
    @SerializedName("imageUrl")
    private String imageUrl;

    @ColumnInfo(name = "isImageSetByUser")
    private boolean isImageSetByUser;

    @ColumnInfo(name = "isPhotoPicked")
    private int isPhotoPicked;

    @ColumnInfo(name = "isJsonFile")
    private int isJsonFile;

    @SerializedName("createdBy")
    private Creator creator;

    @ColumnInfo(name ="createdAt")
    private String createdAt;
    @ColumnInfo(name ="updatedBy")
    private String updatedAt;


    public Post(String userName, String timestamp, String text, String profileImage) {
        this.userName = userName;
        this.timestamp = timestamp;
        this.text = text;
        this.profileImage = profileImage;
        this.isJsonFile = 0;
        this.isPhotoPicked = NO_PHOTO;
        this.creator = null;
        this.userLiked = false;
    }
    public Post(String userName, String timestamp, String text,  String profileImage,
                String postImageUri) {
        this.userName = userName;
        this.timestamp = timestamp;
        this.text = text;
        this.profileImage = profileImage;
        this.imageUrl = postImageUri;
        this.isJsonFile = 0;
        this.isPhotoPicked = PHOTO_PICKED;
    }

    public boolean toggleLike() {
        userLiked = !userLiked;
        likesCount += userLiked ? 1 : -1;
        return userLiked;
    }

    // Getters
    public int getLikesCount() {
        return likesCount;
    }
    public boolean getUserLiked() {
        return userLiked;
    }
    public boolean isImageSetByUser() {
        return isImageSetByUser;
    }
    public int getIsPhotoPicked() {
        return isPhotoPicked;
    }
    public String getUserName() {
        return userName;
    }
    public String getTimestamp() {
        return timestamp;
    }
    public String getText() {
        return text;
    }
    public String getProfileImage() {
        return profileImage;
    }
    public String getImageUrl() {
        return imageUrl;
    }
    public int getIsJsonFile() {
        return this.isJsonFile;
    }

    // Setters
    public void setIsJsonFile(int isJsonFile) {
        this.isJsonFile = isJsonFile;
    }
    public void setIsPhotoPicked(int isPhotoPicked) {
        this.isPhotoPicked = isPhotoPicked;
    }

    public void setImageUrl(String postImage) {
        this.imageUrl = postImage;
    }

    public void setImageSetByUser(boolean isImageSetByUser) {
        this.isImageSetByUser = isImageSetByUser;
    }
    public void setContent(String text) {
        this.text = text;
    }
    public void setLikesCount(int likesCount) {
        this.likesCount = likesCount;
    }

    public void setUserLiked(boolean userLiked) {
        this.userLiked = userLiked;
    }

    public Creator getCreator() {
        return creator;
    }

    public void setCreator(Creator createdBy) {
        this.creator = createdBy;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public boolean isCanEdit() {
        return canEdit;
    }

    public void setCanEdit(boolean canEdit) {
        this.canEdit = canEdit;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }
}
