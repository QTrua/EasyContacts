package com.example.easycontacts.model.db

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.text.TextUtils

import com.example.easycontacts.model.api.Address
import com.example.easycontacts.model.api.Email
import com.example.easycontacts.model.api.Phone

import java.io.Serializable
import java.util.ArrayList
import java.util.Collections

/**
 * Created by xkrej63 on 30.05.2018.
 */

@Entity
class Contact(
        @PrimaryKey var uuid: String = "",
        @ColumnInfo(name = "first_name") var firstName: String = "",
        @ColumnInfo(name = "last_name") var lastName: String = "",
        @ColumnInfo(name = "phone") var phone: String = "",
        @ColumnInfo(name = "email") var email: String = "",
        @ColumnInfo(name = "address") var address: String = ""
) : Serializable {

    val displayName: String
        get() {
            val parts = ArrayList<String>()

            if (!firstName.isEmpty()) {
                parts.add(firstName)
            }

            if (!lastName.isEmpty()) {
                parts.add(lastName)
            }

            return TextUtils.join(" ", parts)
        }

    val primaryContactInfo: String?
        get() {
            if (!phone.isEmpty()) {
                return phone
            }

            if (!email.isEmpty()) {
                return email
            }

            if (!address.isEmpty()) {
                return address
            }

            return null
        }

    fun update(firstName: String, lastName: String, phoneNumber: String, emailAddress: String, addressString: String) {
        this.firstName = firstName
        this.lastName = lastName
        this.phone = phoneNumber
        this.email = emailAddress
        this.address = addressString
    }

    companion object {

        fun fromApiContact(apiContact: com.example.easycontacts.model.api.Contact): Contact {
            return Contact(
                    uuid = apiContact.uuid,
                    firstName = apiContact.firstName,
                    lastName = apiContact.lastName,
                    phone = apiContact.phones.firstOrNull()?.phone ?: "",
                    email = apiContact.emails.firstOrNull()?.email ?: "",
                    address = apiContact.addresses.firstOrNull()?.address ?: ""
            )
        }
    }

}
