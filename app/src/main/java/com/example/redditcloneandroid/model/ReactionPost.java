package com.example.redditcloneandroid.model;

import com.example.redditcloneandroid.model.enums.ReactionType;

import java.time.LocalDate;

public class ReactionPost {

    private int reactionId;
    private ReactionType reactionType;
    private String timestamp;
    private int userId;
    private int postId;

    public ReactionPost(int reactionId, ReactionType reactionType, String timestamp, int userId, int postId) {
        this.reactionId = reactionId;
        this.reactionType = reactionType;
        this.timestamp = timestamp;
        this.userId = userId;
        this.postId = postId;
    }

    public ReactionPost(){

    }

    public int getReactionId() {
        return reactionId;
    }

    public void setReactionId(int reactionId) {
        this.reactionId = reactionId;
    }

    public ReactionType getReactionType() {
        return reactionType;
    }

    public void setReactionType(ReactionType reactionType) {
        this.reactionType = reactionType;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }
}
