package com.example.easycontacts.network;

import com.example.easycontacts.model.Contact;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by xkrej63 on 29.05.2018.
 */

public interface ContactService {

    @GET("contact")
    Call<List<Contact>> listContacts();

    @POST("contact")
    Call<Contact> createContact(@Body Contact contact);
}
