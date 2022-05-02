package com.nasir.ratelimit;

public class UserLimit {
    private String userName;
    private Long rateLimit;

    public UserLimit() {
    }

    public UserLimit(String userName, Long rateLimit) {
        this.userName = userName;
        this.rateLimit = rateLimit;
    }

    public String getUserName() {
        return userName;
    }

    public Long getRateLimit() {
        return rateLimit;
    }
}
