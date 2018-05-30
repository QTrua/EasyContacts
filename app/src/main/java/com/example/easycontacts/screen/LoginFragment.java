package com.example.easycontacts.screen;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.easycontacts.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment {

    private OnLoginInteractionListener listener;

    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button button = view.findViewById(R.id.buttonLogin);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onLogin();
            }
        });
    }

    private void onLogin() {
        View view = getView();
        if (view == null) {
            return;
        }

        TextInputLayout textInputLayoutUserId = view.findViewById(R.id.textLayoutUserID);
        TextInputEditText textInputEditTextUserId = view.findViewById(R.id.textInputUserID);

        String userId = textInputEditTextUserId.getText().toString();
        saveUserId(userId);

        if (listener != null) {
            listener.onLoggedIn();
        }
    }

    private void saveUserId(String userId) {
        SharedPreferences preferences = getContext().
                getSharedPreferences(MainActivity.SHARED_PREF_KEY, Context.MODE_PRIVATE);

        preferences.edit()
                .putString(MainActivity.KEY_USER_ID, userId)
                .commit();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof OnLoginInteractionListener) {
            listener = (OnLoginInteractionListener) context;
        } else {
            throw new RuntimeException(context.getClass().getName() +
                    " does not implement OnLoginInteractionListener"
            );
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    public interface OnLoginInteractionListener {
        void onLoggedIn();
    }
}
