package com.example.foobook_android.adapters;

import static com.example.foobook_android.utility.ImageUtility.loadImage;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.foobook_android.R;
import com.example.foobook_android.models.User;
import com.example.foobook_android.utility.ImageUtility;


import java.util.List;

/**
 * FriendRequestAdapter manages the display of friend requests in a RecyclerView,
 * allowing users to accept or decline requests. Each request is represented by a User object
 * and displayed with the user's display name and profile picture. This adapter provides
 * buttons for accepting or declining each friend request, with appropriate callbacks to handle
 * these actions.
 */
public class FriendRequestAdapter extends RecyclerView.Adapter<FriendRequestAdapter.ViewHolder> {
    private final Context context;
    private List<User> users; // List of users who have sent friend requests
    private final LayoutInflater inflater; // LayoutInflater to inflate the view for each friend request item
    private final FriendRequestListener listener; // Listener for handling accept and decline actions
    private final String currentUserId; // ID of the current user viewing the requests

    // Interface for defining the callback methods for accept and decline actions
    public interface FriendRequestListener {
        void onAcceptRequest(String userId, String friendId);
        void onDeclineRequest(String userId, String friendId);
    }

    // Constructor for the adapter
    public FriendRequestAdapter(Context context, List<User> users, FriendRequestListener listener, String currentUserId) {
        this.context = context;
        this.users = users;
        this.inflater = LayoutInflater.from(context);
        this.listener = listener;
        this.currentUserId = currentUserId;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the layout for each friend request item
        View view = inflater.inflate(R.layout.item_friend_request, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Bind data for each friend request to the ViewHolder
        User user = users.get(position);
        holder.displayNameTextView.setText(user.getDisplayName());

        String profilePic = user.getProfilePic();
        if (ImageUtility.isBase64(profilePic)) {
            Bitmap profilePicBitmap = ImageUtility.base64ToBitmap(profilePic);
            holder.profilePicImageView.setImageBitmap(profilePicBitmap);
        } else if (ImageUtility.isImageUrl(profilePic)) {
            loadImage(holder.profilePicImageView, profilePic, context);
        }
    }

    @Override
    public int getItemCount() {
        // Return the total number of friend requests
        return users.size();
    }

    // Removes an item from the list of friend requests and notifies the adapter
    public void removeItemAt(int position) {
        if (position >= 0 && position < users.size()) {
            users.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, users.size());
        }
    }

    // ViewHolder class for managing the layout of each friend request item
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView displayNameTextView;
        Button acceptButton, declineButton;
        ImageView profilePicImageView;

        public ViewHolder(View itemView) {
            super(itemView);
            // Initialize UI components of the friend request item
            displayNameTextView = itemView.findViewById(R.id.displayNameTextView);
            acceptButton = itemView.findViewById(R.id.acceptButton);
            declineButton = itemView.findViewById(R.id.declineButton);
            profilePicImageView = itemView.findViewById(R.id.profileImageViewFriendRequest);

            // Setup the accept button click listener
            acceptButton.setOnClickListener(view -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    User friend = users.get(position);
                    String friendId = friend.getId();
                    listener.onAcceptRequest(currentUserId, friendId);
                    removeItemAt(position); // Remove the request from the list after accepting
                }
            });

            // Setup the decline button click listener
            declineButton.setOnClickListener(view -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    User friend = users.get(position);
                    String friendId = friend.getId();
                    listener.onDeclineRequest(currentUserId, friendId);
                    removeItemAt(position); // Remove the request from the list after declining
                }
            });
        }
    }

    // Updates the list of friend requests and refreshes the RecyclerView
    @SuppressLint("NotifyDataSetChanged")
    public void setUsers(List<User> users) {
        this.users = users;
        notifyDataSetChanged();
    }
}
