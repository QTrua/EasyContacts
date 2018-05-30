package com.example.easycontacts.model.db;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.example.easycontacts.model.api.Address;
import com.example.easycontacts.model.api.Email;
import com.example.easycontacts.model.api.Phone;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by xkrej63 on 30.05.2018.
 */

@Entity
public class Contact implements Serializable {

    @NonNull
    @PrimaryKey
    private String UUID = "";

    @ColumnInfo(name = "first_name")
    private String firstName;

    @ColumnInfo(name = "last_name")
    private String lastName;

    @ColumnInfo(name = "phone")
    private String phone;

    @ColumnInfo(name = "email")
    private String email;

    @ColumnInfo(name = "address")
    private String address;

    public static Contact fromApiContact(com.example.easycontacts.model.api.Contact apiContact) {
        Contact contact = new Contact();

        contact.setUUID(apiContact.getUUID());
        contact.setFirstName(apiContact.getFirstName());
        contact.setLastName(apiContact.getLastName());

        if (apiContact.getPhones() != null && apiContact.getPhones().size() != 0) {
            contact.setPhone(apiContact.getPhones().get(0).getPhone());
        }

        if (apiContact.getEmails() != null && apiContact.getEmails().size() != 0) {
            contact.setEmail(apiContact.getEmails().get(0).getEmail());
        }

        if (apiContact.getAddresses() != null && apiContact.getAddresses().size() != 0) {
            contact.setAddress(apiContact.getAddresses().get(0).getAddress());
        }

        return contact;
    }

    public String getUUID() {
        return UUID;
    }

    public void setUUID(String UUID) {
        this.UUID = UUID;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
    public String getDisplayName() {
        List<String> parts = new ArrayList<>();

        if (!TextUtils.isEmpty(getFirstName())) {
            parts.add(getFirstName());
        }

        if (!TextUtils.isEmpty(getLastName())) {
            parts.add(getLastName());
        }

        return TextUtils.join(" ", parts);
    }

    public String getPrimaryContactInfo() {
        if (!TextUtils.isEmpty(getPhone())) {
            return getPhone();
        }

        if (!TextUtils.isEmpty(getEmail())) {
            return getEmail();
        }

        if (!TextUtils.isEmpty(getAddress())) {
            return getAddress();
        }

        return null;
    }

    public void update(String firstName, String lastName, String phoneNumber, String emailAddress, String addressString) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phoneNumber;
        this.email = emailAddress;
        this.address = addressString;
    }

}
