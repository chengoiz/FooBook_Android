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

public class FriendRequestAdapter extends RecyclerView.Adapter<FriendRequestAdapter.ViewHolder> {
    private Context context;
    private List<User> users;
    private final LayoutInflater inflater;
    private final FriendRequestListener listener;
    private String currentUserId;

    public interface FriendRequestListener {
        void onAcceptRequest(String userId, String friendId);

        void onDeclineRequest(String userId, String friendId);
    }


    // Adapter constructor
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
        View view = inflater.inflate(R.layout.item_friend_request, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User user = users.get(position);
        holder.displayNameTextView.setText(user.getDisplayname());
        loadImage(holder.profilePicImageView, user.getProfilepic(), context);
    }


    @Override
    public int getItemCount() {
        return users.size();
    }
    public void removeItemAt(int position) {
        if (position >= 0 && position < users.size()) {
            users.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, users.size());
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView displayNameTextView;
        Button acceptButton, declineButton;
        ImageView profilePicImageView;

        public ViewHolder(View itemView) {
            super(itemView);
            displayNameTextView = itemView.findViewById(R.id.displayNameTextView);
            acceptButton = itemView.findViewById(R.id.acceptButton);
            declineButton = itemView.findViewById(R.id.declineButton);
            profilePicImageView = itemView.findViewById(R.id.profileImageViewFriendRequest);


            acceptButton.setOnClickListener(view -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    User friend = users.get(position);
                    String friendId = friend.getId();
                    listener.onAcceptRequest(currentUserId, friendId);
                    removeItemAt(position);
                }
            });

            declineButton.setOnClickListener(view -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    User friend = users.get(position);
                    String friendId = friend.getId();
                    listener.onDeclineRequest(currentUserId, friendId);
                    removeItemAt(position);
                }
            });
        }
    }

    // Method to update the dataset and refresh the RecyclerView
    public void setUsers(List<User> users) {
        this.users = users;
        notifyDataSetChanged();
    }
}
