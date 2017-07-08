package com.bpk.rewards.model;

public class Statistics {
    private int diceCount;
    public static final String FIREBASE_STATISTICS_ROOT = "statistics";

    public int getDiceCount() {
        return diceCount;
    }

    public void setDiceCount(int diceCount) {
        this.diceCount = diceCount;
    }
}
