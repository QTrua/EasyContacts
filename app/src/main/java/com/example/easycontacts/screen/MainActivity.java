package com.example.easycontacts.screen;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.easycontacts.R;
import com.example.easycontacts.model.db.Contact;

public class MainActivity extends AppCompatActivity
    implements ContactListFragment.OnContactListInteractionListener,
    LoginFragment.OnLoginInteractionListener {

    public static final String SHARED_PREF_KEY = "credentials";
    public static final String KEY_USER_ID = "user_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences preferences = getSharedPreferences(SHARED_PREF_KEY, Context.MODE_PRIVATE);
        String userId = preferences.getString(KEY_USER_ID, null);
        if (userId == null) {
            setUpLoginFragment();
        } else {
            setUpListFragment();
        }
    }

    private void setUpLoginFragment() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frameContainer, new LoginFragment())
                .commit();
    }

    private void setUpListFragment() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frameContainer, new ContactListFragment())
                .commit();
    }

    @Override
    public void onCreateContact() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frameContainer, new ContactAddFragment())
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onEditContact(Contact contact) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frameContainer, ContactAddFragment.newInstance(contact))
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onLoggedIn() {
        setUpListFragment();
    }
}
