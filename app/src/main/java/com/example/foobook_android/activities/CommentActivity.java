package com.example.foobook_android.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.foobook_android.utility.ImageUtility;
import com.example.foobook_android.ViewModels.PostViewModel;
import com.example.foobook_android.comment.Comment;
import com.example.foobook_android.comment.CommentsDataHolder;
import com.example.foobook_android.R;
import com.example.foobook_android.adapters.CommentAdapter;

import java.util.ArrayList;

// Activity to display and manage comments on a post
public class CommentActivity extends AppCompatActivity implements CommentAdapter.CommentItemListener {
    private ArrayList<Comment> commentsList; // List of comments for the current post
    private PostViewModel postViewModel; // ViewModel for managing post data
    private RecyclerView commentsRecyclerView; // RecyclerView for displaying comments
    private CommentAdapter commentAdapter; // Adapter for comments RecyclerView
    private String fetchedDisplayName; // Display name of the current user
    private String fetchedProfilePic; // Profile picture URL of the current user
    private int postPosition; // Position of the current post in the list


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        Log.i("CommentActivity", "onCreate");
        setPostViewModel(); // Set up the PostViewModel
        setupCommentRecyclerView(); // Set up the RecyclerView for comments
        fetchUserDetails(); // Fetch current user details
        setupButtons(); // Set up the button listeners
        // Update the display name in the UI
        initializeViews();
    }

    // Sets up the send comment button and its click listener
    public void setupButtons() {
        ImageButton sendCommentBtn = findViewById(R.id.sendCommentButton);
        sendCommentBtn.setOnClickListener(v -> {
            addComment(); // Add a new comment when button is clicked
        });
    }

    // Fetches user details from SharedPreferences
    private void fetchUserDetails() {
        SharedPreferences sharedPreferences = getSharedPreferences("userDetails", MODE_PRIVATE);
        fetchedDisplayName = sharedPreferences.getString("displayName", "Unknown User");
        fetchedProfilePic = sharedPreferences.getString("profilePicUrl", "default_profile_pic_url");
    }

    private void initializeViews() {
        TextView displayNameTextView = findViewById(R.id.commentItemUsername);
        displayNameTextView.setText(fetchedDisplayName);
        ImageView profilePicImageView = findViewById(R.id.commentActivityItemImage);
        if (!fetchedProfilePic.equals("default_profile_pic_url")) {
            Glide.with(this)
                    .load(fetchedProfilePic)
                    .into(profilePicImageView);
        }
    }

    // Retrieves the current user's ID from SharedPreferences
    private String getCurrentUserId() {
        SharedPreferences sharedPreferences = getSharedPreferences("userDetails", MODE_PRIVATE);
        return sharedPreferences.getString("userId", "");
    }

    // Initializes the PostViewModel and fetches user details for the comment functionality
    private void setPostViewModel() {
        postViewModel = new ViewModelProvider(this).get(PostViewModel.class);
        SharedPreferences sharedPreferences = getSharedPreferences("userDetails", MODE_PRIVATE);
        String token = sharedPreferences.getString("token", "");
        postViewModel.setToken(token);
        Log.e("TOKEN FROM COMMENT ACTIVITY: ", token);
        postViewModel.fetchDisplayName(this, getCurrentUserId());

        // Observe changes to display name and profile picture LiveData
        postViewModel.getDisplayNameLiveData().observe(this, displayName -> {
            if (displayName != null && !displayName.isEmpty()) {
                this.fetchedDisplayName = displayName;
            }
        });
        postViewModel.getProfilePicLiveData().observe(this, profilePic -> {
            if (profilePic != null && !profilePic.isEmpty()) {
                this.fetchedProfilePic = profilePic;
            }
        });
    }

    // Sets up the RecyclerView for displaying comments
    private void setupCommentRecyclerView() {
        postPosition = getIntent().getIntExtra("postPosition", -1);
        // If the post position is not passed correctly, finish the activity
        if (postPosition == -1) {
            finish();
        }
        commentsList = new ArrayList<>(CommentsDataHolder.getComments(postPosition));
        commentsRecyclerView = findViewById(R.id.commentsRecyclerView);
        commentsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        commentAdapter = new CommentAdapter(this, commentsList, this);
        commentsRecyclerView.setAdapter(commentAdapter);
    }

    // This method is responsible for adding a new comment to the post.
    // It retrieves the comment text from the EditText, checks if it's not empty,
    // and then creates a new Comment object to add to the CommentsDataHolder and the local list.
    public void addComment() {
        EditText writeTextComment = findViewById(R.id.commentItemComment);
        String commentText = writeTextComment.getText().toString().trim();

        // Check if the comment text is not empty
        if (!commentText.isEmpty()) {
            // Create a new Comment object with the current user's display name, the comment text, and profile picture URL
            Comment newComment = new Comment(fetchedDisplayName, commentText, fetchedProfilePic);
            // Add the new comment to the global comment holder and the local list
            CommentsDataHolder.addComment(postPosition, newComment);
            commentsList.add(newComment);
            // Notify the adapter that a new item has been inserted, and scroll to show the latest comment
            commentAdapter.notifyItemInserted(commentsList.size() - 1);
            commentsRecyclerView.scrollToPosition(commentsList.size() - 1);
            // Clear the EditText for the next comment
            writeTextComment.setText("");
        } else {
            // Show a toast message if the comment is empty
            Toast.makeText(CommentActivity.this, "Comment cannot be empty", Toast.LENGTH_SHORT).show();
        }
    }


    // This method handles the deletion of a comment from the list.
    // It is called when the delete button in the CommentAdapter is pressed.
    @Override
    public void onDeleteComment(int position) {
        // Remove the comment from the global storage and local list
        CommentsDataHolder.removeComment(postPosition, position);
        commentsList.remove(position);
        // Notify the adapter that an item has been removed
        commentAdapter.notifyItemRemoved(position);
    }

    // Lifecycle method called when the activity is becoming visible to the user.
    @Override
    protected void onStart() {
        super.onStart();
        Log.i("CommentActivity", "onStart");
    }

    // Lifecycle method called after onStart() when the activity is now interacting with the user.
    @Override
    protected void onResume() {
        super.onResume();
        Log.i("CommentActivity", "onResume");
    }

    // Lifecycle method called when the system is about to start resuming another activity.
    @Override
    protected void onPause() {
        super.onPause();
        Log.i("CommentActivity", "onPause");
    }

    // Lifecycle method called when the activity is no longer visible to the user.
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("CommentActivity", "onDestroy");
    }

    // Lifecycle method called after the activity has been stopped, just prior to it being started again.
    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i("CommentActivity", "onRestart");
    }
}