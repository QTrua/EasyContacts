package com.example.easycontacts.model.api;

import com.squareup.moshi.Json;

/**
 * Created by xkrej63 on 29.05.2018.
 */

public class Phone {
    public String type;

    @Json(name = "phone_number")
    public String phone;

    public Phone(String type, String address) {
        this.type = type;
        this.phone = address;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
