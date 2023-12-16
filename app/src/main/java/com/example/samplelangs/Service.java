package com.example.samplelangs;

import androidx.annotation.NonNull;

public class Service {
    private String username;
    private String serviceName;
    private double cost;

    public Service(String username, String serviceName, double cost) {
        this.username = username;
        this.serviceName = serviceName;
        this.cost = cost;
    }

    public String getUsername() {
        return username;
    }

    public String getServiceName() {
        return serviceName;
    }

    public double getCost() {
        return cost;
    }
    @NonNull
    @Override
    public String toString() {
        return "Service: " + getServiceName() + ", Cost: " + getCost();
    }

}
