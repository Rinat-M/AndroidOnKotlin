package com.rino.moviedb.ui.contacts

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.rino.moviedb.R
import com.rino.moviedb.databinding.ContactItemBinding
import com.rino.moviedb.entities.Contact
import com.rino.moviedb.utils.makeCall

class ContactsAdapter(
    private val contacts: List<Contact>,
    private val onItemClickListener: OnItemClickListener
) : RecyclerView.Adapter<ContactsAdapter.ContactsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactsViewHolder {
        val binding = ContactItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false
        )

        return ContactsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ContactsViewHolder, position: Int) {
        holder.bind(contacts[position])
    }

    override fun getItemCount(): Int = contacts.size

    inner class ContactsViewHolder(
        private val binding: ContactItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(contact: Contact) {
            with(binding) {
                contactName.text = contact.displayName
                contactPhone.text = contact.phoneNumber

                Glide.with(itemView.context)
                    .load(contact.photoThumbUri)
                    .circleCrop()
                    .placeholder(R.drawable.ic_account)
                    .into(contactThumbnail)

                with(contactCall) {
                    isVisible = contact.hasPhoneNumber
                    setOnClickListener { contact.phoneNumber?.let { context.makeCall(it) } }
                }

                contactLayout.setOnClickListener { onItemClickListener.onItemClick(contact) }
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(contact: Contact)
    }
}