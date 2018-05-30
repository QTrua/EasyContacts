package com.example.easycontacts.screen;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
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

        contactRepository.listContacts(new ContactRepository.ContactCallback() {
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
