package com.bpk.rewards.model;
/**
 * Created by bkini on 5/28/17.
 */

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class User {

    private String name;
    private String userId;
    private int points;
    private long lastopen;
    private String email;
    public static final String FIREBASE_USER_ROOT = "users";

    // Default constructor required for calls to
    // DataSnapshot.getValue(User.class)
    public User() {
    }

    public User(String userId, String name, int points, long lastopen) {
        this.name = name;
        this.userId = userId;
        this.points = points;
        this.lastopen = lastopen;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setLastopen(long lastopen) {
        this.lastopen = lastopen;
    }

    public long getLastopen() {
        return lastopen;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }
}