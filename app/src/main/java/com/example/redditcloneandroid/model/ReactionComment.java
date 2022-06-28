package com.example.redditcloneandroid.model;

import com.example.redditcloneandroid.model.enums.ReactionType;

public class ReactionComment {

    private int reactionId;
    private ReactionType reactionType;
    private String timestamp;
    private int userId;
    private int commentId;

    public ReactionComment(int reactionId, ReactionType reactionType, String timestamp, int userId, int commentId) {
        this.reactionId = reactionId;
        this.reactionType = reactionType;
        this.timestamp = timestamp;
        this.userId = userId;
        this.commentId = commentId;
    }

    public ReactionComment(){

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

    public int getCommentId() {
        return commentId;
    }

    public void setCommentId(int commentId) {
        this.commentId = commentId;
    }
}
