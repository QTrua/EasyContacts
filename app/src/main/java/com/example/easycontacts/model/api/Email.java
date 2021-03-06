package com.example.easycontacts.model.api;

import android.arch.persistence.room.Entity;

/**
 * Created by xkrej63 on 29.05.2018.
 */

@Entity
public class Email {
    public String type;
    public String email;

    public Email(String type, String email) {
        this.type = type;
        this.email = email;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
