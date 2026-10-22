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
import com.example.corpalivapp.ui.theme.VerdeCorpaliv

@Composable
fun NosotrosScreen(
    modifier: Modifier = Modifier,
    onMenuClick: () -> Unit = {}
) {

   val verde = VerdeCorpaliv
   val fondo = MaterialTheme.colorScheme.background
   val colorTexto = MaterialTheme.colorScheme.onBackground
   val colorTitulo = MaterialTheme.colorScheme.primary

    val fondoTarjetaVerde = MaterialTheme.colorScheme.surfaceVariant
    val fondoTarjetaBeige = MaterialTheme.colorScheme.secondaryContainer
    val colorTituloBeige = MaterialTheme.colorScheme.secondary

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(fondo)
            .verticalScroll(rememberScrollState())
    ) {

        // Encabezado
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

        // Contenido
        Column(
            modifier = Modifier.padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            Text(
                text = "Conoce CORPALIV",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = colorTitulo
            )

            Text(
                text = "Trabajamos para promover oportunidades de inclusión y participación.",
                color = colorTexto
            )

            // Quienes somos
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = fondoTarjetaVerde
                ),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp)
                ) {
                    Text(
                        text = "¿Quiénes somos?",
                        fontWeight = FontWeight.Bold,
                        color = colorTitulo,
                        fontSize = 18.sp
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "CORPALIV es una organización enfocada en promover la inclusión y generar oportunidades para las personas.",
                        color = colorTexto
                    )
                }
            }

            // Mision
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = fondoTarjetaBeige
                ),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp)
                ) {
                    Text(
                        text = "Nuestra misión",
                        fontWeight = FontWeight.Bold,
                        color = colorTituloBeige,
                        fontSize = 18.sp
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Impulsar espacios de aprendizaje, desarrollo y participación inclusiva.",
                        color = colorTexto
                    )
                }
            }

            // Programas
            Text(
                text = "Nuestros programas",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = colorTitulo
            )

            // Escuela JAN VAN DIJK
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = fondoTarjetaVerde
                ),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp)
                ) {
                    Text(
                        text = "Escuela Jan Van Dijk",
                        fontWeight = FontWeight.Bold,
                        color = colorTitulo
                    )

                    Text(
                        text = "Programa orientado a la educación y el desarrollo.",
                        color = colorTexto
                    )
                }
            }

            // ALBA LAB
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = fondoTarjetaBeige
                ),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp)
                ) {
                    Text(
                        text = "Alba Lab",
                        fontWeight = FontWeight.Bold,
                        color = colorTituloBeige
                    )

                    Text(
                        text = "Programa enfocado en la inclusión y las oportunidades laborales.",
                        color = colorTexto
                    )
                }
            }
        }
    }
}