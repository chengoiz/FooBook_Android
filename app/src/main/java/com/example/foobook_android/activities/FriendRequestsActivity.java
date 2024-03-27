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
import java.util.HashSet;
import java.util.Set;


public class FriendRequestsActivity extends AppCompatActivity implements FriendRequestAdapter.FriendRequestListener{
    // ViewModel to manage UI-related data in a lifecycle-conscious way
    private FriendshipViewModel friendshipViewModel;
    // Adapter for the RecyclerView to manage friend request items
    private FriendRequestAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friendship_request);
        // Initial setup of the activity, including setting up UI components and data
        initialize();
    }

    // Observes changes in the ViewModel and updates the UI accordingly
    private void observeViewModel() {
        // Observes the result of accepting a friend request
        friendshipViewModel.getAcceptRequestResult().observe(this, isSuccess -> {
            if (isSuccess) {
                // Show success message
                Toast.makeText(this, "Friend request accepted.", Toast.LENGTH_SHORT).show();
            } else {
                // Show failure message
                Toast.makeText(this, "Failed to accept friend request.", Toast.LENGTH_SHORT).show();
            }

        });

        // Observes the result of declining a friend request
        friendshipViewModel.getDeclineRequestResult().observe(this, isSuccess -> {
            if (isSuccess) {
                // Show success message
                Toast.makeText(this, "Friend request declined.", Toast.LENGTH_SHORT).show();
            } else {
                // Show failure message
                Toast.makeText(this, "Failed to decline friend request.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Initializes components and data for the activity
    private void initialize() {
        // Repository for user-related data and operations
        UserRepository userRepository = new UserRepository(this);
        // ID of the current user, used in friend request operations
        String userId = userRepository.getUserId(); // Retrieves the current user's ID

        // Setup for the RecyclerView displaying friend requests
        // RecyclerView for displaying friend requests
        RecyclerView friendRequestsRecyclerView = findViewById(R.id.friendRequestsRecyclerView);
        friendRequestsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new FriendRequestAdapter(this, new ArrayList<>(), this, userId);
        friendRequestsRecyclerView.setAdapter(adapter);

        // Initialize the ViewModel
        friendshipViewModel = new ViewModelProvider(this).get(FriendshipViewModel.class);

        // Fetch existing friend requests and setup observation for the results
        fetchFriendRequests(userId, "Bearer " + getAuthToken());
        observeViewModel();
    }

    // Fetches friend requests for the current user
    private void fetchFriendRequests(String userId, String authToken) {
        friendshipViewModel.getFriendRequests(userId, authToken).observe(this, friendRequestResponse -> {
            if (friendRequestResponse != null && friendRequestResponse.getFriendRequests() != null) {
                // Update the adapter with the fetched friend requests
                adapter.setUsers(friendRequestResponse.getFriendRequests());
            } else {
                // Show failure message if requests could not be fetched
                Toast.makeText(FriendRequestsActivity.this, "Failed to fetch friend requests.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Retrieves the authentication token from SharedPreferences
    private String getAuthToken() {
        SharedPreferences sharedPreferences = getSharedPreferences("userDetails", MODE_PRIVATE);
        return sharedPreferences.getString("token", "");
    }

    @Override
    // Handles acceptance of a friend request
    public void onAcceptRequest(String userId, String friendId) {
        SharedPreferences sharedPreferences = getSharedPreferences("userDetails", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // Retrieve the current friend list
        Set<String> friendList = sharedPreferences.getStringSet("friendList", new HashSet<>());

        // Add the new friend ID to the list
        friendList.add(friendId);

        // Save the updated friend list back to SharedPreferences
        editor.putStringSet("friendList", friendList);
        editor.apply(); // Apply changes
        friendshipViewModel.acceptFriendRequest(userId, friendId);
    }

    @Override
    // Handles decline of a friend request
    public void onDeclineRequest(String userId, String friendId) {
        friendshipViewModel.declineFriendRequest(userId, friendId);
    }
}