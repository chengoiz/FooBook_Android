package com.example.foobook_android.adapters;

import static com.example.foobook_android.utility.ImageUtility.loadImage;

import android.content.Context;
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

import java.util.List;

/**
 * FriendListAdapter manages a list of friends in a RecyclerView, displaying each friend's
 * details and providing interaction options like viewing a friend's profile or deleting a friend.
 * This adapter uses a custom ViewHolder to manage the layout of each friend item.
 */
public class FriendListAdapter extends RecyclerView.Adapter<FriendListAdapter.ViewHolder> {

    private Context context;
    private List<User> myFriends; // List of friends to display
    private final LayoutInflater inflater; // LayoutInflater to inflate views
    private final FriendListAdapter.FriendListListener listener; // Listener for friend list interactions
    private String currentUserId; // Current user's ID to support friend deletion and other actions

    // Interface defining listener methods for friend list interactions
    public interface FriendListListener {
        void onDeleteFriend(String userId, String friendId);
        void onFriendItemClick(String friendId, String friendDisplayName);
    }

    // Constructor initializing the adapter with necessary data and listeners
    public FriendListAdapter(Context context, List<User> users, FriendListListener listener, String currentUserId) {
        this.context = context;
        this.myFriends = users;
        this.inflater = LayoutInflater.from(context);
        this.listener = listener;
        this.currentUserId = currentUserId;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the friend list item layout
        View view = inflater.inflate(R.layout.item_friend_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FriendListAdapter.ViewHolder holder, int position) {
        // Bind friend data to the ViewHolder
        User user = myFriends.get(position);
        holder.displayNameTextView.setText(user.getDisplayName());
        loadImage(holder.profilePicImageView, user.getProfilePic(), context);
    }

    @Override
    public int getItemCount() {
        // Return the total number of friends in the list
        return myFriends.size();
    }

    // Removes a friend item from the list and notifies the adapter
    public void removeItemAt(int position) {
        if (position >= 0 && position < myFriends.size()) {
            myFriends.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, myFriends.size());
        }
    }

    // ViewHolder class for friend items
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView displayNameTextView;
        Button deleteFriend, friendList;
        ImageView profilePicImageView;

        public ViewHolder(View itemView) {
            super(itemView);
            // Initialize UI components of the friend item
            displayNameTextView = itemView.findViewById(R.id.displayNameFriendList);
            friendList = itemView.findViewById(R.id.friendListOfFriend);
            deleteFriend = itemView.findViewById(R.id.deleteFriendButton);
            profilePicImageView = itemView.findViewById(R.id.profileImageViewFriendList);

            // Set up click listeners for delete and view friend actions
            deleteFriend.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    User friend = myFriends.get(position);
                    String friendId = friend.getId();
                    listener.onDeleteFriend(currentUserId, friendId);
                    removeItemAt(position);
                }
            });

            friendList.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    User friend = myFriends.get(position);
                    String friendDisplayName = friend.getDisplayName();
                    String friendId = friend.getId();
                    listener.onFriendItemClick(friendId, friendDisplayName);
                }
            });
        }
    }

    // Update the list of friends and notify the adapter to refresh the display
    public void setMyFriends(List<User> myFriends) {
        this.myFriends = myFriends;
        notifyDataSetChanged();
    }
}
