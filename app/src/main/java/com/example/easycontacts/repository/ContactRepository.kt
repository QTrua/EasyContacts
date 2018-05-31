package com.example.easycontacts.repository

import android.arch.persistence.room.Room
import android.content.ContentResolver
import android.content.Context
import android.content.SharedPreferences
import android.database.Cursor
import android.provider.ContactsContract
import android.text.TextUtils
import android.util.Log

import com.example.easycontacts.database.AppDatabase
import com.example.easycontacts.extension.getDefaultSharedPreferences
import com.example.easycontacts.extension.getStringByColumnIndex
import com.example.easycontacts.model.db.Contact
import com.example.easycontacts.network.NetworkManager
import com.example.easycontacts.screen.MainActivity

import java.util.ArrayList

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Created by xkrej63 on 30.05.2018.
 */

class ContactRepository(context: Context) {

    private val networkManager: NetworkManager

    private val db: AppDatabase

    private val contentResolver: ContentResolver

    init {
        val preferences = context.getDefaultSharedPreferences()
        val userId = preferences.getString(MainActivity.KEY_USER_ID, null)
        networkManager = NetworkManager(userId!!)

        db = Room.databaseBuilder(context, AppDatabase::class.java, "contacts")
                .allowMainThreadQueries()
                .build()

        contentResolver = context.contentResolver
    }

    fun listContacts(callback: ContactCallback) {
        networkManager.contactService
                .listContacts()
                .enqueue(object : Callback<List<com.example.easycontacts.model.api.Contact>> {
                    override fun onResponse(call: Call<List<com.example.easycontacts.model.api.Contact>>, response: Response<List<com.example.easycontacts.model.api.Contact>>) {
                        val dbContacts = response
                                .body()!!
                                .map { Contact.fromApiContact(it) }

                        db.contactDao().deleteAll()
                        db.contactDao().insertAll(dbContacts)

                        callback.withContacts(dbContacts)
                    }

                    override fun onFailure(call: Call<List<com.example.easycontacts.model.api.Contact>>, t: Throwable) {
                        val contacts = db.contactDao().listContacts()
                        callback.withContacts(contacts)
                    }
                })
    }

    fun listLocalContacts(callback: ContactCallback) {
        val cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null,
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC")

        val contacts = ArrayList<Contact>()
        if (cursor!!.count > 0) {
            while (cursor.moveToNext()) {
                val id = cursor.getStringByColumnIndex(ContactsContract.Contacts._ID)
                val name = cursor.getStringByColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)

                val contact = Contact()

                contact.uuid = id
                contact.firstName = name

                val phoneCursor = contentResolver.query(
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                        arrayOf(id), null
                )

                if (phoneCursor?.moveToNext() == true) {
                    contact.phone = phoneCursor
                            .getStringByColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
                }

                phoneCursor.close()

                val emailCursor = contentResolver.query(
                        ContactsContract.CommonDataKinds.Email.CONTENT_URI, null,
                        ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?",
                        arrayOf(id), null
                )

                if (emailCursor?.moveToNext() == true) {
                    contact.email = emailCursor
                            .getStringByColumnIndex(ContactsContract.CommonDataKinds.Email.ADDRESS)
                }
                emailCursor.close()

                val addressCursor = contentResolver.query(
                        ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_URI, null,
                        ContactsContract.CommonDataKinds.StructuredPostal.CONTACT_ID + " = ?",
                        arrayOf(id), null
                )

                if (addressCursor?.moveToNext() == true) {
                    contact.address = addressCursor
                            .getStringByColumnIndex(
                                    ContactsContract.CommonDataKinds.StructuredPostal.FORMATTED_ADDRESS)
                }

                addressCursor.close()

                contacts.add(contact)
            }
        }

        cursor.close()

        callback.withContacts(contacts)
    }

    fun saveContact(contact: Contact, callback: SaveCallback) {
        val apiContact = com.example.easycontacts.model.api.Contact.fromDbContact(contact)
        val call = if (TextUtils.isEmpty(apiContact.uuid)) {
            networkManager.contactService
                    .createContact(apiContact)
        } else {
            networkManager.contactService
                    .updateContact(apiContact.uuid, apiContact)
        }

        call.enqueue(object : Callback<com.example.easycontacts.model.api.Contact> {
            override fun onResponse(call: Call<com.example.easycontacts.model.api.Contact>, response: Response<com.example.easycontacts.model.api.Contact>) {
                if (response.isSuccessful) {
                    callback.onSuccess()
                } else {
                    callback.onError(null)
                }
            }

            override fun onFailure(call: Call<com.example.easycontacts.model.api.Contact>, t: Throwable) {
                callback.onError(t)
            }
        })
    }

    fun deleteContact(contactId: String, callback: SaveCallback) {
        networkManager.contactService
                .deleteContact(contactId)
                .enqueue(object : Callback<Void> {
                    override fun onResponse(call: Call<Void>, response: Response<Void>) {
                        if (response.isSuccessful) {
                            callback.onSuccess()
                        } else {
                            callback.onError(null)
                        }
                    }

                    override fun onFailure(call: Call<Void>, t: Throwable) {
                        callback.onError(t)
                    }
                })
    }

    interface ContactCallback {
        fun withContacts(contacts: List<Contact>)
    }

    interface SaveCallback {
        fun onSuccess()
        fun onError(t: Throwable?)
    }
}
