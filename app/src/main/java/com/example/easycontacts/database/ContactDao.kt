package com.example.easycontacts.database

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Delete
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import android.arch.persistence.room.Update

import com.example.easycontacts.model.db.Contact

/**
 * Created by xkrej63 on 30.05.2018.
 */

@Dao
interface ContactDao {

    @Query("SELECT * FROM contact")
    fun listContacts(): List<Contact>

    @Update
    fun updateContact(contact: Contact)

    @Insert
    fun insert(contact: Contact)

    @Query("DELETE FROM contact")
    fun deleteAll()

    @Insert
    fun insertAll(contacts: List<Contact>)

    @Delete
    fun delete(contact: Contact)
}
