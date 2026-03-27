package com.example.appinterfaces.ui.contact.list

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appinterfaces.data.Contact
import com.example.appinterfaces.data.ContactDatasource
import com.example.appinterfaces.data.groupByInitial
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

class ContactsListViewModel : ViewModel() {
    var uiState: ContactsListUiState by mutableStateOf(ContactsListUiState())
        private set

    init {
        loadContacts()
    }

    fun loadContacts() {
        uiState = uiState.copy(
            isLoading = true,
            isError = false
        )

        viewModelScope.launch {
            uiState = try {
                uiState.copy(
                    contact = ContactDatasource.instance.findAll().groupByInitial(),
                    isLoading = false
                )
            } catch (e: Exception) {
                Log.e("ContactsListViewModel", "Erro ao carregar contatos", e)
                uiState.copy(
                    isError = true,
                    isLoading = false
                )
            }
        }
    }

    fun toggleIsFavorite(contact: Contact) {
        try{
            val updatedContact = contact.copy(isFavorite = !contact.isFavorite)
            ContactDatasource.instance.save(updatedContact)
            uiState = uiState.copy(
                contact = ContactDatasource.instance.findAll().groupByInitial()
            )
        } catch (e: Exception) {
            Log.e("ContactsListViewModel", "Erro ao atualizar contato", e)
        }
    }
}