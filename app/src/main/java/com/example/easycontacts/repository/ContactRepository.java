package com.example.easycontacts.repository;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.SharedPreferences;
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

    public ContactRepository(Context context) {
        SharedPreferences preferences = context.
                getSharedPreferences(MainActivity.SHARED_PREF_KEY, Context.MODE_PRIVATE);
        String userId = preferences.getString(MainActivity.KEY_USER_ID, null);
        networkManager = new NetworkManager(userId);

        db = Room.databaseBuilder(context, AppDatabase.class, "contacts")
                .allowMainThreadQueries()
                .build();
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
