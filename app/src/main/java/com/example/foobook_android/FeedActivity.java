package com.example.foobook_android;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;


import com.example.foobook_android.PostAdapter;
import android.widget.ImageButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;



public class FeedActivity extends AppCompatActivity implements PostAdapter.PostItemListener {


    private RecyclerView recyclerView;
    private PostAdapter postAdapter;
    private PhotoSelectorHelper photoSelectorHelper;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

        recyclerView = findViewById(R.id.feedRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        postAdapter = new PostAdapter(this, PostManager.getPosts(), this);
        recyclerView.setAdapter(postAdapter);


}

    @Override
    public void onEdit(int position) {

    }

    @Override
    public void onDelete(int position) {

    }
}
