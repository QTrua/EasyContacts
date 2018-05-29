package com.example.easycontacts.model;

import java.util.Date;
import java.util.List;

/**
 * Created by xkrej63 on 29.05.2018.
 */

public class Contact {
    private String UUID;

    private String firstName;
    private String lastName;

    private List<Email> emails;
    private List<Address> addresses;
    private List<Phone> phones;

    private Date birthday;
    private String note;
    private String organization;

    public Contact(String UUID, String firstName, String lastName, List<Email> emails, List<Address> addresses, List<Phone> phones, Date birthday, String note, String organization) {
        this.UUID = UUID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.emails = emails;
        this.addresses = addresses;
        this.phones = phones;
        this.birthday = birthday;
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

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
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
