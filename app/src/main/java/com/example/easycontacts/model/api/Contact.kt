package com.example.easycontacts.model.api

import android.text.TextUtils

import com.squareup.moshi.Json

import java.io.Serializable
import java.util.ArrayList
import java.util.Collections

/**
 * Example JSON.
 *    {
 *        "uuid":"73de2853-e0b2-4b26-9897-ce7f224a7432",
 *        "title_before":"Ing.",
 *        "first_name":"Jana",
 *        "middle_name":"Nova",
 *        "last_name":"Novakova",
 *        "title_after":"CSc.",
 *        "emails":[
 *            {
 *                "type":"personal",
 *                "email":"jana.novakova@seznam.cz"
 *            }
 *        ],
 *        "addresses":[
 *            {
 *                "type":"home",
 *                "address":"jkjkjkjkj"
 *            }
 *        ],
 *        "phones":[
 *            {
 *                "type":"personal",
 *                "phone_number":"565656"
 *            }
 *        ],
 *        "note":"best friend",
 *        "organization":"Vysoka skola ekonomicka"
 *    }
 */
class Contact(
        @field:Json(name = "uuid") var uuid: String,
        @field:Json(name = "first_name") var firstName: String,
        @field:Json(name = "last_name") var lastName: String,
        @field:Json(name = "emails") var emails: List<Email>,
        @field:Json(name = "addresses") var addresses: List<Address>,
        @field:Json(name = "phones") var phones: List<Phone>,
        @field:Json(name = "note") var note: String,
        @field:Json(name = "organization") var organization: String) {


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
