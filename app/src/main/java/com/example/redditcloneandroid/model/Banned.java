package com.example.redditcloneandroid.model;

import java.time.LocalDate;

public class Banned {

    private int bannedId;
    private String timestamp;
    private int byUser;
    private int community;
    private String bannedReason;

    public Banned(int bannedId, String timestamp, int byUser, int community, String bannedReason) {
        this.bannedId = bannedId;
        this.timestamp = timestamp;
        this.byUser = byUser;
        this.community = community;
        this.bannedReason = bannedReason;
    }

    public Banned(){

    }

    public int getBannedId() {
        return bannedId;
    }

    public void setBannedId(int bannedId) {
        this.bannedId = bannedId;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public int getByUser() {
        return byUser;
    }

    public void setByUser(int byUser) {
        this.byUser = byUser;
    }

    public int getCommunity() {
        return community;
    }

    public void setCommunity(int community) {
        this.community = community;
    }

    public String getBannedReason() {
        return bannedReason;
    }

    public void setBannedReason(String bannedReason) {
        this.bannedReason = bannedReason;
    }
}
