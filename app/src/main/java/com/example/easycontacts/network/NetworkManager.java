package com.example.easycontacts.network;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;

/**
 * Created by xkrej63 on 29.05.2018.
 */

public class NetworkManager {

    private Retrofit retrofit;

    private ContactService contactService;

    public NetworkManager() {
        retrofit = new Retrofit.Builder()
                .baseUrl("https://contactee.jankrecek.cz/test@example.com/")
                .addConverterFactory(MoshiConverterFactory.create())
                .build();

        contactService = retrofit.create(ContactService.class);
    }

    public ContactService getContactService() {
        return contactService;
    }
}
