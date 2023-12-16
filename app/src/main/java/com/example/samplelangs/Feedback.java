package com.example.samplelangs;

import androidx.annotation.NonNull;

public class Feedback {
    private String username;
    private String message;
    private int rate;

    public Feedback(String username, String messsage, int rate) {
        this.username = username;
        this.message = messsage;
        this.rate = rate;
    }

    public String getUsername() {
        return username;
    }

    public String getFeedbackName() {
        return message;
    }

    public double getRate() {
        return rate;
    }

    @NonNull
    @Override
    public String toString() {
        return "Feedback: " + getFeedbackName() + ", Rating: " + getRate();
    }

}
