package com.example.easycontacts.model;

/**
 * Created by xkrej63 on 29.05.2018.
 */

public class Address {

    private String type;
    private String address;

    public Address(String type, String address) {
        this.type = type;
        this.address = address;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
