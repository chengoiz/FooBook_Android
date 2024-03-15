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

public class FriendListAdapter extends RecyclerView.Adapter<FriendListAdapter.ViewHolder> {

    private Context context;
    private List<User> users;
    private final LayoutInflater inflater;
    private final FriendListAdapter.FriendListListener listener;
    private String currentUserId;

    public interface FriendListListener {
        void onDeleteFriend(String userId, String friendId);

    }

    public FriendListAdapter(Context context, List<User> users, FriendListListener listener, String currentUserId) {
        this.context = context;
        this.users = users;
        this.inflater = LayoutInflater.from(context);
        this.listener = listener;
        this.currentUserId = currentUserId;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_friend_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FriendListAdapter.ViewHolder holder, int position) {
        User user = users.get(position);
        holder.displayNameTextView.setText(user.getDisplayname());
        loadImage(holder.profilePicImageView, user.getProfilepic(), context);
    }

    @Override
    public int getItemCount() {
        return users.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView displayNameTextView;
        Button deleteFriend, friendList;
        ImageView profilePicImageView;


        public ViewHolder(View itemView) {
            super(itemView);
            displayNameTextView = itemView.findViewById(R.id.displayNameFriendList);
            friendList = itemView.findViewById(R.id.friendListOfFriend);
            deleteFriend = itemView.findViewById(R.id.deleteFriendButton);
            profilePicImageView = itemView.findViewById(R.id.profileImageViewFriendList);

            deleteFriend.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    User friend = users.get(position);
                    String friendId = friend.getId();
                    listener.onDeleteFriend(currentUserId, friendId);
                }
            });
        }
    }

    public void setUsers(List<User> users) {
        this.users = users;
        notifyDataSetChanged();
    }
}
