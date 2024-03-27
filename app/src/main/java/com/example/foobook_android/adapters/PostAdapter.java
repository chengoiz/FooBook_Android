package com.example.foobook_android.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
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
import com.bumptech.glide.Glide;
import com.example.foobook_android.activities.CommentActivity;
import com.example.foobook_android.activities.UserPostsActivity;
import com.example.foobook_android.comment.CommentsDataHolder;
import com.example.foobook_android.post.Post;
import com.example.foobook_android.R;
import com.example.foobook_android.utility.ImageUtility;

import java.util.List;


/**
 * PostAdapter is responsible for displaying a list of posts in a RecyclerView,
 * including details such as the post's creator name, timestamp, content, and images.
 * It also provides interaction capabilities like liking, commenting, sharing, and editing posts
 * through designated buttons and menus.
 */
public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {
    public static final int MAX_WIDTH = 1280;
    public static final int MAX_HEIGHT = 960;
    private final Context context;
    private List<Post> posts; // List of posts to be displayed
    private final LayoutInflater inflater; // LayoutInflater to inflate the view for each post item
    private final PostItemListener listener; // Listener for post item interactions
    boolean isLiked;


    // Constructor initializing the adapter with necessary context, data, and listener
    public PostAdapter(Context context, List<Post> posts, PostItemListener listener) {
        this.context = context;
        this.posts = posts;
        this.listener = listener;
        this.inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the layout for each post item
        View itemView = inflater.inflate(R.layout.post_item, parent, false);
        return new PostViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Post post = posts.get(position);
        // Setting text views for user name, timestamp, and post content
        holder.userNameTextView.setText(post.getCreator().getDisplayName());
        holder.timeStampTextView.setText(post.getTimestamp());
        holder.postContentTextView.setText(post.getText());

        // Setting the profile picture of the post creator
        if (post.getCreator().getProfilePic() != null && !post.getCreator().getProfilePic().isEmpty()) {
            String profilePic = post.getCreator().getProfilePic();
            if (ImageUtility.isImageUrl(profilePic)) {
                loadImage(holder.profileImageView, profilePic);
            } else if (ImageUtility.isBase64(profilePic)) {
                Bitmap profilePicBitmap = ImageUtility.base64ToBitmap(profilePic);
                holder.profileImageView.setImageBitmap(profilePicBitmap);
            }
        } else {
            // Set a default or specified drawable if no profile picture is available
            setImageFromDrawableName(holder.profileImageView, post.getProfileImage());
        }

        // Handling the display of the post image if available
        if (post.getImageUrl() != null && !post.getImageUrl().isEmpty()) {
            String postImage = post.getImageUrl();
            holder.postImageView.setVisibility(View.VISIBLE);
            if (ImageUtility.isImageUrl(postImage)) {
                loadImage(holder.postImageView, postImage);
            } else if (ImageUtility.isBase64(postImage)) {
                Bitmap postImageBitmap = ImageUtility.base64ToBitmap(postImage);
                holder.postImageView.setImageBitmap(postImageBitmap);
            }
        } else {
            holder.postImageView.setVisibility(View.GONE);
        }

        // Update likes and comments counters
        updateLikesAndComments(holder, post, position);
        holder.profileImageView.setOnClickListener(view -> showProfilePictureMenu(view, holder));


        // Set onClickListeners for like, comment, edit, and share buttons
        holder.feedBtnLike.setOnClickListener(v -> updateLikes(holder, post));
        holder.feedCommentBtn.setOnClickListener(v -> startCommentActivity(position));
        holder.editPostMenu.setOnClickListener(v -> {
            int adapterPosition = holder.getAdapterPosition();
            if (adapterPosition != RecyclerView.NO_POSITION) {
                showPostMenu(v, listener, adapterPosition);
            }
        });
        holder.shareButton.setOnClickListener(this::showShareMenu);
    }


    private void showProfilePictureMenu(View anchor, PostViewHolder holder) {
        PopupMenu popup = new PopupMenu(anchor.getContext(), anchor);
        // Clear the existing menu items
        popup.getMenu().clear();
        // Dynamically add menu items based on whether users are friends
        int position = holder.getAdapterPosition();
        if (position != RecyclerView.NO_POSITION) {
            popup.getMenu().add(0, R.id.view_profile, 0, "View Profile");
        }
        popup.setOnMenuItemClickListener(item -> {
            int id = item.getItemId();
            if (id == R.id.view_profile) {
                // Navigate to the user's mini profile to view their posts
                navigateToUserPosts(posts.get(position).getCreator().getId(),
                        posts.get(position).getCreator().getDisplayName(),
                        posts.get(position).getCreator().getProfilePic());
            }
            return false;
        });
        popup.show();
    }


    public void navigateToUserPosts(String userId, String displayName, String ProfilePic) {
        Intent intent = new Intent(context, UserPostsActivity.class);
        intent.putExtra("VIEWED_USER_ID", userId);
        intent.putExtra("VIEWED_USER_DISPLAY_NAME", displayName);
        intent.putExtra("VIEWED_USER_PROFILE_PIC", ProfilePic);
        context.startActivity(intent);
    }

    // Method to update likes and comments display for a post
    private void updateLikesAndComments(PostViewHolder holder, Post post, int position) {
        if (post.getUserLiked()) {
            holder.feedBtnLike.setImageResource(R.drawable.btn_like_blue); // Highlight the like button
        } else {
            holder.feedBtnLike.setImageResource(R.drawable.btn_like); // Revert to default like button
        }
        holder.likesCountTextView.setText(context.getString(R.string.likes_count,
                                        post.getLikesCount()));
        int commentCount = CommentsDataHolder.getCommentCount(position);
        holder.commentsCountTextView.setText(context.getString(R.string.comments_count,
                                        commentCount));
    }

//  Method to handle like button click, toggles the like status and updates the display
    public void updateLikes(PostViewHolder holder, Post post) {
        listener.onLike(post.getPostId());
        isLiked = post.toggleLike();
        holder.likesCountTextView.setText(context.getString(R.string.likes_count,
                post.getLikesCount()));
        if (isLiked) {
            holder.feedBtnLike.setImageResource(R.drawable.btn_like_blue); // Highlight the like button
        } else {
            holder.feedBtnLike.setImageResource(R.drawable.btn_like); // Revert to default like button
        }
    }

    // Starts an activity for commenting on a post
    private void startCommentActivity(int position) {
        Intent intent = new Intent(context, CommentActivity.class);
        intent.putExtra("postPosition", position); // Pass the position of the post being commented on
        context.startActivity(intent);
    }

    // Method to load images from a URL into an ImageView using a library like Glide or Picasso
    public void loadImage(ImageView imageView, String imageUrl) {
        Glide.with(context)
                .load(imageUrl)
                .placeholder(R.drawable.defaultpic) // Default placeholder if the image is being loaded
                .error(R.drawable.saved) // Error drawable if the image fails to load
                .into(imageView);
    }

    // Method to set an image from drawable resources by its name
    public void setImageFromDrawableName(ImageView imageView, String drawableName) {
        if (drawableName != null && !drawableName.isEmpty()) {
            Resources resources = context.getResources();
            @SuppressLint("DiscouragedApi") int resourceId = resources.getIdentifier(drawableName, "drawable", context.getPackageName());
            imageView.setImageResource(resourceId != 0 ? resourceId : R.drawable.defaultpic);
        } else {
            imageView.setImageResource(R.drawable.defaultpic); // Default image if drawable name is not provided
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setPosts(List<Post> posts) {
        this.posts = posts;
        notifyDataSetChanged();
    }

    private void showShareMenu(View view) {
        PopupMenu shareMenu = new PopupMenu(view.getContext(), view);
        shareMenu.inflate(R.menu.share_button_menu);
        shareMenu.setOnMenuItemClickListener(item -> false); // Handling of share actions is not implemented here
        shareMenu.show();
    }

    @Override
    public int getItemCount() {
        return posts.size(); // Return the total number of posts
    }

    // Displays a share menu for a post
    private void showPostMenu(View view, PostItemListener listener, int position) {
        PopupMenu postMenu = new PopupMenu(view.getContext(), view);
        postMenu.inflate(R.menu.post_menu); // Assume a menu resource exists for post options
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

    // Removes a post from the adapter based on its ID and notifies the RecyclerView
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

    // Interface for post item interaction listeners
    public interface PostItemListener {
        void onEdit(Post post);

        void onDelete(String postId);

        void onLike(String postId);
    }

    // ViewHolder class to manage the layout of each post item
    static class PostViewHolder extends RecyclerView.ViewHolder {
        // Declare UI components for the post
        TextView userNameTextView, timeStampTextView, postContentTextView, likesCountTextView,
                commentsCountTextView;
        ImageView profileImageView, postImageView;
        ImageButton feedCommentBtn, feedBtnLike, editPostMenu, shareButton;

        PostViewHolder(View itemView) {
            super(itemView);
            // Initialize UI components
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