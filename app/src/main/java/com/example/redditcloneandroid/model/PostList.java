package com.example.redditcloneandroid.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PostList {

    @SerializedName("results")
    private List<Post> posts;

    public PostList(List<Post> posts) {
        this.posts = posts;
    }

    public PostList(){

    }

    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }
}
