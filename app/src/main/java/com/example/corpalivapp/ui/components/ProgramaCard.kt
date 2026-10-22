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
import androidx.compose.material3.MaterialTheme
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
    modifier: Modifier = Modifier
) {

    // Identificamos el programa
    val esEscuela = titulo.contains(
        "Escuela",
        ignoreCase = true
    )

    // COLOR DE FONDO
    val colores = MaterialTheme.colorScheme

    val colorFondo = if (esEscuela) {
        colores.surfaceVariant
    } else {
        colores.secondaryContainer
    }

    val colorPrincipal = if (esEscuela) {
        colores.primary
    } else {
        colores.secondary
    }

    val colorIcono = if (esEscuela) {
        colores.primaryContainer
    } else {
        colores.tertiaryContainer
    }

    val colorDescripcion = colores.onSurfaceVariant

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
