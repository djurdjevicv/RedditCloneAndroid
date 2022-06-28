package com.example.redditcloneandroid.model;

import com.google.gson.annotations.SerializedName;

import java.time.LocalDate;

public class Community {

    @SerializedName("communityId")
    private int communityId;

    @SerializedName("name")
    private String name;

    @SerializedName("description")
    private String description;

    @SerializedName("creationDate")
    private String creationDate;

    @SerializedName("moderator")
    private int moderator;

    @SerializedName("active")
    private String active;

    public Community(int communityId, String name, String description, String creationDate, int moderator, String active) {
        this.communityId = communityId;
        this.name = name;
        this.description = description;
        this.creationDate = creationDate;
        this.moderator = moderator;
        this.active = active;
    }

    public Community(){

    }

    public int getCommunityId() {
        return communityId;
    }

    public void setCommunityId(int communityId) {
        this.communityId = communityId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public int getModerator() {
        return moderator;
    }

    public void setModerator(int moderator) {
        this.moderator = moderator;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }
}
