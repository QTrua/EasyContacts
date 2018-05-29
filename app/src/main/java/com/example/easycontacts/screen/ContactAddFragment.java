package com.example.easycontacts.screen;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
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

    private NetworkManager networkManager = new NetworkManager();

    public ContactAddFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
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
    }

    private void send(View interactedView) {
        View view = getView();
        if (view == null) {
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
        if (TextUtils.isEmpty(firstName)) {
            textLayoutFirstName.setError("First name is required");

            textInputFirstName.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (s.length() > 0) {
                        textLayoutFirstName.setErrorEnabled(false);
                    }
                }
            });
            return;
        } else {
            textLayoutFirstName.setErrorEnabled(false);
        }

        String lastName = textInputLastName.getText().toString();
        if (TextUtils.isEmpty(lastName)) {
            textLayoutLastName.setError("Last name is required");
            return;
        } else {
            textLayoutLastName.setErrorEnabled(false);
        }

        Contact contact = new Contact();
        contact.setFirstName(firstName);
        contact.setLastName(lastName);

        String phoneNumber = textInputPhone.getText().toString();
        if (!TextUtils.isEmpty(phoneNumber)) {
            Phone phone = new Phone("personal", phoneNumber);
            contact.setPhones(Arrays.asList(phone));
        }

        String emailAddress = textInputEmail.getText().toString();
        if (!TextUtils.isEmpty(emailAddress)) {
            Email email = new Email("personal", emailAddress);
            contact.setEmails(Arrays.asList(email));
        }

        String addressString = textInputAddress.getText().toString();
        if (!TextUtils.isEmpty(addressString)) {
            Address address = new Address("home", addressString);
            contact.setAddresses(Arrays.asList(address));
        }

        Button sendButton = (Button)interactedView;
        sendButton.setEnabled(false);

        networkManager.getContactService()
                .createContact(contact)
                .enqueue(new Callback<Contact>() {
                    @Override
                    public void onResponse(Call<Contact> call, Response<Contact> response) {
                        if (response.isSuccessful()) {
                            getActivity().onBackPressed();
                        } else {
                            Toast.makeText(getContext(), "Request not successful", Toast.LENGTH_LONG).show();;
                        }
                    }

                    @Override
                    public void onFailure(Call<Contact> call, Throwable t) {
                        Log.e(getClass().getSimpleName(), t.toString());
                    }
                });
    }


}
