package com.example.foobook_android.activities;

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
import com.example.foobook_android.models.User;

import java.util.ArrayList;
import java.util.List;


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
        fetchFriendRequests(userId);
    }
    private void initialize() {
        userRepository = new UserRepository(this);
        userId = userRepository.getIdFromSharedPreferences();

        friendRequestsRecyclerView = findViewById(R.id.friendRequestsRecyclerView);
        friendRequestsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new FriendRequestAdapter(this, new ArrayList<>(), this);
        friendRequestsRecyclerView.setAdapter(adapter);

        friendshipViewModel = new ViewModelProvider(this).get(FriendshipViewModel.class);
    }
    private void fetchFriendRequests(String userId) {
        friendshipViewModel.getFriendRequests(userId).observe(this, friendRequestResponse -> {
            if (friendRequestResponse != null && friendRequestResponse.getFriendRequests() != null) {
                adapter.setUsers(friendRequestResponse.getFriendRequests());
            } else {
                Toast.makeText(FriendRequestsActivity.this, "Failed to fetch friend requests.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onAcceptRequest(String userId, String friendId) {
        friendshipViewModel.acceptFriendRequest(userId, friendId);
        // Optionally, refresh the list or show a confirmation message
        Toast.makeText(this, "Friend request accepted.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDeclineRequest(String userId, String friendId) {
        friendshipViewModel.declineFriendRequest(userId, friendId);
        // Optionally, refresh the list or show a confirmation message
        Toast.makeText(this, "Friend request declined.", Toast.LENGTH_SHORT).show();
    }
}
