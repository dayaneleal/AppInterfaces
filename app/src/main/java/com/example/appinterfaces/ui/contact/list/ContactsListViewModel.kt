package com.example.appinterfaces.ui.contact.list

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appinterfaces.data.Contact
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

class ContactsListViewModel : ViewModel() {
    val uiState: MutableState<ContactsListUiState> = mutableStateOf(ContactsListUiState())

    init {
        loadContacts()
    }

    fun loadContacts()  {
        uiState.value = uiState.value.copy(
            isLoading = true,
            isError = false
        )

        viewModelScope.launch {
            delay(2000)
            val hasError = Random.nextBoolean()

            uiState.value = if(hasError) {
                uiState.value.copy(
                    isLoading = false,
                    isError = true
                )
            } else {
                val isEmpty = Random.nextBoolean()
                if(isEmpty) {
                    uiState.value.copy(
                        contact = listOf(),
                        isLoading = false,
                    )
                } else {
                    uiState.value.copy(
                        contact = generateContacts(),
                        isLoading = false,
                    )
                }
            }
        }
    }

    fun toggleIsFavorite(updatedContact: Contact){
        uiState.value = uiState.value.copy(
            contact = uiState.value.contact.map { currentContact ->
                if(currentContact.id == updatedContact.id) {
                    currentContact.copy(isFavorite = !currentContact.isFavorite)
                } else {
                    currentContact
                }
            }
        )
    }
}