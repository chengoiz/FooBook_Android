package com.example.foobook_android.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.example.foobook_android.R;
import com.example.foobook_android.Repositories.UserRepository;
import com.example.foobook_android.ViewModels.FriendshipViewModel;
import com.example.foobook_android.adapters.FriendListAdapter;


import java.util.ArrayList;

public class FriendsListActivity extends AppCompatActivity implements FriendListAdapter.FriendListListener{
    private FriendshipViewModel friendshipViewModel;

    private RecyclerView friendListRecyclerView;
    private FriendListAdapter adapter;
    private UserRepository userRepository;
    private String userId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends_list);
        initialize();
        fetchFriendList(userId);
        observeViewModel();
    }

    private void observeViewModel() {
        friendshipViewModel.getDeclineRequestResult().observe(this, isSuccess -> {
            if (isSuccess) {
                Toast.makeText(this, "Successfully deleted from friends.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Failed to delete friend.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initialize() {
        userRepository = new UserRepository(this);
        userId = userRepository.getIdFromSharedPreferences();

        friendListRecyclerView = findViewById(R.id.friendListRecycleView);
        friendListRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new FriendListAdapter(this, new ArrayList<>(), this, userId);
        friendListRecyclerView.setAdapter(adapter);

        friendshipViewModel = new ViewModelProvider(this).get(FriendshipViewModel.class);
    }
    private void fetchFriendList(String userId) {
        friendshipViewModel.getFriendList(userId).observe(this, friendListResponse -> {
            if (friendListResponse != null && friendListResponse.getFriends() != null) {
                adapter.setUsers(friendListResponse.getFriends());
            } else {
                Toast.makeText(FriendsListActivity.this, "Failed to fetch friend list.", Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public void onDeleteFriend(String userId, String friendId) {
    friendshipViewModel.declineFriendRequest(userId,friendId);
   // fetchFriendList(userId);
    Toast.makeText(this, "Successfully deleted from friends.", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }
}