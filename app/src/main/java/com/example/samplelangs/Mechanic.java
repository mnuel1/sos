package com.example.samplelangs;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class Mechanic implements Serializable {
    private String owner;
    private String garageName;
    private String username;
    private String password;
    private String reenter;
    private String address;
    private String contact;

    private String vehicleType;

    public Mechanic(String owner, String garageName, String username,
                    String password, String reenter, String address,
                    String contact) {
        this.owner = owner;
        this.garageName = garageName;
        this.username = username;
        this.password = password;
        this.reenter = reenter;
        this.address = address;
        this.contact = contact;
    }

    public Mechanic(String username, String owner, String garageName, String address,
                    String contact, String vehicleType) {
        this.username = username;
        this.owner = owner;
        this.garageName = garageName;
        this.address = address;
        this.contact = contact;
        this.vehicleType = vehicleType;
    }

    public String getOwner() {
        return owner;
    }
    public String getGarageName() {
        return garageName;
    }
    public String getUsername() {
        return username;
    }
    public String getPassword() {
        return password;
    }
    public String getReenter() {
        return reenter;
    }
    public String getAddress() {
        return address;
    }
    public String getContact() {
        return contact;
    }
    public String getVehicleType() {
        return vehicleType;
    }


}
