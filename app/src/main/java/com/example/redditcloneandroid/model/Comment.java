package com.example.redditcloneandroid.model;

import java.time.LocalDate;

public class Comment {

    private int commentId;
    private String text;
    private String timestamp;
    private int user;
    private int post;

    public Comment(int commentId, String text, String timestamp, int user, int post) {
        this.commentId = commentId;
        this.text = text;
        this.timestamp = timestamp;
        this.user = user;
        this.post = post;
    }

    public Comment(){

    }

    public int getCommentId() {
        return commentId;
    }

    public void setCommentId(int commentId) {
        this.commentId = commentId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public int getUser() {
        return user;
    }

    public void setUser(int user) {
        this.user = user;
    }

    public int getPost() {
        return post;
    }

    public void setPost(int post) {
        this.post = post;
    }
}
