package com.example.redditcloneandroid.model;

import com.google.gson.annotations.SerializedName;

import java.time.LocalDate;

public class Post {

    @SerializedName("postId")
    private int postId;

    @SerializedName("title")
    private String title;

    @SerializedName("text")
    private String text;

    @SerializedName("creationDate")
    private String creationDate;

    @SerializedName("imagePath")
    private String imagePath;

    @SerializedName("community")
    private int community;

    @SerializedName("user")
    private int user;

    @SerializedName("flair")
    private int flair;

    @SerializedName("active")
    private String active;


    public Post(int postId, String title, String text, String creationDate, String imagePath, int flair, int community, int user, String active) {
        this.postId = postId;
        this.title = title;
        this.text = text;
        this.creationDate = creationDate;
        this.imagePath = imagePath;
        this.community = community;
        this.flair = flair;
        this.user = user;
        this.active = active;
    }

    public Post(){

    }

    public int getFlair() {
        return flair;
    }

    public void setFlair(int flair) {
        this.flair = flair;
    }

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public int getCommunity() {
        return community;
    }

    public void setCommunity(int community) {
        this.community = community;
    }

    public int getUser() {
        return user;
    }

    public void setUser(int user) {
        this.user = user;
    }
}





