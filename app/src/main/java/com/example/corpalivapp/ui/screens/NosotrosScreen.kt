package com.example.corpalivapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
fun NosotrosScreen(
    modifier: Modifier = Modifier,
    onMenuClick: () -> Unit = {}
) {

    val verde = Color(0xFF286657)
    val fondo = Color(0xFFFAF8F5)

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
                text = "Sobre nosotros",
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
                text = "Conoce CORPALIV",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = verde
            )

            Text(
                text = "Trabajamos para promover oportunidades de inclusión y participación."
            )

            // QUIÉNES SOMOS
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFE8EFE9)
                ),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {

                    Text(
                        text = "¿Quiénes somos?",
                        fontWeight = FontWeight.Bold,
                        color = verde,
                        fontSize = 18.sp
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "CORPALIV es una organización enfocada en promover la inclusión y generar oportunidades para las personas."
                    )
                }
            }

            // MISIÓN
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFF4E8D9)
                ),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {

                    Text(
                        text = "Nuestra misión",
                        fontWeight = FontWeight.Bold,
                        color = verde,
                        fontSize = 18.sp
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Impulsar espacios de aprendizaje, desarrollo y participación inclusiva."
                    )
                }
            }

            // PROGRAMAS
            Text(
                text = "Nuestros programas",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = verde
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFE8EFE9)
                ),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {

                    Text(
                        text = "Escuela Jan Van Dijk",
                        fontWeight = FontWeight.Bold,
                        color = verde
                    )

                    Text(
                        text = "Programa orientado a la educación y el desarrollo."
                    )
                }
            }

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFF4E8D9)
                ),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {

                    Text(
                        text = "Alba Lab",
                        fontWeight = FontWeight.Bold,
                        color = verde
                    )

                    Text(
                        text = "Programa enfocado en la inclusión y las oportunidades laborales."
                    )
                }
            }
        }
    }
}