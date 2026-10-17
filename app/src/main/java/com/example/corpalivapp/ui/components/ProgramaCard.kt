package com.example.corpalivapp.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


@Composable
fun ProgramaCard(titulo : String, descripcion : String, modifier: Modifier = Modifier){
    Card(modifier = modifier.fillMaxWidth()) {
        Column(modifier = modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)){
            Text(
                text = titulo,
                style = MaterialTheme.typography.titleLarge
            )
            Text(
                text = descripcion,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}