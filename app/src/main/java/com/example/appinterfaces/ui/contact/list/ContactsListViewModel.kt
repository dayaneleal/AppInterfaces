package com.example.appinterfaces.ui.contact.list

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appinterfaces.data.Contact
import com.example.appinterfaces.data.groupByInitial
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
                        contact = emptyMap(),
                        isLoading = false,
                    )
                } else {
                    uiState.value.copy(
                        contact = generateContacts().groupByInitial(),
                        isLoading = false,
                    )
                }
            }
        }
    }

    fun toggleIsFavorite(updatedContact: Contact){
        val newMap: MutableMap<String, List<Contact>> = mutableMapOf()
        uiState.value.contact.keys.forEach { key ->
            newMap[key] = uiState.value.contact[key]!!.map {
                    currentContact ->
                if(currentContact.id == updatedContact.id) {
                    currentContact.copy(isFavorite = !currentContact.isFavorite)
                } else {
                    currentContact
                }
            }
        }
        uiState.value = uiState.value.copy(
            contact = newMap
        )
    }
}