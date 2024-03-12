package com.example.foobook_android.activities;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foobook_android.R;
import com.example.foobook_android.ViewModels.FriendshipViewModel;
import com.example.foobook_android.adapters.FriendRequestAdapter;

import java.util.ArrayList;

public class FriendRequestsActivity extends AppCompatActivity {
    private FriendshipViewModel friendshipViewModel;
    private RecyclerView friendRequestsRecyclerView;
    private FriendRequestAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friendship_request);

        friendRequestsRecyclerView = findViewById(R.id.friendRequestsRecyclerView);
        friendRequestsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new FriendRequestAdapter(this, new ArrayList<>()); // Initialize your adapter
        friendRequestsRecyclerView.setAdapter(adapter);

        // Initialize ViewModel
        friendshipViewModel = new ViewModelProvider(this).get(FriendshipViewModel.class);
        String userId = "yourUserId"; // Obtain current user's ID
        friendshipViewModel.getFriendRequests(userId).observe(this, friendRequests -> {
            if (friendRequests != null) {
                adapter.setUsers(friendRequests); // Update the adapter
            } else {
                Toast.makeText(this, "Failed to fetch friend requests.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
