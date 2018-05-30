package com.example.easycontacts.screen;


import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.easycontacts.R;
import com.example.easycontacts.model.Address;
import com.example.easycontacts.model.Contact;
import com.example.easycontacts.model.Email;
import com.example.easycontacts.model.Phone;
import com.example.easycontacts.network.NetworkManager;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;

import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class ContactAddFragment extends Fragment {

    private NetworkManager networkManager;

    private static final String KEY_CONTACT = "contact";

    private Contact contact;

    public ContactAddFragment() {
        // Required empty public constructor
    }

    public static ContactAddFragment newInstance(Contact contact) {
        ContactAddFragment fragment = new ContactAddFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(KEY_CONTACT, contact);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();
        if (bundle != null) {
            contact = (Contact) bundle.getSerializable(KEY_CONTACT);
        }

        setHasOptionsMenu(contact != null);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_contact_add, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button sendButton = view.findViewById(R.id.buttonSend);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                send(v);
            }
        });

        if (contact != null) {
            EditText textInputFirstName = view.<TextInputLayout>findViewById(R.id.textLayoutFirstName).getEditText();
            EditText textInputLastName = view.<TextInputLayout>findViewById(R.id.textLayoutLastName).getEditText();
            EditText textInputPhone = view.<TextInputLayout>findViewById(R.id.textLayoutPhone).getEditText();
            EditText textInputEmail = view.<TextInputLayout>findViewById(R.id.textLayoutEmail).getEditText();
            EditText textInputAddress = view.<TextInputLayout>findViewById(R.id.textLayoutAddress).getEditText();

            textInputFirstName.setText(contact.getFirstName());
            textInputLastName.setText(contact.getLastName());
            if (contact.getPhones() != null && contact.getPhones().size() != 0) {
                textInputPhone.setText(contact.getPhones().get(0).getPhone());
            }

            if (contact.getEmails() != null && contact.getEmails().size() != 0) {
                textInputEmail.setText(contact.getEmails().get(0).getEmail());
            }

            if (contact.getAddresses() != null && contact.getAddresses().size() != 0) {
                textInputAddress.setText(contact.getAddresses().get(0).getAddress());
            }
        }

        SharedPreferences preferences = getContext().
                getSharedPreferences(MainActivity.SHARED_PREF_KEY, Context.MODE_PRIVATE);
        String userId = preferences.getString(MainActivity.KEY_USER_ID, null);
        networkManager = new NetworkManager(userId);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.contact_edit, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.itemDelete:
                onDeleteItem();
                return true;
        }

        return false;
    }

    private void send(View interactedView) {
        View view = getView();
        if (view == null) {
            return;
        }

        if (!validateInputs(view)) {
            return;
        }

        final TextInputLayout textLayoutFirstName = view.findViewById(R.id.textLayoutFirstName);
        EditText textInputFirstName = textLayoutFirstName.getEditText();

        TextInputLayout textLayoutLastName = view.findViewById(R.id.textLayoutLastName);
        EditText textInputLastName = textLayoutLastName.getEditText();

        EditText textInputPhone = view.<TextInputLayout>findViewById(R.id.textLayoutPhone).getEditText();
        EditText textInputEmail = view.<TextInputLayout>findViewById(R.id.textLayoutEmail).getEditText();
        EditText textInputAddress = view.<TextInputLayout>findViewById(R.id.textLayoutAddress).getEditText();

        String firstName = textInputFirstName.getText().toString();
        String lastName = textInputLastName.getText().toString();
        String phoneNumber = textInputPhone.getText().toString();
        String emailAddress = textInputEmail.getText().toString();
        String addressString = textInputAddress.getText().toString();

        Button sendButton = (Button)interactedView;
        sendButton.setEnabled(false);

        Contact newContact;
        if (contact != null) {
            newContact = contact;
        } else {
            newContact = new Contact();
        }
        newContact.update(firstName, lastName, phoneNumber, emailAddress, addressString);

        saveContact(newContact);
    }

    private void saveContact(Contact newContact) {
        Call<Contact> call;
        if (newContact.getUUID() == null) {
            call = networkManager.getContactService()
                    .createContact(newContact);
        } else {
            call = networkManager.getContactService()
                    .updateContact(newContact.getUUID(), newContact);
        }

        call.enqueue(new Callback<Contact>() {
            @Override
            public void onResponse(@NonNull Call<Contact> call, @NonNull Response<Contact> response) {
                if (response.isSuccessful()) {
                    getActivity().onBackPressed();
                } else {
                    Toast.makeText(getContext(), "Request not successful", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Contact> call, @NonNull Throwable t) {
                Log.e(getClass().getSimpleName(), t.toString());
            }
        });
    }

    private boolean validateInputs(View view) {
        final TextInputLayout textLayoutFirstName = view.findViewById(R.id.textLayoutFirstName);
        EditText textInputFirstName = textLayoutFirstName.getEditText();

        TextInputLayout textLayoutLastName = view.findViewById(R.id.textLayoutLastName);
        EditText textInputLastName = textLayoutLastName.getEditText();

        if (TextUtils.isEmpty(textInputFirstName.getText().toString())) {
            textLayoutFirstName.setError("First name is required");
            return false;
        } else {
            textLayoutFirstName.setErrorEnabled(false);
        }

        if (TextUtils.isEmpty(textInputLastName.getText().toString())) {
            textLayoutLastName.setError("Last name is required");
            return false;
        } else {
            textLayoutLastName.setErrorEnabled(false);
        }

        return true;
    }

    private void onDeleteItem() {
        AlertDialog alertDialog = new AlertDialog.Builder(getContext())
                .setTitle(R.string.delete_confirmation)
                .setMessage(R.string.delete_confirmation_description)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        doDeleteItem();
                    }
                })
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create();

        alertDialog.show();
    }

    private void doDeleteItem() {

        if (contact == null) {
            return;
        }

        networkManager.getContactService()
                .deleteContact(contact.getUUID())
                .enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                        if (response.isSuccessful()) {
                            getActivity().onBackPressed();
                        } else {
                            Toast.makeText(getContext(), "Request not successful", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                        Log.e(getClass().getSimpleName(), t.toString());
                    }
                });
    }
}
