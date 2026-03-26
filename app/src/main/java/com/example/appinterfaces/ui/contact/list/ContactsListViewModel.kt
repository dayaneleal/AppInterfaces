package com.example.appinterfaces.ui.contact.list

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appinterfaces.data.Contact
import com.example.appinterfaces.data.ContactDatasource
import com.example.appinterfaces.data.groupByInitial
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

class ContactsListViewModel : ViewModel() {
    val uiState: MutableState<ContactsListUiState> = mutableStateOf(ContactsListUiState())

    init {
        loadContacts()
    }

    fun loadContacts() {
        uiState.value = uiState.value.copy(
            isLoading = true,
            isError = false
        )

        viewModelScope.launch {
            uiState.value = try {
                uiState.value.copy(
                    contact = ContactDatasource.instance.findAll().groupByInitial(),
                    isLoading = false
                )
            } catch (e: Exception) {
                Log.e("ContactsListViewModel", "Erro ao carregar contatos", e)
                uiState.value.copy(
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
            uiState.value = uiState.value.copy(
                contact = ContactDatasource.instance.findAll().groupByInitial()
            )
        } catch (e: Exception) {
            Log.e("ContactsListViewModel", "Erro ao atualizar contato", e)
        }
    }
}