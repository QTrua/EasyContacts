package com.example.easycontacts.screen;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.easycontacts.R;
import com.example.easycontacts.model.Contact;

public class MainActivity extends AppCompatActivity
    implements ContactListFragment.OnContactListInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setUpListFragment();
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
}
