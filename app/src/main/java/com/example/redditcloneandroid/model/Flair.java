package com.example.redditcloneandroid.model;

public class Flair {

    private int flairId;
    private String name;
    private int communityId;
    private String active;

    public Flair(int flairId, String name, int communityId, String active) {
        this.flairId = flairId;
        this.name = name;
        this.communityId = communityId;
        this.active = active;
    }

    public Flair(){

    }

    public int getFlairId() {
        return flairId;
    }

    public void setFlairId(int flairId) {
        this.flairId = flairId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCommunityId() {
        return communityId;
    }

    public void setCommunityId(int communityId) {
        this.communityId = communityId;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }
}
