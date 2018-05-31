package com.example.easycontacts.screen


import android.content.Context
import android.content.DialogInterface
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.PorterDuff
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.support.design.widget.TextInputLayout
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

import com.example.easycontacts.R
import com.example.easycontacts.model.db.Contact
import com.example.easycontacts.network.NetworkManager
import com.example.easycontacts.repository.ContactRepository

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * A simple [Fragment] subclass.
 */
class ContactAddFragment : Fragment() {

    private var contactRepository: ContactRepository? = null

    private var contact: Contact? = null

    private val isNetworkAvailable: Boolean
        get() {
            val connectivityManager = context!!.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetworkInfo = connectivityManager.activeNetworkInfo
            return activeNetworkInfo != null && activeNetworkInfo.isConnected
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val bundle = arguments
        if (bundle != null) {
            contact = bundle.getSerializable(KEY_CONTACT) as Contact
        }

        setHasOptionsMenu(contact != null)

        contactRepository = ContactRepository(context!!)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_contact_add, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sendButton = view.findViewById<Button>(R.id.buttonSend)
        sendButton.setOnClickListener { v -> send(v) }

        if (contact != null) {
            val textInputFirstName = view.findViewById<TextInputLayout>(R.id.textLayoutFirstName).editText
            val textInputLastName = view.findViewById<TextInputLayout>(R.id.textLayoutLastName).editText
            val textInputPhone = view.findViewById<TextInputLayout>(R.id.textLayoutPhone).editText
            val textInputEmail = view.findViewById<TextInputLayout>(R.id.textLayoutEmail).editText
            val textInputAddress = view.findViewById<TextInputLayout>(R.id.textLayoutAddress).editText

            textInputFirstName!!.setText(contact!!.firstName)
            textInputLastName!!.setText(contact!!.lastName)
            textInputPhone!!.setText(contact!!.phone)
            textInputEmail!!.setText(contact!!.email)
            textInputAddress!!.setText(contact!!.address)
        }

        if (!isNetworkAvailable) {
            sendButton.isEnabled = false
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater!!.inflate(R.menu.contact_edit, menu)
        if (!isNetworkAvailable) {
            val item = menu!!.findItem(R.id.itemDelete)
            item.isEnabled = false
            item.icon
                    .setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_IN)
        }
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item!!.itemId) {
            R.id.itemDelete -> {
                onDeleteItem()
                return true
            }
        }

        return false
    }

    private fun send(interactedView: View) {
        val view = view ?: return

        if (!validateInputs(view)) {
            return
        }

        val textLayoutFirstName = view.findViewById<TextInputLayout>(R.id.textLayoutFirstName)
        val textInputFirstName = textLayoutFirstName.editText

        val textLayoutLastName = view.findViewById<TextInputLayout>(R.id.textLayoutLastName)
        val textInputLastName = textLayoutLastName.editText

        val textInputPhone = view.findViewById<TextInputLayout>(R.id.textLayoutPhone).editText
        val textInputEmail = view.findViewById<TextInputLayout>(R.id.textLayoutEmail).editText
        val textInputAddress = view.findViewById<TextInputLayout>(R.id.textLayoutAddress).editText

        val firstName = textInputFirstName!!.text.toString()
        val lastName = textInputLastName!!.text.toString()
        val phoneNumber = textInputPhone!!.text.toString()
        val emailAddress = textInputEmail!!.text.toString()
        val addressString = textInputAddress!!.text.toString()

        val sendButton = interactedView as Button
        sendButton.isEnabled = false

        val newContact = contact?.let { it } ?: Contact()
        newContact.update(firstName, lastName, phoneNumber, emailAddress, addressString)

        contactRepository!!.saveContact(newContact, object : ContactRepository.SaveCallback {
            override fun onSuccess() {
                activity!!.onBackPressed()
            }

            override fun onError(t: Throwable?) {
                Toast.makeText(context, "Request not successful", Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun validateInputs(view: View): Boolean {
        val textLayoutFirstName = view.findViewById<TextInputLayout>(R.id.textLayoutFirstName)
        val textInputFirstName = textLayoutFirstName.editText

        val textLayoutLastName = view.findViewById<TextInputLayout>(R.id.textLayoutLastName)
        val textInputLastName = textLayoutLastName.editText

        if (TextUtils.isEmpty(textInputFirstName!!.text.toString())) {
            textLayoutFirstName.error = "First name is required"
            return false
        } else {
            textLayoutFirstName.isErrorEnabled = false
        }

        if (TextUtils.isEmpty(textInputLastName!!.text.toString())) {
            textLayoutLastName.error = "Last name is required"
            return false
        } else {
            textLayoutLastName.isErrorEnabled = false
        }

        return true
    }

    private fun onDeleteItem() {
        val alertDialog = AlertDialog.Builder(context!!)
                .setTitle(R.string.delete_confirmation)
                .setMessage(R.string.delete_confirmation_description)
                .setPositiveButton(android.R.string.ok) { dialog, which -> doDeleteItem() }
                .setNegativeButton(android.R.string.cancel) { dialog, which -> dialog.dismiss() }
                .create()

        alertDialog.show()
    }

    private fun doDeleteItem() {
        if (contact == null) {
            return
        }

        contactRepository!!.deleteContact(contact!!.uuid, object : ContactRepository.SaveCallback {
            override fun onSuccess() {
                activity!!.onBackPressed()
            }

            override fun onError(t: Throwable?) {
                Toast.makeText(context, "Request not successful", Toast.LENGTH_LONG).show()
            }
        })
    }

    companion object {

        private val KEY_CONTACT = "contact"

        fun newInstance(contact: Contact): ContactAddFragment {
            val fragment = ContactAddFragment()
            val bundle = Bundle()
            bundle.putSerializable(KEY_CONTACT, contact)
            fragment.arguments = bundle
            return fragment
        }
    }
}
