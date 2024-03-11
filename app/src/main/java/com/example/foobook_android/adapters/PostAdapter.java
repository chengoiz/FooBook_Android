package com.example.foobook_android.adapters;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.foobook_android.activities.CommentActivity;
import com.example.foobook_android.comment.CommentsDataHolder;
import com.example.foobook_android.post.Post;
import com.example.foobook_android.R;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {
    private static final int PHOTO_PICKED = 1;
    private static final int NO_PHOTO = 0;

    private final Context context;
    private List<Post> posts;
    private final LayoutInflater inflater;
    private final PostItemListener listener;

    public PostAdapter(Context context, List<Post> posts, PostItemListener listener) {
        this.context = context;
        this.posts = posts;
        this.listener = listener;
        this.inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.post_item, parent, false);
        return new PostViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Post post = posts.get(position);
        holder.userNameTextView.setText(post.getUserName());
        holder.timeStampTextView.setText(post.getTimestamp());
        holder.postContentTextView.setText(post.getContent());

        // Set profile image
        if (post.getIsJsonFile() == Post.JSON_FILE) {
            loadImage(holder.profileImageView, post.getProfileImage());
        } else {
            setImageFromDrawableName(holder.profileImageView, post.getProfileImage());
        }

        // Set post image or hide if not applicable
        // Set post image or hide if not applicable
        if (post.getIsPhotoPicked() == PHOTO_PICKED && post.getPostImageUrl() != null && !post.getPostImageUrl().isEmpty()) {
            holder.postImageView.setVisibility(View.VISIBLE);
            loadImage(holder.postImageView, post.getPostImageUrl());
        } else {
            holder.postImageView.setVisibility(View.GONE);
        }

        updateLikesAndComments(holder, post, position);

        // Like button click handling
        holder.feedBtnLike.setOnClickListener(v -> updateLikes(holder, post));

        // Comment button click handling
        holder.feedCommentBtn.setOnClickListener(v -> startCommentActivity(position));

        // Menu button handling
        holder.editPostMenu.setOnClickListener(v -> {
            int adapterPosition = holder.getAdapterPosition();
            if (adapterPosition != RecyclerView.NO_POSITION) {
                showPostMenu(v, listener, adapterPosition);
            }
        });

        // Share button click
        holder.shareButton.setOnClickListener(this::showShareMenu);
    }


    private void updateLikesAndComments(PostViewHolder holder, Post post, int position) {
        holder.likesCountTextView.setText(context.getString(R.string.likes_count,
                                        post.getLikesCount()));
        int commentCount = CommentsDataHolder.getCommentCount(position);
        holder.commentsCountTextView.setText(context.getString(R.string.comments_count,
                                        commentCount));
    }

    private void updateLikes(PostViewHolder holder, Post post) {
        post.toggleLike();
        holder.likesCountTextView.setText(context.getString(R.string.likes_count,
                post.getLikesCount()));
    }

    private void startCommentActivity(int position) {
        Intent intent = new Intent(context, CommentActivity.class);
        intent.putExtra("postPosition", position);
        context.startActivity(intent);
    }

    public void loadImage(ImageView imageView, String imageUrl) {
        Glide.with(context)
                .load(imageUrl)
                .placeholder(R.drawable.defaultpic)
                .error(R.drawable.saved)
                .into(imageView);
    }

    public void setImageFromDrawableName(ImageView imageView, String drawableName) {
        Resources resources = context.getResources();
        int resourceId = resources.getIdentifier(drawableName, "drawable", context.getPackageName());
        imageView.setImageResource(resourceId != 0 ? resourceId : R.drawable.defaultpic);
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
        notifyDataSetChanged();
    }

    private void showShareMenu(View view) {
        PopupMenu shareMenu = new PopupMenu(view.getContext(), view);
        shareMenu.inflate(R.menu.share_button_menu);
        shareMenu.setOnMenuItemClickListener(item -> false);
        shareMenu.show();
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    private void showPostMenu(View view, PostItemListener listener, int position) {
        PopupMenu postMenu = new PopupMenu(view.getContext(), view);
        postMenu.inflate(R.menu.post_menu);
        postMenu.setOnMenuItemClickListener(item -> {
            // Obtain the Post object from the adapter's current dataset using the position.
            Post postToEdit = posts.get(position);

            if (item.getItemId() == R.id.menuEditPost) {
                listener.onEdit(postToEdit);
                return true;
            } else if (item.getItemId() == R.id.menuDeletePost) {
                listener.onDelete((int)postToEdit.getId());
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, getItemCount());
                return true;
            }
            return false;
        });
        postMenu.show();
    }

    public void removePostById(long postId) {
        int position = -1;
        for (int i = 0; i < posts.size(); i++) {
            if (posts.get(i).getId() == postId) {
                position = i;
                break;
            }
        }
        if (position != -1) {
            posts.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, posts.size());
        }
    }

    public Post getPostAt(int position) {
        if (position >= 0 && position < posts.size()) {
            return posts.get(position);
        }
        return null; // Or throw an exception based on how you want to handle this scenario
    }

    public interface PostItemListener {
        void onEdit(Post post);

        void onDelete(long position);
    }

    static class PostViewHolder extends RecyclerView.ViewHolder {
        TextView userNameTextView, timeStampTextView, postContentTextView, likesCountTextView,
                commentsCountTextView;
        ImageView profileImageView, postImageView;
        ImageButton feedCommentBtn, feedBtnLike, editPostMenu, shareButton;

        PostViewHolder(View itemView) {
            super(itemView);
            userNameTextView = itemView.findViewById(R.id.userNameTextView);
            timeStampTextView = itemView.findViewById(R.id.timeStampTextView);
            postContentTextView = itemView.findViewById(R.id.postContentTextView);
            profileImageView = itemView.findViewById(R.id.profileImageView);
            postImageView = itemView.findViewById(R.id.postImageView);
            likesCountTextView = itemView.findViewById(R.id.likesCountTextView);
            commentsCountTextView = itemView.findViewById(R.id.commentsCountTextView);
            feedCommentBtn = itemView.findViewById(R.id.feedBtnComment);
            feedBtnLike = itemView.findViewById(R.id.feedBtnLike);
            editPostMenu = itemView.findViewById(R.id.editPostMenu);
            shareButton = itemView.findViewById(R.id.feedBtnShare);
        }
    }
}