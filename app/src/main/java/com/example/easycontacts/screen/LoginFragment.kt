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
import com.example.easycontacts.extension.getDefaultSharedPreferences
import kotlinx.android.synthetic.main.fragment_login.*

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

        buttonLogin.setOnClickListener { onLogin() }
    }

    private fun onLogin() {
        if (view == null) {
            return
        }

        val userId = textInputUserID.text.toString()
        saveUserId(userId)

        listener?.onLoggedIn()
    }

    private fun saveUserId(userId: String) {
        context?.getDefaultSharedPreferences()
                ?.edit()
                ?.putString(MainActivity.KEY_USER_ID, userId)
                ?.commit()
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
