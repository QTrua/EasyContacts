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
import kotlinx.android.synthetic.main.fragment_contact_add.*

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

        buttonSend.setOnClickListener { send() }

        if (contact != null) {
            textLayoutFirstName.editText?.setText(contact!!.firstName)
            textLayoutLastName.editText?.setText(contact!!.lastName)
            textLayoutPhone.editText?.setText(contact!!.phone)
            textLayoutEmail.editText?.setText(contact!!.email)
            textLayoutAddress.editText?.setText(contact!!.address)
        }

        if (!isNetworkAvailable) {
            buttonSend.isEnabled = false
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

    private fun send() {
        val view = view ?: return

        if (!validateInputs()) {
            return
        }

        val firstName = textLayoutFirstName.editText?.text.toString()
        val lastName = textLayoutLastName.editText?.text.toString()
        val phoneNumber = textLayoutPhone.editText?.text.toString()
        val emailAddress = textLayoutEmail.editText?.text.toString()
        val addressString = textLayoutAddress.editText?.text.toString()

        buttonSend.isEnabled = false

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

    private fun validateInputs(): Boolean {
        if (TextUtils.isEmpty(textLayoutFirstName.editText?.text.toString())) {
            textLayoutFirstName.error = "First name is required"
            return false
        } else {
            textLayoutFirstName.isErrorEnabled = false
        }

        if (TextUtils.isEmpty(textLayoutLastName.editText?.text.toString())) {
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
                .setPositiveButton(android.R.string.ok) { _, _ -> doDeleteItem() }
                .setNegativeButton(android.R.string.cancel) { dialog, _ -> dialog.dismiss() }
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
