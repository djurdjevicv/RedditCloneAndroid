package com.example.redditcloneandroid.model;

public class Rule {

    private int ruleId;
    private String description;
    private int community;

    public Rule(int ruleId, String description, int community) {
        this.ruleId = ruleId;
        this.description = description;
        this.community = community;
    }

    public Rule(){

    }

    public int getRuleId() {
        return ruleId;
    }

    public void setRuleId(int ruleId) {
        this.ruleId = ruleId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getCommunity() {
        return community;
    }

    public void setCommunity(int community) {
        this.community = community;
    }

}
