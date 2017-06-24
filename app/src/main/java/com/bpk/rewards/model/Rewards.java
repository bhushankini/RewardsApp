package com.bpk.rewards.model;
/**
 * Created by bkini on 5/28/17.
 */

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Rewards {

    private String display;
    private int icon;
    private Long value;
    public static final String FIREBASE_REWARDS_ROOT = "rewards";

    public Rewards() {
    }

    public Rewards(String display, int icon, Long value) {
       this.display = display;
        this.icon = icon;
        this.value = value;

    }

    public String getDisplay() {
        return display;
    }

    public void setDisplay(String display) {
        this.display = display;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public Long getValue() {
        return value;
    }

    public void setValue(Long value) {
        this.value = value;
    }
}