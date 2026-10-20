package com.example.corpalivapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ProgramaCard(
    titulo: String,
    descripcion: String,
    modifier: Modifier = Modifier,
    modoOscuro: Boolean = false
) {

    // Identificamos el programa
    val esEscuela = titulo.contains(
        "Escuela",
        ignoreCase = true
    )

    // COLOR DE FONDO
    val colorFondo = if (modoOscuro) {
        if (esEscuela) {
            Color(0xFF30483C)
        } else {
            Color(0xFF4A3B32)
        }
    } else {
        if (esEscuela) {
            Color(0xFFE8EFE9)
        } else {
            Color(0xFFF4E8D9)
        }
    }

    // COLOR DEL TÍTULO
    val colorPrincipal = if (modoOscuro) {
        if (esEscuela) {
            Color(0xFFB5E2C8)
        } else {
            Color(0xFFFFD3A8)
        }
    } else {
        if (esEscuela) {
            Color(0xFF286657)
        } else {
            Color(0xFF935A30)
        }
    }

    // COLOR DEL ICONO
    val colorIcono = if (modoOscuro) {
        if (esEscuela) {
            Color(0xFF466453)
        } else {
            Color(0xFF665043)
        }
    } else {
        if (esEscuela) {
            Color(0xFFD0E2D6)
        } else {
            Color(0xFFEBD5BC)
        }
    }

    // COLOR DE LA DESCRIPCIÓN
    val colorDescripcion = if (modoOscuro) {
        Color(0xFFE5EAE5)
    } else {
        Color(0xFF48534D)
    }

    val simbolo = if (esEscuela) "🎓" else "💼"

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = colorFondo
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 0.dp
        )
    ) {

        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.Start
        ) {

            // ICONO
            Box(
                modifier = Modifier
                    .background(
                        color = colorIcono,
                        shape = RoundedCornerShape(12.dp)
                    )
                    .padding(12.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = simbolo,
                    fontSize = 26.sp
                )
            }

            // TÍTULO
            Text(
                text = titulo,
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                color = colorPrincipal
            )

            // DESCRIPCIÓN
            Text(
                text = descripcion,
                fontSize = 14.sp,
                lineHeight = 21.sp,
                color = colorDescripcion
            )
        }
    }
}
