package com.example.appinterfaces.ui.contact.form

import com.example.appinterfaces.data.Contact

data class ContactFormState(
    val contactId: Int = 0,
    val isLoading: Boolean = false,
    val contact: Contact = Contact(),
    val hasErrorLoading: Boolean = false,
) {
    val isNewContact get(): Boolean = contactId <= 0
}