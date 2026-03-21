package com.example.appinterfaces.ui.contact

import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CloudOff
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.appinterfaces.R
import com.example.appinterfaces.data.Contact
import com.example.appinterfaces.ui.theme.AppInterfacesTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

@Composable
fun ContactListScreen(
    modifier: Modifier = Modifier,
    coroutineScope: CoroutineScope = rememberCoroutineScope()
) {

    val isInitialCompositionState: MutableState<Boolean> = rememberSaveable {
        mutableStateOf(true)
    }
    val isLoadingState: MutableState<Boolean> = rememberSaveable {
        mutableStateOf(false)
    }
    val isErrorState: MutableState<Boolean> = rememberSaveable {
        mutableStateOf(false)
    }

    val contactsState: MutableState<List<Contact>> = rememberSaveable {
        mutableStateOf(emptyList())
    }

    val loadContacts: () -> Unit = {
        isLoadingState.value = true
        isErrorState.value = false
        coroutineScope.launch {
            delay(2000)
            isErrorState.value = Random.nextBoolean()
            if(!isErrorState.value) {
                val isEmpty = Random.nextBoolean()
                if(isEmpty) {
                    contactsState.value = emptyList()
                } else {
                    contactsState.value = generateContacts()
                }
            }
            isLoadingState.value = false
        }
    }

    val contentModifier = modifier.fillMaxSize()

    if(isLoadingState.value) {
        LoadingState(contentModifier)
    } else if(isErrorState.value) {
        ErrorState(contentModifier, onTryAgainPressed = loadContacts)
    } else {
        Scaffold(
            floatingActionButton = {
                ExtendedFloatingActionButton(
                    onClick = {
                        contactsState.value = contactsState.value.plus(
                            Contact(firstName = "Teste", lastName = "Teste")
                        )
                    }
                ){
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = "Novo contato"

                    )
                    Spacer(Modifier.size(8.dp))
                    Text(text = "Novo contato")
                }

            },
            modifier = modifier.fillMaxSize(),
            topBar = { AppBar(
                onRefreshPressed =
                    loadContacts
            ) }
        ) { paddingValues ->
            val defaultModifier: Modifier = Modifier.padding(paddingValues)
            if (contactsState.value.isEmpty()) {
                EmptyState(defaultModifier)
            } else {
                List(
                    modifier = defaultModifier,
                    contacts = contactsState.value
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBar(
    modifier: Modifier = Modifier,
    onRefreshPressed: () -> Unit,
) {
    TopAppBar(
        modifier = modifier.fillMaxWidth(),
        title = { Text("Contatos") },
        colors = TopAppBarDefaults.topAppBarColors(
            actionIconContentColor = MaterialTheme.colorScheme.primary,
            titleContentColor = MaterialTheme.colorScheme.primary,
        ),
        actions = {
            IconButton(
                onClick = onRefreshPressed
            ) {
                Icon(
                    imageVector = Icons.Filled.Refresh,
                    contentDescription = "Atualizar"
                )
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun AppBarPreview(modifier: Modifier = Modifier) {
    AppInterfacesTheme{
        AppBar(
            onRefreshPressed = {}
        )
    }
}

@Composable
fun LoadingState(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CircularProgressIndicator(
            color= MaterialTheme.colorScheme.primary,
            trackColor = MaterialTheme.colorScheme.surfaceVariant,
            modifier = Modifier.size(48.dp)
        )
        Spacer(modifier = Modifier.size(16.dp))
        Text(
            text = "Carregando contatos...",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@Preview(showBackground = true)
@Composable
fun LoadingStatePreview(modifier: Modifier = Modifier) {
    AppInterfacesTheme{
        LoadingState()
    }
}

@Composable
fun ErrorState(
    modifier: Modifier = Modifier,
    onTryAgainPressed: () -> Unit
) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Filled.CloudOff,
            contentDescription = "Erro ao carregar",
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(64.dp)
        )

        val horizontalPadding = PaddingValues( start = 8.dp, end  = 8.dp)
        Text(
            modifier = Modifier
                .padding(horizontalPadding)
                .padding(top = 16.dp),
            text = "Ocorreu um erro ao carregar",
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.titleLarge,
        )
        Text(
            modifier = Modifier
                .padding(horizontalPadding)
                .padding(top = 8.dp),
            text = "Aguarda um momento e tente novamente",
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.bodyMedium,
        )
        ElevatedButton(
            modifier = Modifier.padding(top = 16.dp),
            onClick = onTryAgainPressed) {
            Text(text = "Tentar novamente")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ErrorStatePreview(modifier: Modifier = Modifier) {
    AppInterfacesTheme{
        ErrorState(onTryAgainPressed = {})
    }
}

@Composable
fun EmptyState(modifier: Modifier = Modifier) {
    Column(
        modifier = Modifier
            .padding(all = 16.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.no_data),
            contentDescription = "Nenhum contato encontrado",
        )
        Text(
            text = "Nada por aqui...",
            modifier = Modifier.padding(top = 16.dp),
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.titleLarge,
        )
        Text(
            text = "Você ainda não adicionou nenhum contato." +
                    "\nAdicione o primeiro utilizando o botão \"Novo contato\"",
            modifier = Modifier.padding(top = 8.dp),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.primary,
            textAlign = TextAlign.Center
        )
    }
}

@Preview(showBackground = true)
@Composable
fun EmptyStatePreview(modifier: Modifier = Modifier) {
    AppInterfacesTheme{
        EmptyState()
    }
}

@Composable
fun List(
    modifier: Modifier = Modifier,
         contacts: List<Contact> = emptyList()
) {
    LazyColumn(
        modifier = modifier.fillMaxSize()
    ) {
        items(contacts) { contact ->
            ContactListItem(contact = contact)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ListPreview(modifier: Modifier = Modifier) {
    AppInterfacesTheme{
        List(
            contacts = generateContacts()
        )
    }
}

@Composable
fun ContactListItem(modifier: Modifier = Modifier, contact: Contact) {
    val isFavoriteState: MutableState<Boolean> = rememberSaveable {
        mutableStateOf(contact.isFavorite)
    }
    ListItem(
        modifier = modifier,
        headlineContent = {
            Text(contact.fullName)
        },
        trailingContent = {
            IconButton(
                onClick = {
                    isFavoriteState.value = !isFavoriteState.value
                }
            ) {
                Icon(
                    contentDescription = "Favoritar",
                    imageVector = if(isFavoriteState.value) {
                        Icons.Filled.Favorite
                    } else {
                        Icons.Filled.FavoriteBorder
                    },
                    tint = if (isFavoriteState.value) {
                        Color.Red
                    } else {
                        LocalContentColor.current
                    }
                )
            }
        }
    )
}

private fun generateContacts(): List<Contact> {
    val firstNames = listOf(
        "João", "José", "Everton", "Marcos", "André", "Anderson", "Antônio",
        "Laura", "Ana", "Maria", "Joaquina", "Suelen"
    )
    val lastNames = listOf(
        "Do Carmo", "Oliveira", "Dos Santos", "Da Silva", "Brasil", "Pichetti",
        "Cordeiro", "Silveira", "Andrades", "Cardoso"
    )
    val contacts: MutableList<Contact> = mutableListOf()
    for (i in 0..29) {
        var generatedNewContact = false
        while (!generatedNewContact) {
            val firstNameIndex = Random.nextInt(firstNames.size)
            val lastNameIndex = Random.nextInt(lastNames.size)
            val newContact = Contact(
                id = i + 1,
                firstName = firstNames[firstNameIndex],
                lastName = lastNames[lastNameIndex]
            )
            if (!contacts.any { it.fullName == newContact.fullName }) {
                contacts.add(newContact)
                generatedNewContact = true
            }
        }
    }
    return contacts
}
