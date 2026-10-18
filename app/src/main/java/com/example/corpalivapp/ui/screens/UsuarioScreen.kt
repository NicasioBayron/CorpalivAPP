package com.example.corpalivapp.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun UsuarioScreen(modifier: Modifier = Modifier){
    Column(
        modifier = modifier.padding(24.dp)
    ) {
        Text(text = "HOALMIdn#O hola usuario xd")
    }
}