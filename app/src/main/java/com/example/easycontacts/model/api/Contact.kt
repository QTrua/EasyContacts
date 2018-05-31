package com.example.easycontacts.model.api

import android.text.TextUtils

import com.squareup.moshi.Json

import java.io.Serializable
import java.util.ArrayList
import java.util.Collections

/**
 * Created by xkrej63 on 29.05.2018.
 */

class Contact(
        @Json(name = "uuid") var uuid: String,
        @Json(name = "first_name") var firstName: String,
        @Json(name = "last_name") var lastName: String,
        @Json(name = "emails") var emails: List<Email>,
        @Json(name = "addresses") var addresses: List<Address>,
        @Json(name = "phones") var phones: List<Phone>,
        @Json(name = "note") var note: String,
        @Json(name = "organization") var organization: String) {


    companion object {

        fun fromDbContact(dbContact: com.example.easycontacts.model.db.Contact): Contact {

            val phones = if (!TextUtils.isEmpty(dbContact.phone)) {
                val phone = Phone("personal", dbContact.phone)
                listOf(phone)
            } else {
                listOf()
            }

            val emails = if (!TextUtils.isEmpty(dbContact.email)) {
                val email = Email("personal", dbContact.email)
                listOf(email)
            } else {
                listOf()
            }

            val addresses = if (!TextUtils.isEmpty(dbContact.address)) {
                val address = Address("home", dbContact.address)
                listOf(address)
            } else {
                listOf()
            }

            return Contact(
                    uuid = dbContact.uuid, firstName = dbContact.firstName, lastName = dbContact.lastName,
                    phones = phones, emails = emails, addresses = addresses,
                    note = "",
                    organization = ""
            )
        }
    }
}
