package com.example.foobook_android.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foobook_android.R;
import com.example.foobook_android.models.User;

import java.util.List;

public class FriendRequestAdapter extends RecyclerView.Adapter<FriendRequestAdapter.ViewHolder> {
    private Context context;
    private List<User> users; // Assuming User is your model class
    private LayoutInflater inflater;


    // Adapter constructor
    public FriendRequestAdapter(Context context, List<User> users) {
        this.context = context;
        this.users = users;
        this.inflater = LayoutInflater.from(context);
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

        public ViewHolder(View itemView) {
            super(itemView);
            displayNameTextView = itemView.findViewById(R.id.displayNameTextView);
            acceptButton = itemView.findViewById(R.id.acceptButton);
            declineButton = itemView.findViewById(R.id.declineButton);

            // You can also set onClickListeners for your buttons here
        }
    }

    // Method to update the dataset and refresh the RecyclerView
    public void setUsers(List<User> users) {
        this.users = users;
        notifyDataSetChanged();
    }
}
