package com.example.foobook_android;

import java.util.List;

public class PostListWrapper {
    private List<Post> posts;

    // Getter and Setter
    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }
    public void addPost(Post newPost) {
        posts.add(newPost);
    }
}
