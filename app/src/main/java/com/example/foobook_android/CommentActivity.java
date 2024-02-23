package com.example.foobook_android;

import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foobook_android.adapters.CommentAdapter;


import java.util.ArrayList;

public class CommentActivity extends AppCompatActivity implements CommentAdapter.CommentItemListener {
    private ArrayList<Comment> commentsList;
    private RecyclerView commentsRecyclerView;
    private CommentAdapter commentAdapter;
    private int postPosition;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        Log.i("CommentActivity", "onCreate");

        postPosition = getIntent().getIntExtra("postPosition", -1);
        if (postPosition == -1) {
            finish();
            return;
        }

        // Retrieve comments for this post from the CommentsDataHolder
        commentsList = new ArrayList<>(CommentsDataHolder.getComments(postPosition));
        commentsRecyclerView = findViewById(R.id.commentsRecyclerView);
        commentsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        commentAdapter = new CommentAdapter(this, commentsList, this);
        commentsRecyclerView.setAdapter(commentAdapter);

        // Adding a comment
        ImageButton sendCommentBtn = findViewById(R.id.sendCommentButton);
        sendCommentBtn.setOnClickListener(v -> {
            addComment();
        });
    }
    public void addComment() {
        EditText writeTextComment = findViewById(R.id.commentItemComment);
        String commentText = writeTextComment.getText().toString().trim();
        // Hard coded for now
        String commenterName = "username";
        String commenterProfileImage = "URL or Path to Image";

        if (!commentText.isEmpty()) {
            Comment newComment = new Comment(commenterName, commentText, commenterProfileImage);
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