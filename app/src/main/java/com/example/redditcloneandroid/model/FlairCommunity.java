package com.example.redditcloneandroid.model;

public class FlairCommunity {

    private int flairId;
    private String name;
    private Community communities;
    private String active;


    public FlairCommunity(int flairId, String name, Community communities, String active) {
        this.flairId = flairId;
        this.name = name;
        this.communities = communities;
        this.active = active;
    }

    public FlairCommunity() {

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

    public Community getCommunities() {
        return communities;
    }

    public void setCommunities(Community communities) {
        this.communities = communities;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }
}
