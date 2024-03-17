
package com.example.foobook_android.adapters;


import static com.example.foobook_android.utility.ImageUtility.loadImage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foobook_android.R;
import com.example.foobook_android.models.User;

import java.util.List;

public class FriendsOfFriendAdapter extends RecyclerView.Adapter<FriendsOfFriendAdapter.ViewHolder> {
    private Context context;
    private List<User> friendsOfFriends;
    private final LayoutInflater inflater;
    private String currentFriendId;

    public FriendsOfFriendAdapter(Context context, List<User> friendsOfFriends, String currentUserId) {
        this.context = context;
        this.friendsOfFriends = friendsOfFriends;
        this.inflater = LayoutInflater.from(context);
        this.currentFriendId = currentUserId;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_friend_of_friend, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User friend = friendsOfFriends.get(position);
        holder.friendOfFriendText.setText(friend.getDisplayname());
        loadImage(holder.friendOfFriendProfilePic, friend.getProfilepic(), context);
    }

    @Override
    public int getItemCount() {
        return friendsOfFriends.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView friendOfFriendText;

        ImageView friendOfFriendProfilePic;


        public ViewHolder(View itemView) {
            super(itemView);
            friendOfFriendText = itemView.findViewById(R.id.friendOfFriendText);
            friendOfFriendProfilePic = itemView.findViewById(R.id.friendOfFriendProfilePic);
        }
    }
    public void setMyFriends(List<User> myFriends) {
        this.friendsOfFriends = myFriends;
        notifyDataSetChanged();
    }
}
