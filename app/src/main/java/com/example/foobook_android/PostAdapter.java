package com.example.foobook_android;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {

    public interface PostItemListener {
        void onEdit(int position);
        void onDelete(int position);
    }

    private final Context context;
    private final List<Post> posts;
    private final LayoutInflater mInflater;
    private final PostItemListener listener;

    public PostAdapter(Context context, List<Post> posts, PostItemListener listener) {
        this.context = context;
        this.posts = posts;
        this.listener = listener;
        mInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.post_item, parent, false);
        return new PostViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Post post = posts.get(position);
        holder.userNameTextView.setText(post.getUserName());
        holder.timeStampTextView.setText(post.getTimestamp());
        holder.postContentTextView.setText(post.getContent());
        loadImage(holder.profileImageView, post.getProfileImage());
        loadImage(holder.postImageView, post.getPostImage());



        // comment button
        holder.feedCommentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Intent to start CommentActivity
                if (position != RecyclerView.NO_POSITION) {
                    Intent intent = new Intent(context, CommentActivity.class);
                    intent.putExtra("postPosition", position);
                    context.startActivity(intent);
                }
            }
        });

    }

    public void loadImage(ImageView imageView, String imageUrl) {
        Glide.with(context)
                .load(imageUrl)
                .placeholder(R.drawable.defaultpic)
                .error(R.drawable.ic_launcher_background)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(GlideException e, Object model,
                                                Target<Drawable> target, boolean isFirstResource) {
                        Log.e("Glide", "Load failed for " + imageUrl, e);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model,
                                                   Target<Drawable> target, DataSource dataSource,
                                                   boolean isFirstResource) {
                        Log.d("Glide", "Load succeeded for " + imageUrl);
                        return false;
                    }
                })
                .into(imageView);
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }


    static class PostViewHolder extends RecyclerView.ViewHolder {
        TextView userNameTextView;
        TextView timeStampTextView;
        TextView postContentTextView;
        ImageView profileImageView;
        ImageView postImageView;
        ImageButton feedCommentBtn;
        ImageButton feedBtnLike;



        PostViewHolder(View itemView) {
            super(itemView);
            userNameTextView = itemView.findViewById(R.id.userNameTextView);
            timeStampTextView = itemView.findViewById(R.id.timeStampTextView);
            postContentTextView = itemView.findViewById(R.id.postContentTextView);
            profileImageView = itemView.findViewById(R.id.profileImageView);
            postImageView = itemView.findViewById(R.id.postImageView);
            feedCommentBtn = itemView.findViewById(R.id.feedBtnComment);
            feedBtnLike = itemView.findViewById(R.id.feedBtnLike);


        }


    }
}
