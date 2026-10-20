package com.example.corpalivapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun UsuarioScreen(
    modifier: Modifier = Modifier,
    onMenuClick: () -> Unit = {},
    onInicioClick: () -> Unit = {}
) {

    val verde = Color(0xFF286657)
    val fondo = Color(0xFFFAF8F5)

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(fondo)
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
                text = "Mi perfil",
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
                text = "Mi perfil",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = verde
            )

            Text("Información de tu cuenta")

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFE8EFE9)
                ),
                shape = RoundedCornerShape(16.dp)
            ) {

                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {

                    Text(
                        text = "Nombre",
                        fontWeight = FontWeight.Bold,
                        color = verde
                    )

                    Text("Sin información disponible")

                    HorizontalDivider()

                    Text(
                        text = "Correo electrónico",
                        fontWeight = FontWeight.Bold,
                        color = verde
                    )

                    Text("Sin información disponible")
                }
            }

            Button(
                onClick = onInicioClick,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = verde
                )
            ) {
                Text("Volver al inicio")
            }
        }
    }
}