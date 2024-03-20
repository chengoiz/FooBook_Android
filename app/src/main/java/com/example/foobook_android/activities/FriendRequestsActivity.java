package com.example.foobook_android.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foobook_android.R;
import com.example.foobook_android.Repositories.UserRepository;
import com.example.foobook_android.ViewModels.FriendshipViewModel;
import com.example.foobook_android.adapters.FriendRequestAdapter;


import java.util.ArrayList;



public class FriendRequestsActivity extends AppCompatActivity implements FriendRequestAdapter.FriendRequestListener{
    private FriendshipViewModel friendshipViewModel;
    private RecyclerView friendRequestsRecyclerView;
    private FriendRequestAdapter adapter;
    private UserRepository userRepository;
    private String userId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friendship_request);
        initialize();
    }

    private void observeViewModel() {
        friendshipViewModel.getAcceptRequestResult().observe(this, isSuccess -> {
            if (isSuccess) {
                Toast.makeText(this, "Friend request accepted.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Failed to accept friend request.", Toast.LENGTH_SHORT).show();
            }
        });

        friendshipViewModel.getDeclineRequestResult().observe(this, isSuccess -> {
            if (isSuccess) {
                Toast.makeText(this, "Friend request declined.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Failed to decline friend request.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initialize() {
        userRepository = new UserRepository(this);
        userId = userRepository.getUserId();

        friendRequestsRecyclerView = findViewById(R.id.friendRequestsRecyclerView);
        friendRequestsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new FriendRequestAdapter(this, new ArrayList<>(), this, userId);
        friendRequestsRecyclerView.setAdapter(adapter);

        friendshipViewModel = new ViewModelProvider(this).get(FriendshipViewModel.class);

        fetchFriendRequests(userId, "Bearer " + getAuthToken());
        observeViewModel();
    }

    private void fetchFriendRequests(String userId, String authToken) {
        friendshipViewModel.getFriendRequests(userId, authToken).observe(this, friendRequestResponse -> {
            if (friendRequestResponse != null && friendRequestResponse.getFriendRequests() != null) {
                adapter.setUsers(friendRequestResponse.getFriendRequests());
            } else {
                Toast.makeText(FriendRequestsActivity.this, "Failed to fetch friend requests.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String getAuthToken() {
        SharedPreferences sharedPreferences = getSharedPreferences("userDetails", MODE_PRIVATE);
        return sharedPreferences.getString("token", "");
        // Retrieve the auth token from SharedPreferences.
    }

    @Override
    public void onAcceptRequest(String userId, String friendId) {
        friendshipViewModel.acceptFriendRequest(userId, friendId);
    }

    @Override
    public void onDeclineRequest(String userId, String friendId) {
        friendshipViewModel.declineFriendRequest(userId, friendId);
    }
}
