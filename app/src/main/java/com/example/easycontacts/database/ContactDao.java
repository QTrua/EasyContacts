package com.example.easycontacts.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.easycontacts.model.db.Contact;

import java.util.List;

/**
 * Created by xkrej63 on 30.05.2018.
 */

@Dao
public interface ContactDao {

    @Query("SELECT * FROM contact")
    List<Contact> listContacts();

    @Update
    void updateContact(Contact contact);

    @Insert
    void insert(Contact contact);

    @Query("DELETE FROM contact")
    void deleteAll();

    @Insert
    void insertAll(List<Contact> contacts);

    @Delete
    void delete(Contact contact);
}
