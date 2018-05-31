package com.example.easycontacts.repository;

import android.arch.persistence.room.Room;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import com.example.easycontacts.database.AppDatabase;
import com.example.easycontacts.model.db.Contact;
import com.example.easycontacts.network.NetworkManager;
import com.example.easycontacts.screen.MainActivity;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by xkrej63 on 30.05.2018.
 */

public class ContactRepository {

    private NetworkManager networkManager;

    private AppDatabase db;

    private ContentResolver contentResolver;

    public ContactRepository(Context context) {
        SharedPreferences preferences = context.
                getSharedPreferences(MainActivity.SHARED_PREF_KEY, Context.MODE_PRIVATE);
        String userId = preferences.getString(MainActivity.KEY_USER_ID, null);
        networkManager = new NetworkManager(userId);

        db = Room.databaseBuilder(context, AppDatabase.class, "contacts")
                .allowMainThreadQueries()
                .build();

        contentResolver = context.getContentResolver();
    }

    public void listContacts(final ContactCallback callback) {
        networkManager.getContactService()
                .listContacts()
                .enqueue(new Callback<List<com.example.easycontacts.model.api.Contact>>() {
                    @Override
                    public void onResponse(Call<List<com.example.easycontacts.model.api.Contact>> call, Response<List<com.example.easycontacts.model.api.Contact>> response) {
                        List<com.example.easycontacts.model.api.Contact> contacts = response.body();
                        List<Contact> dbContacts = new ArrayList<>();
                        for (com.example.easycontacts.model.api.Contact contact: contacts) {
                            dbContacts.add(Contact.fromApiContact(contact));
                        }

                        db.contactDao().deleteAll();
                        db.contactDao().insertAll(dbContacts);

                        callback.withContacts(dbContacts);
                    }

                    @Override
                    public void onFailure(@NonNull Call<List<com.example.easycontacts.model.api.Contact>> call, Throwable t) {
                        List<Contact> contacts = db.contactDao().listContacts();
                        callback.withContacts(contacts);
                    }
                });
    }

    public void listLocalContacts(ContactCallback callback) {
        Cursor cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null,
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC");

        List<Contact> contacts = new ArrayList<>();
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                String id = cursor.getString(
                        cursor.getColumnIndex(ContactsContract.Contacts._ID));
                String name = cursor.getString(
                        cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

                Contact contact = new Contact();

                contact.setUUID(id);
                contact.setFirstName(name);

                Cursor phoneCursor = contentResolver.query(
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                        new String[]{id},
                        null
                );

                if (phoneCursor.moveToNext()) {
                    String phoneNumber = phoneCursor.getString(
                            phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    contact.setPhone(phoneNumber);
                }

                phoneCursor.close();

                Cursor emailCursor = contentResolver.query(
                        ContactsContract.CommonDataKinds.Email.CONTENT_URI,
                        null,
                        ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?",
                        new String[]{id},
                        null
                );

                if (emailCursor.moveToNext()) {
                    String email = emailCursor.getString(
                            emailCursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.ADDRESS)
                    );
                    contact.setEmail(email);
                }
                emailCursor.close();

                Cursor addressCursor = contentResolver.query(
                        ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_URI,
                        null,
                        ContactsContract.CommonDataKinds.StructuredPostal.CONTACT_ID + " = ?",
                        new String[]{id},
                        null
                );

                if (addressCursor.moveToNext()) {
                    String address = addressCursor.getString(
                            addressCursor.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.FORMATTED_ADDRESS)
                    );
                    contact.setAddress(address);
                }

                addressCursor.close();

                contacts.add(contact);
            }
        }

        cursor.close();

        callback.withContacts(contacts);
    }

    public void saveContact(Contact contact, final SaveCallback callback) {
        com.example.easycontacts.model.api.Contact apiContact = com.example.easycontacts.model.api.Contact.fromDbContact(contact);
        Call<com.example.easycontacts.model.api.Contact> call;
        if (TextUtils.isEmpty(apiContact.getUUID())) {
            call = networkManager.getContactService()
                    .createContact(apiContact);
        } else {
            call = networkManager.getContactService()
                    .updateContact(apiContact.getUUID(), apiContact);
        }

        call.enqueue(new Callback<com.example.easycontacts.model.api.Contact>() {
            @Override
            public void onResponse(@NonNull Call<com.example.easycontacts.model.api.Contact> call, @NonNull Response<com.example.easycontacts.model.api.Contact> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess();
                } else {
                    callback.onError(null);
                }
            }

            @Override
            public void onFailure(@NonNull Call<com.example.easycontacts.model.api.Contact> call, @NonNull Throwable t) {
                callback.onError(t);
            }
        });
    }

    public void deleteContact(String contactId, final SaveCallback callback) {
        networkManager.getContactService()
                .deleteContact(contactId)
                .enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful()) {
                            callback.onSuccess();
                        } else {
                            callback.onError(null);
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        callback.onError(t);
                    }
                });
    }

    public interface ContactCallback {
        void withContacts(List<Contact> contacts);
    }

    public interface SaveCallback {
        void onSuccess();
        void onError(Throwable t);
    }
}
