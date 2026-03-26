package com.example.appinterfaces.ui.shared.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CloudOff
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.appinterfaces.ui.theme.AppInterfacesTheme

@Composable
fun DefaultErrorState(
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
fun DefaultErrorStatePreview(modifier: Modifier = Modifier) {
    AppInterfacesTheme{
        DefaultErrorState(onTryAgainPressed = {})
    }
}