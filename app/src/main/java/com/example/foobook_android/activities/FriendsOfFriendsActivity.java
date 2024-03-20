package com.example.foobook_android.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.example.foobook_android.R;
import com.example.foobook_android.Repositories.UserRepository;
import com.example.foobook_android.ViewModels.FriendshipViewModel;
import com.example.foobook_android.ViewModels.PostViewModel;
import com.example.foobook_android.adapters.FriendsOfFriendAdapter;

import java.util.ArrayList;

public class FriendsOfFriendsActivity extends AppCompatActivity {
    private FriendshipViewModel friendshipViewModel;
    private RecyclerView friendOfFriendListRecyclerView;
    private FriendsOfFriendAdapter adapter;
    private String friendId;
    private String friendDisplayName;
    private TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends_of_friends);
        initialize();
    }

    private void initialize() {
        if (getIntent() != null && getIntent().hasExtra("FRIEND_ID")) {
            friendId = getIntent().getStringExtra("FRIEND_ID");
        }
        if (getIntent() != null && getIntent().hasExtra("FRIEND_DISPLAY_NAME")) {
            friendDisplayName = getIntent().getStringExtra("FRIEND_DISPLAY_NAME");
        }
        title = findViewById(R.id.friendsOfFriendTitle);
        String newTitle = friendDisplayName + "'s friends";
        title.setText(newTitle);
        friendOfFriendListRecyclerView = findViewById(R.id.friendsOfFriendRecycleView);
        friendOfFriendListRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new FriendsOfFriendAdapter(this, new ArrayList<>(), friendId);
        friendOfFriendListRecyclerView.setAdapter(adapter);
        friendshipViewModel = new ViewModelProvider(this).get(FriendshipViewModel.class);

        fetchFriendsOfFriend(friendId);
    }



    private void fetchFriendsOfFriend(String friendId) {
        friendshipViewModel.getFriendList(friendId).observe(this, friendListResponse -> {
            if (friendListResponse != null && friendListResponse.getFriends() != null) {
                adapter.setMyFriends(friendListResponse.getFriends());
            } else {
                Toast.makeText(FriendsOfFriendsActivity.this, "Failed to fetch friends of friend.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}