package com.example.easycontacts.screen


import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.support.design.widget.TextInputEditText
import android.support.design.widget.TextInputLayout
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText

import com.example.easycontacts.R

/**
 * A simple [Fragment] subclass.
 */
class LoginFragment : Fragment() {

    private var listener: OnLoginInteractionListener? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val button = view.findViewById<Button>(R.id.buttonLogin)
        button.setOnClickListener { onLogin() }
    }

    private fun onLogin() {
        val view = view ?: return

        val textInputLayoutUserId = view.findViewById<TextInputLayout>(R.id.textLayoutUserID)
        val textInputEditTextUserId = view.findViewById<TextInputEditText>(R.id.textInputUserID)

        val userId = textInputEditTextUserId.text.toString()
        saveUserId(userId)

        if (listener != null) {
            listener!!.onLoggedIn()
        }
    }

    private fun saveUserId(userId: String) {
        val preferences = context!!.getSharedPreferences(MainActivity.SHARED_PREF_KEY, Context.MODE_PRIVATE)

        preferences.edit()
                .putString(MainActivity.KEY_USER_ID, userId)
                .commit()
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        if (context is OnLoginInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context!!.javaClass.name + " does not implement OnLoginInteractionListener"
            )
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    interface OnLoginInteractionListener {
        fun onLoggedIn()
    }
}
