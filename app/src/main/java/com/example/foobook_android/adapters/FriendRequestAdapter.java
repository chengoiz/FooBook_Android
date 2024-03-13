package com.example.foobook_android.adapters;

import android.content.Context;
import android.net.Uri;
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

    public interface FriendRequestListener {
        void onAcceptRequest(String userId, String friendId);
        void onDeclineRequest(String userId, String friendId);
    }


    // Adapter constructor
    public FriendRequestAdapter(Context context, List<User> users, FriendRequestListener listener) {
        this.context = context;
        this.users = users;
        this.inflater = LayoutInflater.from(context);
        this.listener = listener;
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
        holder.profilePicImageView.setImageURI(Uri.parse(user.getProfilepic()));
        // Implement onClickListeners for acceptButton and declineButton here
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    // ViewHolder class
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView displayNameTextView;
        Button acceptButton, declineButton;
        ImageView profilePicImageView;

        public ViewHolder(View itemView) {
            super(itemView);
            displayNameTextView = itemView.findViewById(R.id.displayNameTextView);
            acceptButton = itemView.findViewById(R.id.acceptButton);
            declineButton = itemView.findViewById(R.id.declineButton);
            profilePicImageView = itemView.findViewById(R.id.profileImageView);


            acceptButton.setOnClickListener(view -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    User user = users.get(position);
                    listener.onAcceptRequest("currentUserId", user.getId()); // Use actual IDs
                }
            });

            declineButton.setOnClickListener(view -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    User user = users.get(position);
                    listener.onDeclineRequest("currentUserId", user.getId()); // Use actual IDs
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
