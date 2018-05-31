package com.example.easycontacts.network

import com.example.easycontacts.model.api.Contact

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

/**
 * Created by xkrej63 on 29.05.2018.
 */

interface ContactService {

    @GET("contact")
    fun listContacts(): Call<List<Contact>>

    @POST("contact")
    fun createContact(@Body contact: Contact): Call<Contact>

    @PATCH("contact/{contact_id}")
    fun updateContact(@Path("contact_id") contactId: String, @Body contact: Contact): Call<Contact>

    @DELETE("contact/{contact_id}")
    fun deleteContact(@Path("contact_id") contactId: String): Call<Void>
}
