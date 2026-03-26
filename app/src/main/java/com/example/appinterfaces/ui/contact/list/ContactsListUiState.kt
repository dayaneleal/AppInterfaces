package com.example.appinterfaces.ui.contact.list

import com.example.appinterfaces.data.Contact

data class ContactsListUiState(
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val contact: Map<String, List<Contact>> = emptyMap()
)