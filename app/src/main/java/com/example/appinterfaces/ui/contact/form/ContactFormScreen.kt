package com.example.appinterfaces.ui.contact.form

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Mail
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.appinterfaces.data.Contact
import com.example.appinterfaces.data.ContactTypeEnum
import com.example.appinterfaces.ui.contact.form.composables.FormCheckbox
import com.example.appinterfaces.ui.contact.form.composables.FormDatePicker
import com.example.appinterfaces.ui.contact.form.composables.FormFieldRow
import com.example.appinterfaces.ui.contact.form.composables.FormRadioButton
import com.example.appinterfaces.ui.contact.form.composables.FormTextField
import com.example.appinterfaces.ui.shared.composables.ComposableAvatar
import com.example.appinterfaces.ui.shared.composables.DefaultErrorState
import com.example.appinterfaces.ui.shared.composables.DefaultLoadingState
import com.example.appinterfaces.ui.theme.AppInterfacesTheme

@Composable
fun ContactFormScreen(
    modifier: Modifier = Modifier,
    onBackPressed: () -> Unit,
    viewModel: ContactFormViewModel = viewModel()
) {
    val contentModifier: Modifier = modifier.fillMaxSize()
    if(viewModel.uiState.isLoading) {
        DefaultLoadingState(modifier = contentModifier)
    } else if(viewModel.uiState.hasErrorLoading) {
        DefaultErrorState(modifier = contentModifier, onTryAgainPressed = viewModel::loadContact)
    } else {
        Scaffold(
            modifier = contentModifier,
            topBar = {
                AppBar(
                    isNewContact = true,
                    onBackPressed = onBackPressed
                )
            }
        ) { paddingValues ->
            FormContent(
                modifier = Modifier.padding(paddingValues),
                contact = viewModel.uiState.contact
            )
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AppBar(
    modifier: Modifier = Modifier,
    isNewContact: Boolean,
    onBackPressed: () -> Unit
) {
    TopAppBar(
        modifier = modifier.fillMaxWidth(),
        title = {
            Text(if (isNewContact) "Novo Contato" else "Editar Contato")
        },
        navigationIcon = {
            IconButton(onClick = onBackPressed) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Voltar"
                )
            }
        }
    )
}

private class BooleanParameterProvider : PreviewParameterProvider<Boolean> {
    override val values: Sequence<Boolean> = sequenceOf(true, false)
}

@Preview(showBackground = true)
@Composable
private fun AppBarPreview(
    @PreviewParameter(BooleanParameterProvider::class) isNewContact: Boolean
) {
    AppInterfacesTheme() {
        AppBar(
            isNewContact = isNewContact,
            onBackPressed = {}
        )
    }
}

@Composable
private fun FormContent(
    modifier: Modifier = Modifier,
    contact: Contact
) {
    Column(
        modifier.padding(16.dp)
            .imePadding()
            .verticalScroll(
                rememberScrollState()
            ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val formTextFieldModifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
        ComposableAvatar(
            modifier = Modifier.padding(bottom = 16.dp),
            firstName = contact.firstName,
            lastName = contact.lastName,
            size = 150.dp,
            textStyle = MaterialTheme.typography.displayLarge
        )
        FormFieldRow(
            label = "Descrição",
            imageVector = Icons.Filled.Person
        ) {
            FormTextField(
                modifier = formTextFieldModifier,
                label = "Nome",
                value = contact.firstName,
                onValueChange = {},
                keyboardCapitalization = KeyboardCapitalization.Words
            )
        }
        FormFieldRow(
            label = "Sobrenome",
        ) {
            FormTextField(
                modifier = formTextFieldModifier,
                label = "Sobrenome",
                value = contact.lastName,
                onValueChange = {},
                keyboardCapitalization = KeyboardCapitalization.Words
            )
        }
        FormFieldRow(
            label = "Telefone",
            imageVector = Icons.Filled.Phone
        ) {
            FormTextField(
                modifier = formTextFieldModifier,
                label = "Telefone",
                value = contact.phoneNumber,
                onValueChange = {},
                keyboardType = KeyboardType.Phone
            )
        }
        FormFieldRow(
            label = "E-mail",
            imageVector = Icons.Filled.Mail
        ) {
            FormTextField(
                modifier = formTextFieldModifier,
                label = "E-mail",
                value = contact.email,
                onValueChange = {},
                keyboardType = KeyboardType.Email
            )
        }
        FormFieldRow(
            label = "Data de aniversário",
        ) {
            FormDatePicker(
                modifier = formTextFieldModifier,
                label = "Data de aniversário",
                value = contact.birthDate,
                onValueChange = {},
            )
        }
        FormFieldRow(
            label = "Valor patrimonial",
            imageVector = Icons.Filled.AttachMoney
        ) {
            FormTextField(
                modifier = formTextFieldModifier,
                label = "Valor patrimonial",
                value = contact.assertValue.toString(),
                onValueChange = {},
                keyboardType = KeyboardType.Number
            )
        }
        val choiceOptionsNotifier = Modifier.padding(8.dp)
        FormFieldRow(
            label = "Favorito",
        ) {
            FormCheckbox(
                modifier = choiceOptionsNotifier,
                label = "Favorito",
                checked = contact.isFavorite,
                onCheckedChange = {}
            )
        }
        FormFieldRow(
            label = "Tipo"
        ) {
            FormRadioButton(
                modifier = choiceOptionsNotifier,
                label = "Pessoal",
                value = ContactTypeEnum.PERSONAL,
                groupValue = contact.type,
                onValueChanged = {}
            )
            FormRadioButton(
                modifier = choiceOptionsNotifier,
                label = "Profissional",
                value = ContactTypeEnum.PROFESSIONAL,
                groupValue = contact.type,
                onValueChanged = {}
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun FormContentPreview() {
    AppInterfacesTheme() {
        FormContent(
            contact = Contact()
        )
    }
}