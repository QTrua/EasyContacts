package com.example.easycontacts.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import com.example.easycontacts.R
import com.example.easycontacts.model.db.Contact

/**
 * Created by xkrej63 on 29.05.2018.
 */

class ContactListAdapter() : RecyclerView.Adapter<ContactListAdapter.ViewHolder>() {

    private var contacts: List<Contact>? = null

    var listener: OnContactItemInteracted? = null

    fun setContacts(contacts: List<Contact>) {
        this.contacts = contacts
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.adapter_item_contact, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val contact = contacts!![position]
        holder.onBind(contact)
    }

    override fun getItemCount(): Int {
        return contacts?.size ?: 0
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun onBind(contact: Contact) {
            val textTitleView = itemView.findViewById<TextView>(R.id.textTitleView)
            val textSubtitleView = itemView.findViewById<TextView>(R.id.textSubtitleView)

            textTitleView.text = contact.displayName
            textSubtitleView.text = contact.primaryContactInfo

            itemView.setOnClickListener {
                if (listener != null) {
                    listener!!.onContactClicked(contact)
                }
            }
        }
    }

    interface OnContactItemInteracted {
        fun onContactClicked(contact: Contact)
    }
}
