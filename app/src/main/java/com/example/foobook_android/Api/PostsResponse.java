package com.example.foobook_android.Api;

import com.example.foobook_android.post.Post;
import com.google.gson.annotations.SerializedName;
import java.util.List;

// Captures the response from the server when requesting posts. This response includes
// a message indicating the outcome of the request and a list of Post objects if successful.
public class PostsResponse {
    @SerializedName("message")
    private final String message;
    // List of posts returned by the server. These are serialized from JSON based on the "posts" key.
    @SerializedName("posts")
    private List<Post> posts;

    // Constructs a new PostsResponse with the specified message. Typically used for error handling.
    public PostsResponse(String message) {
        this.message = message;
    }

    // Retrieves the list of posts contained in this response.
    public List<Post> getPosts() {
        return posts;
    }

    // Returns the message associated with this response.
    public String getMessage() {
        return message;
    }

    // Sets the list of posts in this response. Used to populate the response with actual posts data.
    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }
}
