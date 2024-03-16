package com.example.foobook_android.post;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.example.foobook_android.comment.Comment;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Post implements Serializable {
    public static final int PHOTO_PICKED = 1;
    public static final int NO_PHOTO = 0;
    public static final int JSON_FILE = 1;
    public static final int NOT_JSON_FILE = 0;

    @NonNull
    @PrimaryKey
    @SerializedName("_id")
    private String postId;
    @ColumnInfo(name = "userName")
    private String userName;

    @ColumnInfo(name = "timestamp")
    private String timestamp;

    @ColumnInfo(name = "text")
    private String text;

    @Ignore /** Ignoring this field for Room database **/
    private List<Comment> comments = new ArrayList<>();

    @ColumnInfo(name = "likesCount")
    @SerializedName("likeCount")
    private int likesCount;

    @ColumnInfo(name = "isLikedByCurrentUser")
    @SerializedName("userLiked")
    private boolean isLikedByCurrentUser;

    @SerializedName("canEdit")
    private boolean canEdit;

    @ColumnInfo(name = "profileImage")
    private String profileImage;

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

    @ColumnInfo(name = "isFriend")
    private boolean isFriend;



    public Post(String userName, String timestamp, String text, String profileImage) {
        this.userName = userName;
        this.timestamp = timestamp;
        this.text = text;
        this.profileImage = profileImage;
        this.isJsonFile = 0;
        this.isPhotoPicked = NO_PHOTO;
        this.creator = null;
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
        isLikedByCurrentUser = !isLikedByCurrentUser;
        likesCount += isLikedByCurrentUser ? 1 : -1;
        return isLikedByCurrentUser;
    }

    // Getters
    public int getLikesCount() {
        return likesCount;
    }

    public boolean isLikedByCurrentUser() {
        return isLikedByCurrentUser;
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
    public Uri getPostImageUri(){
        return Uri.parse(imageUrl);
    }
    public List<Comment> getComments() {
        return comments;
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



    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public void setContent(String text) {
        this.text = text;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }
    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }
    public void setLikesCount(int likesCount) {
        this.likesCount = likesCount;
    }

    public void setIsLikedByCurrentUser(boolean isLikedByCurrentUser) {
        this.isLikedByCurrentUser = isLikedByCurrentUser;
    }
    public void setIsFriend(boolean isFriend) {
        this.isFriend = true;
    }
    public boolean getIsFriend() {
        return isFriend;
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
