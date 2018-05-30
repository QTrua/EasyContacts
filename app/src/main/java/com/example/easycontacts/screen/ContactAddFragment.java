package com.example.easycontacts.screen;


import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
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
import com.example.easycontacts.model.db.Contact;
import com.example.easycontacts.network.NetworkManager;
import com.example.easycontacts.repository.ContactRepository;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class ContactAddFragment extends Fragment {

    private ContactRepository contactRepository;

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

        contactRepository = new ContactRepository(getContext());
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
            textInputPhone.setText(contact.getPhone());
            textInputEmail.setText(contact.getEmail());
            textInputAddress.setText(contact.getAddress());
        }

        if (!isNetworkAvailable()) {
            sendButton.setEnabled(false);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.contact_edit, menu);
        if (!isNetworkAvailable()) {
            MenuItem item = menu.findItem(R.id.itemDelete);
            item.setEnabled(false);
            item.getIcon()
                    .setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_IN);
        }
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

        contactRepository.saveContact(newContact, new ContactRepository.SaveCallback() {
            @Override
            public void onSuccess() {
                getActivity().onBackPressed();
            }

            @Override
            public void onError(Throwable t) {
                Toast.makeText(getContext(), "Request not successful", Toast.LENGTH_LONG).show();
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

        contactRepository.deleteContact(contact.getUUID(), new ContactRepository.SaveCallback() {
            @Override
            public void onSuccess() {
                getActivity().onBackPressed();
            }

            @Override
            public void onError(Throwable t) {
                Toast.makeText(getContext(), "Request not successful", Toast.LENGTH_LONG).show();
            }
        });
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
