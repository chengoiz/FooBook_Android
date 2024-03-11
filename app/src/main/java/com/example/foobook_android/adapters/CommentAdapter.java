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

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {

    private Context context;
    private List<Comment> comments;
    private final LayoutInflater mInflater;
    // Interface for handling delete actions, adheres to OOP encapsulation principles.
    private CommentItemListener listener;

    public interface CommentItemListener {
        void onDeleteComment(int position);
    }

    public CommentAdapter(Context context, List<Comment> comments, CommentItemListener listener) {
        this.context = context;
        this.comments = comments;
        this.mInflater = LayoutInflater.from(context);
        this.listener = listener;
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.comment_item, parent, false);
        return new CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        // Moved the binding logic to a method within the ViewHolder
        holder.bind(comments.get(position), position);
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    public class CommentViewHolder extends RecyclerView.ViewHolder {
        TextView commenterNameTextView;
        EditText commentText;
        ImageView commenterProfileImageView;
        ImageButton commentMenu;
        ImageButton commentItemSaveBtn;

        CommentViewHolder(View itemView) {
            super(itemView);
            commenterNameTextView = itemView.findViewById(R.id.commentItemUsername);
            commentText = itemView.findViewById(R.id.commentItemComment);
            commenterProfileImageView = itemView.findViewById(R.id.commentItemImage);
            commentMenu = itemView.findViewById(R.id.commentMenu);
            commentItemSaveBtn = itemView.findViewById(R.id.commentItemSaveBtn);
        }

        // Encapsulates ViewHolder binding logic for cleaner onBindViewHolder method
        void bind(Comment comment, int position) {
            commenterNameTextView.setText(comment.getCommenterName());
            commentText.setText(comment.getCommentText());

            setupPopupMenu(comment, position);
            setupSaveButton(comment, position);
        }

        // Handles the setup and functionality of the PopupMenu
        private void setupPopupMenu(Comment comment, int position) {
            commentMenu.setOnClickListener(v -> {
                PopupMenu commentMenu = new PopupMenu(context, this.commentMenu);
                commentMenu.getMenuInflater().inflate(R.menu.comment_menu, commentMenu.getMenu());
                commentMenu.setOnMenuItemClickListener(item -> onMenuItemClick(item, comment, position));
                commentMenu.show();
            });
        }

        // Separates the logic for handling menu item clicks
        private boolean onMenuItemClick(MenuItem item, Comment comment, int position) {
            if (item.getItemId() == R.id.menuEditComment) {
                makeCommentEditable();
                return true;
            } else if (item.getItemId() == R.id.menuDeleteComment) {
                listener.onDeleteComment(position);
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
            FieldValidation.setupFieldValidation(commentText); // Assuming it configures validation
            commentItemSaveBtn.setVisibility(View.VISIBLE);
        }

        // Configures the save button behavior
        private void setupSaveButton(Comment comment, int position) {
            commentItemSaveBtn.setOnClickListener(v -> {
                if (!commentText.getText().toString().isEmpty()) {
                    comment.setCommentText(commentText.getText().toString());
                    disableEditing();
                    notifyItemChanged(position); // Notify adapter directly from ViewHolder for encapsulation
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
            commentItemSaveBtn.setVisibility(View.GONE);
        }
    }
}
