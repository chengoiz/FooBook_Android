package com.example.foobook_android.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foobook_android.utility.ImageUtility;
import com.example.foobook_android.ViewModels.PostViewModel;
import com.example.foobook_android.comment.Comment;
import com.example.foobook_android.comment.CommentsDataHolder;
import com.example.foobook_android.R;
import com.example.foobook_android.adapters.CommentAdapter;

import java.util.ArrayList;

public class CommentActivity extends AppCompatActivity implements CommentAdapter.CommentItemListener {
    private ArrayList<Comment> commentsList;
    private PostViewModel postViewModel;
    private RecyclerView commentsRecyclerView;
    private CommentAdapter commentAdapter;
    private String fetchedDisplayName;
    private String fetchedProfilePic;
    private int postPosition;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        Log.i("CommentActivity", "onCreate");
        setPostViewModel();
        setupCommentRecyclerView();
        fetchUserDetails();
        setupButtons();
        TextView usernameTextView = findViewById(R.id.commentItemUsername);
        usernameTextView.setText(fetchedDisplayName);
    }

    public void setupButtons() {
        ImageButton sendCommentBtn = findViewById(R.id.sendCommentButton);
        sendCommentBtn.setOnClickListener(v -> {
            addComment();
        });
    }

    private void fetchUserDetails() {
        SharedPreferences sharedPreferences = getSharedPreferences("userDetails", MODE_PRIVATE);
        fetchedDisplayName = sharedPreferences.getString("displayName", "Unknown User");
        fetchedProfilePic = sharedPreferences.getString("profilePicUrl", "default_profile_pic_url");
    }

    private String getCurrentUserId() {
        SharedPreferences sharedPreferences = getSharedPreferences("userDetails", MODE_PRIVATE);
        return sharedPreferences.getString("userId", ""); // Replace "userId" with the actual key you used to store the user's ID
    }

    private void setPostViewModel() {
        postViewModel = new ViewModelProvider(this).get(PostViewModel.class);
        SharedPreferences sharedPreferences = getSharedPreferences("userDetails", MODE_PRIVATE);
        String token = sharedPreferences.getString("token", "");
        postViewModel.setToken(token);
        postViewModel.fetchUsername(this);
        // Observe the username LiveData
        postViewModel.getUsernameLiveData().observe(this, username -> {
            if (username != null && !username.isEmpty()) {
                this.fetchedDisplayName = username;
            }
        });
        postViewModel.getUsernameLiveData().observe(this, username -> {
            TextView usernameTextView = findViewById(R.id.commentItemUsername);
            if (username != null) {
                usernameTextView.setText(username);
            } else {
                usernameTextView.setText("Unknown User");
            }
        });
        postViewModel.getProfilePicLiveData().observe(this, profilePic -> {
            if (profilePic != null && !profilePic.isEmpty()) {
                this.fetchedProfilePic = profilePic;


            }
        });
    }
    private void setupCommentRecyclerView() {
        postPosition = getIntent().getIntExtra("postPosition", -1);
        if (postPosition == -1) {
            finish();
        }
        commentsList = new ArrayList<>(CommentsDataHolder.getComments(postPosition));
        commentsRecyclerView = findViewById(R.id.commentsRecyclerView);
        commentsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        commentAdapter = new CommentAdapter(this, commentsList, this);
        commentsRecyclerView.setAdapter(commentAdapter);
    }

    public void addComment() {
        EditText writeTextComment = findViewById(R.id.commentItemComment);
        String commentText = writeTextComment.getText().toString().trim();

        if (!commentText.isEmpty()) {
            // Use fetchedDisplayName and fetchedProfilePic for new comments
            Comment newComment = new Comment(fetchedDisplayName, commentText, fetchedProfilePic);
            CommentsDataHolder.addComment(postPosition, newComment);
            commentsList.add(newComment);
            commentAdapter.notifyItemInserted(commentsList.size() - 1);
            commentsRecyclerView.scrollToPosition(commentsList.size() - 1);
            writeTextComment.setText("");
        } else {
            Toast.makeText(CommentActivity.this, "Comment cannot be empty", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onDeleteComment(int position) {
        CommentsDataHolder.removeComment(postPosition, position);
        commentsList.remove(position);
        commentAdapter.notifyItemRemoved(position);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i("CommentActivity", "onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("CommentActivity", "onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i("CommentActivity", "onPause");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("CommentActivity", "onDestroy");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i("CommentActivity", "onRestart");
    }
}