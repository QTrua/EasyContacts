package com.example.easycontacts.screen


import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.easycontacts.R
import com.example.easycontacts.adapter.ContactListAdapter
import com.example.easycontacts.model.db.Contact
import com.example.easycontacts.repository.ContactRepository
import kotlinx.android.synthetic.main.fragment_contact_list.*


/**
 * A simple [Fragment] subclass.
 */
class ContactListFragment : Fragment() {

    private var contactRepository: ContactRepository? = null

    private val contactListAdapter = ContactListAdapter()

    private var listener: OnContactListInteractionListener? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        contactRepository = ContactRepository(context!!)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_contact_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        contactListAdapter.listener = {
            listener?.onEditContact(it)
        }

        listContacts.adapter = contactListAdapter

        fabAddContact?.setOnClickListener {
            listener?.onCreateContact()
        }

        requestAndLoadContacts()
    }

    private fun requestAndLoadContacts() {
        val permitted = ContextCompat.checkSelfPermission(context!!, Manifest.permission.READ_CONTACTS)
        if (permitted != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(activity!!, Manifest.permission.READ_CONTACTS)) {
                showPermissionsRationale()
            } else {
                doRequestPermission()
            }
        } else {
            doLoadContacts()
        }
    }

    private fun doRequestPermission() {
        requestPermissions(arrayOf(Manifest.permission.READ_CONTACTS),
                REQ_CONTACT_READ_REQ)
    }

    private fun showPermissionsRationale() {
        val dialog = AlertDialog.Builder(context)
                .setTitle(R.string.requesting_permission)
                .setMessage(R.string.requesting_permission_description)
                .setNeutralButton(android.R.string.ok) { dialog, _ ->
                    dialog.dismiss()

                    doRequestPermission()
                }
                .create()

        dialog.show()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            REQ_CONTACT_READ_REQ -> {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    requestAndLoadContacts()
                }
            }
        }
    }

    private fun doLoadContacts() {
        contactRepository!!.listContacts(object : ContactRepository.ContactCallback {
            override fun withContacts(contacts: List<Contact>) {
                contactListAdapter.contacts = contacts
            }
        })
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        if (context is OnContactListInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context!!.javaClass.name + " does not implement OnContactListInteractionListener"
            )
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    interface OnContactListInteractionListener {
        fun onCreateContact()
        fun onEditContact(contact: Contact)
    }

    companion object {

        private val REQ_CONTACT_READ_REQ = 101
    }
}
