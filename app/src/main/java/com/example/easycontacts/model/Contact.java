package com.example.easycontacts.model;

import android.text.TextUtils;

import com.squareup.moshi.Json;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by xkrej63 on 29.05.2018.
 */

public class Contact {
    @Json(name = "uuid")
    public String UUID;

    @Json(name = "first_name")
    public String firstName;

    @Json(name = "last_name")
    public String lastName;

    @Json(name = "emails")
    public List<Email> emails;

    @Json(name = "addresses")
    public List<Address> addresses;

    @Json(name = "phones")
    public List<Phone> phones;

    @Json(name = "note")
    public String note;

    @Json(name = "organization")
    public String organization;

    public Contact() {}

    public Contact(String UUID, String firstName, String lastName, List<Email> emails, List<Address> addresses, List<Phone> phones, String note, String organization) {
        this.UUID = UUID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.emails = emails;
        this.addresses = addresses;
        this.phones = phones;
        this.note = note;
        this.organization = organization;
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

    public List<Email> getEmails() {
        return emails;
    }

    public void setEmails(List<Email> emails) {
        this.emails = emails;
    }

    public List<Address> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<Address> addresses) {
        this.addresses = addresses;
    }

    public List<Phone> getPhones() {
        return phones;
    }

    public void setPhones(List<Phone> phones) {
        this.phones = phones;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
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
        if (getPhones() != null && getPhones().size() != 0) {
            return getPhones().get(0).getPhone();
        }

        if (getEmails() != null && getEmails().size() != 0) {
            return getEmails().get(0).getEmail();
        }

        if (getAddresses() != null && getAddresses().size() != 0) {
            return getAddresses().get(0).getAddress();
        }

        return null;
    }
}
