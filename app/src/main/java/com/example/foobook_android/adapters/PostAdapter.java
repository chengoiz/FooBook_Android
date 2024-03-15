package com.example.foobook_android.adapters;


import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import com.bumptech.glide.Glide;
import com.example.foobook_android.Api.WebServiceApi;
import com.example.foobook_android.activities.CommentActivity;
import com.example.foobook_android.comment.CommentsDataHolder;
import com.example.foobook_android.models.FriendshipRequest;
import com.example.foobook_android.models.User;
import com.example.foobook_android.post.Post;
import com.example.foobook_android.R;
import com.example.foobook_android.utility.RetrofitClient;
import com.google.gson.Gson;

import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {
    private static final int PHOTO_PICKED = 1;
    private static final int NO_PHOTO = 0;

    private final Context context;
    private List<Post> posts;
    private final LayoutInflater inflater;
    private final PostItemListener listener;
    private List<String> friendIds;


    public PostAdapter(Context context, List<Post> posts, PostItemListener listener) {
        this.context = context;
        this.posts = posts;
        this.listener = listener;
        this.inflater = LayoutInflater.from(context);
        this.friendIds = friendIds;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.post_item, parent, false);
        return new PostViewHolder(itemView);

    }

//    public class ViewHolder extends RecyclerView.ViewHolder {
//        // Other views
//        ImageView profileImageView;
//
//        public ViewHolder(View itemView) {
//            super(itemView);
//            profileImageView = itemView.findViewById(R.id.profileImageView); // Assuming the ID is profileImageView
//
//            profileImageView.setOnClickListener(v -> {
//                int position = getAdapterPosition();
//                if (position != RecyclerView.NO_POSITION) {
//                    Post post = posts.get(position);
//                    sendFriendRequest(post.getCreatedBy()); // Assuming Post has a method getCreatedBy() returning the User ID
//                }
//            });
//        }
//    }


    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Post post = posts.get(position);
        holder.userNameTextView.setText(post.getCreator().getDisplayName());
        holder.timeStampTextView.setText(post.getTimestamp());
        holder.postContentTextView.setText(post.getText());

        // Set profile image
        if (post.getCreator().getProfilePic() != null && !post.getCreator().getProfilePic().isEmpty()) {
            loadImage(holder.profileImageView, post.getCreator().getProfilePic());
        } else {
            setImageFromDrawableName(holder.profileImageView, post.getProfileImage());
        }

        // Set post image or hide if not applicable
        // Set post image or hide if not applicable
        if (post.getImageUrl() != null && !post.getImageUrl().isEmpty()) {
            holder.postImageView.setVisibility(View.VISIBLE);
            loadImage(holder.postImageView, post.getImageUrl());
        } else {
            holder.postImageView.setVisibility(View.GONE);
        }

        updateLikesAndComments(holder, post, position);
        holder.profileImageView.setOnClickListener(view -> showProfilePictureMenu(view, holder));


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

    private void showProfilePictureMenu(View anchor, PostViewHolder holder) {
        PopupMenu popup = new PopupMenu(anchor.getContext(), anchor);
        popup.getMenuInflater().inflate(R.menu.profile_picture_menu, popup.getMenu());
        popup.setOnMenuItemClickListener(item -> {
            int position = holder.getAdapterPosition();
            int id = item.getItemId();
            if (id == R.id.add_friend) {
                // Use getCreatedBy() to get the user ID directly
                String receiverUserId = posts.get(position).getCreator().getId();
                sendFriendRequest(receiverUserId);
                return true;
            }   else if (id == R.id.view_profile) {
                    // TODO: Implement the go to profile logic here
                    return true;
            }
            return false;
        });
        popup.show();
    }

    private String getCurrentUserId() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("userDetails", MODE_PRIVATE);
        return sharedPreferences.getString("userId", "");
    }

    private String getAuthToken() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("userDetails", MODE_PRIVATE);
        return sharedPreferences.getString("token", "");
        // Retrieve the auth token from SharedPreferences.
    }

    private void sendFriendRequest(String receiverUserId) {
        String currentUserId = getCurrentUserId();
        String authToken = getAuthToken();

        // Assuming you have a FriendshipRequest model and Gson setup
        FriendshipRequest friendshipRequest = new FriendshipRequest(currentUserId, receiverUserId);
        Gson gson = new Gson();
        String json = gson.toJson(friendshipRequest);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);

        // Adjust the Retrofit call to match the expected parameters
        RetrofitClient.getClient("http://10.0.2.2:8080/", authToken).create(WebServiceApi.class)
                .sendFriendRequest(receiverUserId, requestBody, authToken) // Now matching the expected signature
                .enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(context, "Friend request sent!", Toast.LENGTH_SHORT).show();
                        } else {
                            Log.e("FriendRequestError", "Failed to send friend request. HTTP status code: " + response.code() + " Message: " + response.message());
                            Toast.makeText(context, "Failed to send friend request.", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(context, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
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
        if (drawableName != null && !drawableName.isEmpty()) {
            Resources resources = context.getResources();
            int resourceId = resources.getIdentifier(drawableName, "drawable", context.getPackageName());
            imageView.setImageResource(resourceId != 0 ? resourceId : R.drawable.defaultpic);
        } else {
            imageView.setImageResource(R.drawable.defaultpic); // Default image if drawableName is null
        }
    }

    public void setPosts(List<Post> posts) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("userDetails", Context.MODE_PRIVATE);
        String currentUserId = sharedPreferences.getString("userId", "");
//        for (Post post : posts) {
//            post.setCanEdit(post.getCreator().getId().equals(currentUserId));
//        }
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
            if (!posts.isEmpty()) {
                Post postToEdit = posts.get(position);

                if (postToEdit.isCanEdit()) {
                    if (item.getItemId() == R.id.menuEditPost) {
                        listener.onEdit(postToEdit);
                        postMenu.show();
                        return true;
                    } else if (item.getItemId() == R.id.menuDeletePost) {
                        listener.onDelete(postToEdit.getPostId());
//                        posts.remove(position);
//                        notifyItemRemoved(position);
//                        notifyItemRangeChanged(position, getItemCount());  todo: we can remove this after finished.
                        postMenu.show();
                        return true;
                    }
                } else {
                    Toast.makeText(context, "User unauthorized to edit or delete this post.", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(view.getContext(), "Selected post is no longer available.", Toast.LENGTH_SHORT).show();
            }
            return false;
        });

        postMenu.show();
    }



    public void removePostById(String postId) {
        int position = -1;
        for (int i = 0; i < posts.size(); i++) {
            if (posts.get(i).getPostId().equals(postId)) {
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

        void onDelete(String postId);
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