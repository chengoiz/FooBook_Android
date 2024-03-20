package com.example.foobook_android.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import com.example.foobook_android.R;
import com.example.foobook_android.Repositories.UserRepository;
import com.example.foobook_android.ViewModels.FriendshipViewModel;
import com.example.foobook_android.adapters.FriendListAdapter;
import java.util.ArrayList;

public class FriendsListActivity extends AppCompatActivity implements FriendListAdapter.FriendListListener{
    // ViewModel for handling operations related to friendships
    private FriendshipViewModel friendshipViewModel;

    // RecyclerView for displaying the list of friends
    private RecyclerView friendListRecyclerView;
    // Adapter for managing the data in RecyclerView
    private FriendListAdapter adapter;
    // Repository for handling user-related data operations
    private UserRepository userRepository;
    // The current user's ID
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Sets the content view to the friends list activity layout
        setContentView(R.layout.activity_friends_list);
        // Calls initialize to set up UI components and data bindings
        initialize();
    }

    // Observes the ViewModel for friend deletion results
    private void observeViewModel() {
        // Observes the result of friend deletion requests
        friendshipViewModel.getDeclineRequestResult().observe(this, isSuccess -> {
            if (isSuccess) {
                // If friend deletion is successful, display a success message
                Toast.makeText(this, "Successfully deleted from friends.", Toast.LENGTH_SHORT).show();
            } else {
                // If friend deletion fails, display an error message
                Toast.makeText(this, "Failed to delete friend.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Initializes UI components and sets up data bindings
    private void initialize() {
        // Initializes the UserRepository
        userRepository = new UserRepository(this);
        // Retrieves the current user's ID
        userId = userRepository.getUserId();

        // Sets up the RecyclerView for displaying friends
        friendListRecyclerView = findViewById(R.id.friendListRecycleView);
        friendListRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        // Initializes the adapter for the RecyclerView with an empty list
        adapter = new FriendListAdapter(this, new ArrayList<>(), this, userId);
        // Sets the adapter to the RecyclerView
        friendListRecyclerView.setAdapter(adapter);

        // Initializes the FriendshipViewModel
        friendshipViewModel = new ViewModelProvider(this).get(FriendshipViewModel.class);

        // Fetches the friend list for the current user
        fetchFriendList(userId);
        // Starts observing the ViewModel for updates
        observeViewModel();
    }

    // Fetches the friend list for the current user and updates the UI
    private void fetchFriendList(String userId) {
        friendshipViewModel.getFriendList(userId).observe(this, friendListResponse -> {
            if (friendListResponse != null && friendListResponse.getFriends() != null) {
                // Updates the adapter with the fetched friends
                adapter.setMyFriends(friendListResponse.getFriends());
            } else {
                // Displays an error message if fetching the friend list fails
                Toast.makeText(FriendsListActivity.this, "Failed to fetch friend list.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    // Handles friend deletion requests
    public void onDeleteFriend(String userId, String friendId) {
        // Requests the ViewModel to delete a friend
        friendshipViewModel.declineFriendRequest(userId, friendId);
        // Displays a success message (Note: This message might be better placed inside observeViewModel to accurately reflect the operation result)
        Toast.makeText(this, "Successfully deleted from friends.", Toast.LENGTH_SHORT).show();
    }

    @Override
    // Handles clicks on friend items, navigating to the friend's details or friends list
    public void onFriendItemClick(String friendId, String friendDisplayName) {
        // Creates an intent to navigate to the FriendsOfFriendsActivity with friend details
        Intent intent = new Intent(this, FriendsOfFriendsActivity.class);
        intent.putExtra("FRIEND_ID", friendId);
        intent.putExtra("FRIEND_DISPLAY_NAME", friendDisplayName);
        // Starts the FriendsOfFriendsActivity
        startActivity(intent);
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
        // Auto-generated method stub, potentially for future use or overridden functionality
    }
}
