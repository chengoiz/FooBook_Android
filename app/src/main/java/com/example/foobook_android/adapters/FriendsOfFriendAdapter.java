
package com.example.foobook_android.adapters;


import static com.example.foobook_android.utility.ImageUtility.loadImage;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foobook_android.R;
import com.example.foobook_android.models.User;
import com.example.foobook_android.utility.ImageUtility;

import java.util.List;

/**
 * FriendsOfFriendAdapter is responsible for displaying a list of friends of a specific friend
 * in a RecyclerView. It shows basic information about each friend, such as their name and profile picture.
 */
public class FriendsOfFriendAdapter extends RecyclerView.Adapter<FriendsOfFriendAdapter.ViewHolder> {
    private final Context context;
    private List<User> friendsOfFriends; // List of friends of a specific friend
    private final LayoutInflater inflater; // LayoutInflater to inflate the view for each item

    // Constructor initializing the adapter with necessary context, data, and friend ID
    public FriendsOfFriendAdapter(Context context, List<User> friendsOfFriends) {
        this.context = context;
        this.friendsOfFriends = friendsOfFriends;
        this.inflater = LayoutInflater.from(context);
        // The ID of the friend whose friends are being displayed
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the layout for each friend of friend item
        View view = inflater.inflate(R.layout.item_friend_of_friend, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Bind data for each friend of friend to the ViewHolder
        User friend = friendsOfFriends.get(position);
        holder.friendOfFriendText.setText(friend.getDisplayName());
        String profilePic = friend.getProfilePic();
        if (ImageUtility.isBase64(profilePic)) {
            Bitmap profilePicBitmap = ImageUtility.base64ToBitmap(profilePic);
            holder.friendOfFriendProfilePic.setImageBitmap(profilePicBitmap);
        } else if (ImageUtility.isImageUrl(profilePic)) {
            loadImage(holder.friendOfFriendProfilePic, profilePic, context);
        }
    }

    @Override
    public int getItemCount() {
        // Return the total number of friends of friends
        return friendsOfFriends.size();
    }

    // ViewHolder class for managing the layout of each friend of friend item
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView friendOfFriendText; // TextView for displaying the friend's name
        ImageView friendOfFriendProfilePic; // ImageView for displaying the friend's profile picture

        public ViewHolder(View itemView) {
            super(itemView);
            // Initialize UI components
            friendOfFriendText = itemView.findViewById(R.id.friendOfFriendText);
            friendOfFriendProfilePic = itemView.findViewById(R.id.friendOfFriendProfilePic);
        }
    }

    // Updates the list of friends of friends and refreshes the RecyclerView
    @SuppressLint("NotifyDataSetChanged")
    public void setMyFriends(List<User> myFriends) {
        this.friendsOfFriends = myFriends;
        notifyDataSetChanged();
    }
}
