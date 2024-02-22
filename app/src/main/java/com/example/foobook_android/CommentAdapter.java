package com.example.foobook_android.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foobook_android.Comment;
import com.example.foobook_android.FieldValidation;
import com.example.foobook_android.R;

import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {

    private Context context;
    private List<Comment> comments;
    private final LayoutInflater mInflater;
    private CommentItemListener listener; // Listener for delete button

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
        Comment comment = comments.get(position);
        holder.commenterNameTextView.setText(comment.getCommenterName());
        holder.commentText.setText(comment.getCommentText());

        holder.commentMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Creating the instance of comment menu
                PopupMenu commentMenu = new PopupMenu(context, holder.commentMenu);
                // Inflating the menu using xml file
                commentMenu.getMenuInflater().inflate(R.menu.comment_menu, commentMenu.getMenu());

                commentMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        // edit comment
                        if (item.getItemId() == R.id.menuEditComment) {
                            // Enable EditText for editing
                            holder.commentText.setFocusable(true);
                            holder.commentText.setClickable(true);
                            holder.commentText.setFocusableInTouchMode(true);
                            holder.commentText.requestFocus();
                            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.showSoftInput(holder.commentText, InputMethodManager.SHOW_IMPLICIT);
                            FieldValidation.setupFieldValidation(holder.commentText);
                            // Show save button

                            holder.commentItemSaveBtn.setVisibility(View.VISIBLE);
                        } else if (item.getItemId() == R.id.menuDeleteComment) {
                            // delete comment
                            listener.onDeleteComment(position);
                        }
                        return true;
                    }
                });

                commentMenu.show(); //showing comment menu
            }
        });


        // save button for the comment edit
        holder.commentItemSaveBtn.setOnClickListener(v -> {
            if (!holder.commentText.getText().toString().isEmpty()) {


                // Update the comment object with the new text
                comment.setCommentText(holder.commentText.getText().toString());

                // Make EditText non-editable again
                holder.commentText.setFocusable(false);
                holder.commentText.setFocusableInTouchMode(false); // important for non-touch screen devices
                holder.commentText.setClickable(false);

                // Optionally, hide the keyboard
                InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(holder.commentText.getWindowToken(), 0);

                // Hide the save button
                holder.commentItemSaveBtn.setVisibility(View.GONE);

                // Notify the adapter that the item has changed to refresh the item view
                notifyItemChanged(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    public static class CommentViewHolder extends RecyclerView.ViewHolder {
        TextView commenterNameTextView;
        EditText commentText;
        ImageView commenterProfileImageView;
        ImageButton commentMenu;
        ImageButton commentItemSaveBtn;
        Button menuEditComment;


        CommentViewHolder(View itemView) {
            super(itemView);
            commenterNameTextView = itemView.findViewById(R.id.commentItemUsername);
            commentText = itemView.findViewById(R.id.commentItemComment);
            commenterProfileImageView = itemView.findViewById(R.id.commentItemImage);
            commentMenu = itemView.findViewById(R.id.commentMenu);
            menuEditComment = itemView.findViewById(R.id.menuEditComment);
            commentItemSaveBtn = itemView.findViewById(R.id.commentItemSaveBtn);

        }
    }
}