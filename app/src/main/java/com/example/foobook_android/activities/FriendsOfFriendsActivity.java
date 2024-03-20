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

/**
 * FriendsOfFriendsActivity displays a list of friends for a specific user's friend.
 * It utilizes a RecyclerView to list these friends and leverages a ViewModel to fetch
 * and observe the friends' data. This activity is designed to show the social connections
 * of a user's friend, providing insight into their network. It is accessible by selecting
 * a friend in the FriendsListActivity, which passes the friend's ID and display name to this
 * activity for data retrieval and presentation.
 */
public class FriendsOfFriendsActivity extends AppCompatActivity {
    // ViewModel for handling friendship data
    private FriendshipViewModel friendshipViewModel;
    // RecyclerView for displaying the list of a friend's friends
    private RecyclerView friendOfFriendListRecyclerView;
    // Adapter for the RecyclerView
    private FriendsOfFriendAdapter adapter;
    // The ID of the friend whose friends are being displayed
    private String friendId;
    // The display name of the friend, used for setting the activity's title
    private String friendDisplayName;
    // TextView for the title of the activity
    private TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Setting the content view to this activity's layout
        setContentView(R.layout.activity_friends_of_friends);
        // Initial setup of UI components and data binding
        initialize();
    }

    // Initializes UI components and binds data from intent
    private void initialize() {
        // Retrieves the friend's ID and display name passed through intent
        if (getIntent() != null && getIntent().hasExtra("FRIEND_ID")) {
            friendId = getIntent().getStringExtra("FRIEND_ID");
        }
        if (getIntent() != null && getIntent().hasExtra("FRIEND_DISPLAY_NAME")) {
            friendDisplayName = getIntent().getStringExtra("FRIEND_DISPLAY_NAME");
        }

        // Sets the activity's title to include the friend's name
        title = findViewById(R.id.friendsOfFriendTitle);
        String newTitle = friendDisplayName + "'s friends";
        title.setText(newTitle);

        // Sets up the RecyclerView with its adapter and layout manager
        friendOfFriendListRecyclerView = findViewById(R.id.friendsOfFriendRecycleView);
        friendOfFriendListRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new FriendsOfFriendAdapter(this, new ArrayList<>(), friendId);
        friendOfFriendListRecyclerView.setAdapter(adapter);

        // Initializes the ViewModel for fetching the friend list
        friendshipViewModel = new ViewModelProvider(this).get(FriendshipViewModel.class);

        // Fetches the list of friends for the specified friend's ID
        fetchFriendsOfFriend(friendId);
    }

    // Fetches and observes the friends of the specified friend
    private void fetchFriendsOfFriend(String friendId) {
        friendshipViewModel.getFriendList(friendId).observe(this, friendListResponse -> {
            if (friendListResponse != null && friendListResponse.getFriends() != null) {
                // Updates the adapter with the retrieved list of friends
                adapter.setMyFriends(friendListResponse.getFriends());
            } else {
                // Displays a toast message if fetching friends of friend fails
                Toast.makeText(FriendsOfFriendsActivity.this, "Failed to fetch friends of friend.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
