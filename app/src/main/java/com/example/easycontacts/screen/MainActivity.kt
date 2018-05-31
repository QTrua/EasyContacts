package com.example.easycontacts.screen

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

import com.example.easycontacts.R
import com.example.easycontacts.extension.getDefaultSharedPreferences
import com.example.easycontacts.model.db.Contact

class MainActivity : AppCompatActivity(), ContactListFragment.OnContactListInteractionListener, LoginFragment.OnLoginInteractionListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val preferences = getDefaultSharedPreferences()
        val userId = preferences.getString(KEY_USER_ID, null)
        if (userId == null) {
            setUpLoginFragment()
        } else {
            setUpListFragment()
        }
    }

    private fun setUpLoginFragment() {
        supportFragmentManager
                .beginTransaction()
                .replace(R.id.frameContainer, LoginFragment())
                .commit()
    }

    private fun setUpListFragment() {
        supportFragmentManager
                .beginTransaction()
                .replace(R.id.frameContainer, ContactListFragment())
                .commit()
    }

    override fun onCreateContact() {
        supportFragmentManager
                .beginTransaction()
                .replace(R.id.frameContainer, ContactAddFragment())
                .addToBackStack(null)
                .commit()
    }

    override fun onEditContact(contact: Contact) {
        supportFragmentManager
                .beginTransaction()
                .replace(R.id.frameContainer, ContactAddFragment.newInstance(contact))
                .addToBackStack(null)
                .commit()
    }

    override fun onLoggedIn() {
        setUpListFragment()
    }

    companion object {

        const val KEY_USER_ID = "user_id"
    }
}
