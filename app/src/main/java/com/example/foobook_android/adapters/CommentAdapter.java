package com.example.foobook_android.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foobook_android.comment.Comment;
import com.example.foobook_android.utility.FieldValidation;
import com.example.foobook_android.R;

import java.util.List;

/**
 * CommentAdapter is a RecyclerView adapter that handles displaying comments in a list.
 * It supports basic operations like viewing, editing, and deleting comments through
 * a listener interface. This adapter uses a custom ViewHolder to manage the layout of
 * each comment item.
 */
public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {

    private final Context context;
    private final List<Comment> comments; // List of comments to display
    private final LayoutInflater mInflater; // LayoutInflater to inflate the view for each comment item

    // Listener for handling actions on each comment item
    private final CommentItemListener listener;

    // Interface for defining the actions on comment items
    public interface CommentItemListener {
        void onDeleteComment(int position); // Method to handle deletion of a comment
    }

    // Constructor for the adapter
    public CommentAdapter(Context context, List<Comment> comments, CommentItemListener listener) {
        this.context = context;
        this.comments = comments;
        this.mInflater = LayoutInflater.from(context); // Initialize LayoutInflater
        this.listener = listener; // Set the listener for comment actions
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the comment item layout
        View view = mInflater.inflate(R.layout.comment_item, parent, false);
        return new CommentViewHolder(view); // Return a new instance of the ViewHolder
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        // Bind data to the ViewHolder
        holder.bind(comments.get(position), position);
    }

    @Override
    public int getItemCount() {
        // Return the size of the comments list
        return comments.size();
    }

    /**
     * ViewHolder class for comment items. Holds references to the UI components and
     * binds data to each comment item. It also handles the edit and delete actions.
     */
    public class CommentViewHolder extends RecyclerView.ViewHolder {
        TextView commenterNameTextView;
        EditText commentText;
        ImageView commenterProfileImageView;
        ImageButton commentMenu;
        ImageButton commentItemSaveBtn;

        CommentViewHolder(View itemView) {
            super(itemView);
            // Initialize UI components
            commenterNameTextView = itemView.findViewById(R.id.commentItemUsername);
            commentText = itemView.findViewById(R.id.commentItemComment);
            commenterProfileImageView = itemView.findViewById(R.id.commentItemImage);
            commentMenu = itemView.findViewById(R.id.commentMenu);
            commentItemSaveBtn = itemView.findViewById(R.id.commentItemSaveBtn);
        }

        // Binds data to the ViewHolder components
        void bind(Comment comment, int position) {
            commenterNameTextView.setText(comment.getCommenterName());
            commentText.setText(comment.getCommentText());

            setupPopupMenu(position); // Setup the popup menu for edit and delete actions
            setupSaveButton(comment, position); // Setup the save button for saving edits
        }

        // Handles the setup and functionality of the PopupMenu
        private void setupPopupMenu(int position) {
            commentMenu.setOnClickListener(v -> {
                PopupMenu commentMenu = new PopupMenu(context, this.commentMenu);
                commentMenu.getMenuInflater().inflate(R.menu.comment_menu, commentMenu.getMenu());
                commentMenu.setOnMenuItemClickListener(item -> onMenuItemClick(item, position));
                commentMenu.show();
            });
        }

        // Handles menu item clicks for edit and delete actions
        private boolean onMenuItemClick(MenuItem item, int position) {
            if (item.getItemId() == R.id.menuEditComment) {
                makeCommentEditable(); // Make the comment editable
                return true;
            } else if (item.getItemId() == R.id.menuDeleteComment) {
                listener.onDeleteComment(position); // Notify listener to delete the comment
                return true;
            }
            return false;
        }


        // Prepares the EditText for editing and shows the save button
        private void makeCommentEditable() {
            commentText.setFocusable(true);
            commentText.setClickable(true);
            commentText.setFocusableInTouchMode(true);
            commentText.requestFocus();
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(commentText, InputMethodManager.SHOW_IMPLICIT);
            FieldValidation.setupFieldValidation(commentText); // Setup field validation if needed
            commentItemSaveBtn.setVisibility(View.VISIBLE); // Show the save button
        }

        // Configures the save button behavior for saving edits
        private void setupSaveButton(Comment comment, int position) {
            commentItemSaveBtn.setOnClickListener(v -> {
                if (!commentText.getText().toString().isEmpty()) {
                    comment.setCommentText(commentText.getText().toString());
                    disableEditing();
                    notifyItemChanged(position); // Notify adapter of item change to update UI
                }
            });
        }

        // Disables editing for the comment EditText and hides the keyboard
        private void disableEditing() {
            commentText.setFocusable(false);
            commentText.setFocusableInTouchMode(false);
            commentText.setClickable(false);
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(commentText.getWindowToken(), 0);
            commentItemSaveBtn.setVisibility(View.GONE); // Hide the save button after saving
        }
    }
}
