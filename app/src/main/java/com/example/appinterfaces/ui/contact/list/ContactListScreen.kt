package com.example.appinterfaces.ui.contact.list

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Refresh
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.appinterfaces.R
import com.example.appinterfaces.data.Contact
import com.example.appinterfaces.data.groupByInitial
import com.example.appinterfaces.data.utils.generateContacts
import com.example.appinterfaces.ui.shared.composables.ComposableAvatar
import com.example.appinterfaces.ui.shared.composables.DefaultErrorState
import com.example.appinterfaces.ui.shared.composables.DefaultLoadingState
import com.example.appinterfaces.ui.shared.composables.FavoriteIconButton
import com.example.appinterfaces.ui.theme.AppInterfacesTheme
import kotlin.random.Random

@Composable
fun ContactListScreen(
    modifier: Modifier = Modifier,
    viewModel: ContactsListViewModel = viewModel()
) {
    val contentModifier = modifier.fillMaxSize()

    if(viewModel.uiState.value.isLoading) {
        DefaultLoadingState(contentModifier, "Carregando contatos...")
    } else if(viewModel.uiState.value.isError) {
        DefaultErrorState(contentModifier, onTryAgainPressed = viewModel::loadContacts)
    } else {
        Scaffold(
            floatingActionButton = {
                ExtendedFloatingActionButton(
                    onClick = {
                        // TODO: Ir para a tela de adicionar contato
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
                    viewModel::loadContacts
            ) }
        ) { paddingValues ->
            val defaultModifier: Modifier = Modifier.padding(paddingValues)
            if (viewModel.uiState.value.contact.isEmpty()) {
                EmptyState(defaultModifier)
            } else {
                List(
                    modifier = defaultModifier,
                    contacts = viewModel.uiState.value.contact,
                    onFavoritePressed = viewModel::toggleIsFavorite
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
    contacts: Map<String, List<Contact>> = emptyMap(),
    onFavoritePressed: (Contact) -> Unit
) {
    LazyColumn(
        modifier = modifier.fillMaxSize()
    ) {
        contacts.forEach { (key, contacts) ->
            stickyHeader {
                Box(modifier = Modifier.fillMaxWidth().background(color = MaterialTheme.colorScheme.secondaryContainer))
                {
                    Text(
                        text = key,
                        modifier = Modifier.fillMaxWidth().padding(top = 8.dp, bottom = 8.dp, start = 16.dp),
                        color = MaterialTheme.colorScheme.secondary
                    )
                }
            }

            items(contacts) { contact ->
                ContactListItem(
                    contact = contact,
                    onFavoritePressed = onFavoritePressed
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ListPreview(modifier: Modifier = Modifier) {
    AppInterfacesTheme{
        List(
            contacts = generateContacts().groupByInitial(),
            onFavoritePressed = {}
        )
    }
}

@Composable
fun ContactListItem(
    modifier: Modifier = Modifier,
    contact: Contact,
    onFavoritePressed: (Contact) -> Unit = {}
) {
    ListItem(
        modifier = modifier,
        headlineContent = {
            Text(contact.fullName)
        },
        leadingContent = {
            ComposableAvatar(
                firstName = contact.firstName,
                lastName = contact.lastName
            )
        },
        trailingContent = {
            FavoriteIconButton(
                isFavorite = contact.isFavorite,
                onClick =  {
                    onFavoritePressed(contact)
                }
            )
        }
    )
}