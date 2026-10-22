package com.example.corpalivapp.ui.components

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import com.example.corpalivapp.ui.theme.VerdeCorpaliv

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuDeslizante(
    onMenuClick: () -> Unit
) {

    val verdeCorpaliv = VerdeCorpaliv

    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = verdeCorpaliv),

        navigationIcon = {
            TextButton(
                onClick = onMenuClick
            ) {
                Text(
                    text = "☰ Menú",
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }
        },

        title = {
            Text(
                text = "CORPALIV",
                color = Color.White)
        }
    )
}