package com.example.easycontacts.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

/**
 * Created by xkrej63 on 29.05.2018.
 */

class NetworkManager(userId: String) {

    private val retrofit: Retrofit

    val contactService: ContactService

    init {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .build()

        val baseUrl = "https://contactee.jankrecek.cz/$userId/"
        retrofit = Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(okHttpClient)
                .addConverterFactory(MoshiConverterFactory.create())
                .build()

        contactService = retrofit.create(ContactService::class.java)
    }
}
