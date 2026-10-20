package com.example.corpalivapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun AjustesScreen(
    modifier: Modifier = Modifier,
    modoOscuro: Boolean = false,
    onModoOscuroChange: (Boolean) -> Unit = {},
    onMenuClick: () -> Unit = {},
    onPerfilClick: () -> Unit = {},
    onSolicitudesClick: () -> Unit = {}
) {
    val verde = Color(0xFF286657)

    val fondo = if (modoOscuro) {
        Color(0xFF202820)
    } else {
        Color(0xFFFAF8F5)
    }

    val colorTexto = if (modoOscuro) {
        Color.White
    } else {
        Color(0xFF35443D)
    }

    val colorTarjeta = if (modoOscuro) {
        Color(0xFF364238)
    } else {
        Color(0xFFE8EFE9)
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(fondo)
            .verticalScroll(rememberScrollState())
    ) {

        // ENCABEZADO
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(verde)
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Ajustes",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            TextButton(onClick = onMenuClick) {
                Text(
                    text = "☰",
                    color = Color.White,
                    fontSize = 26.sp
                )
            }
        }

        // CONTENIDO
        Column(
            modifier = Modifier.padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            Text(
                text = "Configuración",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = if (modoOscuro) Color.White else verde
            )

            Text(
                text = "Personaliza tu experiencia en CORPALIV.",
                color = colorTexto
            )

            // APARIENCIA
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = colorTarjeta
                ),
                shape = RoundedCornerShape(16.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = "Modo oscuro",
                            fontWeight = FontWeight.Bold,
                            color = colorTexto
                        )

                        Text(
                            text = "Cambia la apariencia de Ajustes.",
                            fontSize = 12.sp,
                            color = colorTexto
                        )
                    }

                    Switch(
                        checked = modoOscuro,
                        onCheckedChange = onModoOscuroChange
                    )
                }
            }

            // ACCESOS RÁPIDOS
            Text(
                text = "Mi cuenta",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = colorTexto
            )

            Button(
                onClick = onPerfilClick,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = verde
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Ver mi perfil")
            }

            Button(
                onClick = onSolicitudesClick,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = verde
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Mis solicitudes")
            }

            // INFORMACIÓN
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = if (modoOscuro) {
                        Color(0xFF364238)
                    } else {
                        Color(0xFFF4E8D9)
                    }
                ),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Acerca de la aplicación",
                        fontWeight = FontWeight.Bold,
                        color = colorTexto
                    )

                    Text(
                        text = "CORPALIV es una aplicación orientada a promover la inclusión y facilitar la colaboración.",
                        color = colorTexto
                    )

                    Text(
                        text = "Versión de demostración 1.0",
                        color = colorTexto,
                        fontSize = 12.sp
                    )
                }
            }
        }
    }
}