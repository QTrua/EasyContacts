package com.example.easycontacts.model.api;

import android.text.TextUtils;

import com.squareup.moshi.Json;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
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

    public static Contact fromDbContact(com.example.easycontacts.model.db.Contact dbContact) {
        Contact contact = new Contact();

        contact.setUUID(dbContact.getUUID());
        contact.setFirstName(dbContact.getFirstName());
        contact.setLastName(dbContact.getLastName());

        if (!TextUtils.isEmpty(dbContact.getPhone())) {
            Phone phone = new Phone("personal", dbContact.getPhone());
            contact.setPhones(Collections.singletonList(phone));
        }

        if (!TextUtils.isEmpty(dbContact.getEmail())) {
            Email email = new Email("personal", dbContact.getEmail());
            contact.setEmails(Collections.singletonList(email));
        }

        if (!TextUtils.isEmpty(dbContact.getAddress())) {
            Address address = new Address("home", dbContact.getAddress());
            contact.setAddresses(Collections.singletonList(address));
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
}
