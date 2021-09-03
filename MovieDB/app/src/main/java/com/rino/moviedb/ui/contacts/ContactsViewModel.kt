package com.rino.moviedb.ui.contacts

import android.content.Context
import android.provider.ContactsContract
import android.provider.ContactsContract.CommonDataKinds.Phone
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rino.moviedb.entities.Contact
import com.rino.moviedb.entities.ScreenState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ContactsViewModel : ViewModel() {
    private val contactsList: ArrayList<Contact> = arrayListOf()

    private val _contacts: MutableLiveData<ScreenState<List<Contact>>> =
        MutableLiveData(ScreenState.Loading)
    val contacts: LiveData<ScreenState<List<Contact>>> = _contacts

    fun fetchContacts(context: Context) = viewModelScope.launch(Dispatchers.IO) {
        val cursorWithContacts = context.contentResolver.query(
            ContactsContract.Contacts.CONTENT_URI,
            null,
            null,
            null,
            ContactsContract.Contacts.DISPLAY_NAME + " ASC"
        )

        cursorWithContacts?.let { cursor ->
            for (i in 0..cursor.count) {
                if (cursor.moveToPosition(i)) {
                    val contactId = cursor
                        .getString(cursor.getColumnIndex(ContactsContract.Contacts._ID))

                    val displayName = cursor
                        .getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))

                    val photoThumbUri = cursor
                        .getString(cursor.getColumnIndex(ContactsContract.Contacts.PHOTO_THUMBNAIL_URI))

                    val hasPhoneNumber = cursor
                        .getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))

                    val phone = if (hasPhoneNumber == "1") {
                        getContactPhone(context, contactId)
                    } else null

                    contactsList.add(
                        Contact(
                            contactId,
                            displayName,
                            photoThumbUri,
                            hasPhoneNumber == "1",
                            phone
                        )
                    )
                }
            }
        }

        cursorWithContacts?.close()

        _contacts.postValue(ScreenState.Success(contactsList))
    }

    private fun getContactPhone(context: Context, contactId: String): String? {
        val phones = context.contentResolver.query(
            Phone.CONTENT_URI,
            null,
            Phone.CONTACT_ID + " = " + contactId,
            null,
            null
        )

        var phoneNumber: String? = null
        phones?.let {
            if (it.moveToNext()) {
                phoneNumber = it.getString(phones.getColumnIndex(Phone.NUMBER))
            }
        }

        phones?.close()

        return phoneNumber
    }

}