package com.example.foobook_android.activities;

import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.foobook_android.comment.Comment;
import com.example.foobook_android.comment.CommentsDataHolder;
import com.example.foobook_android.R;
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
        setupCommentRecyclerView();
        setupButtons();
    }

    public void setupButtons() {
        ImageButton sendCommentBtn = findViewById(R.id.sendCommentButton);
        sendCommentBtn.setOnClickListener(v -> {
            addComment();
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
        // Hard coded for now
        String commenterName = "Tomer";
        String commenterProfileImage = "drawable/defaultpic.png";

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