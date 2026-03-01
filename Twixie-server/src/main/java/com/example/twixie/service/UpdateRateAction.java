package com.example.twixie.service;

public enum UpdateRateAction {
    INCREMENT_USER_ADD_POST(5),
    DECREMENT_USER_DELETE_POST(-5),
    LIKE(1),
    DISLIKE(-1),
    UNLIKE(-1),
    UNDISLIKE(1),
    FOLLOW(5),
    UNFOLLOW(-6),
    INCREMENT_TOPIC(2),
    DECREMENT_TOPIC(-2),
    DECREMENT_AGING(-1);

    private final int rateChange;

    UpdateRateAction(int rateChange) {
        this.rateChange = rateChange;
    }

    public int getRateChange() {
        return rateChange;
    }
}
