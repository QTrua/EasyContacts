package com.example.easycontacts.screen;


import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.easycontacts.R;
import com.example.easycontacts.adapter.ContactListAdapter;
import com.example.easycontacts.model.db.Contact;
import com.example.easycontacts.repository.ContactRepository;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class ContactListFragment extends Fragment {

    private static final int REQ_CONTACT_READ_REQ = 101;

    private ContactRepository contactRepository;

    private ContactListAdapter contactListAdapter = new ContactListAdapter();

    private OnContactListInteractionListener listener;

    public ContactListFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        contactRepository = new ContactRepository(getContext());
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_contact_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView contactListView = view.findViewById(R.id.listContacts);
        contactListAdapter.listener = new ContactListAdapter.OnContactItemInteracted() {
            @Override
            public void onContactClicked(Contact contact) {
                if (listener != null) {
                    listener.onEditContact(contact);
                }
            }
        };
        contactListView.setAdapter(contactListAdapter);

        FloatingActionButton fab = view.findViewById(R.id.fabAddContact);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onCreateContact();
                }
            }
        });

        requestAndLoadContacts();
    }

    private void requestAndLoadContacts() {
        int permitted = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_CONTACTS);
        if (permitted != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.READ_CONTACTS)) {
                showPermissionsRationale();
            } else {
                doRequestPermission();
            }
        } else {
            doLoadContacts();
        }
    }

    private void doRequestPermission() {
        requestPermissions(new String[]{Manifest.permission.READ_CONTACTS},
                REQ_CONTACT_READ_REQ);
    }

    private void showPermissionsRationale() {
        AlertDialog dialog = new AlertDialog.Builder(getContext())
                .setTitle(R.string.requesting_permission)
                .setMessage(R.string.requesting_permission_description)
                .setNeutralButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                        doRequestPermission();
                    }
                })
                .create();

        dialog.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case REQ_CONTACT_READ_REQ: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    requestAndLoadContacts();
                }
            }
        }
    }

    private void doLoadContacts() {
        contactRepository.listLocalContacts(new ContactRepository.ContactCallback() {
            @Override
            public void withContacts(List<Contact> contacts) {
                contactListAdapter.setContacts(contacts);
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof OnContactListInteractionListener) {
            listener = (OnContactListInteractionListener) context;
        } else {
            throw new RuntimeException(context.getClass().getName() +
                    " does not implement OnContactListInteractionListener"
            );
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    public interface OnContactListInteractionListener {
        void onCreateContact();
        void onEditContact(Contact contact);
    }
}
