package com.example.corpalivapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.corpalivapp.ui.theme.VerdeCorpaliv

@Composable
fun AportesScreen(
    modifier: Modifier = Modifier,
    onMenuClick: () -> Unit = {},
    onContactoClick: () -> Unit = {}
) {

    val verde = VerdeCorpaliv
    val fondo = MaterialTheme.colorScheme.background
    val colorTexto = MaterialTheme.colorScheme.onBackground
    val colorTitulo = MaterialTheme.colorScheme.primary

    val fondoVerde = MaterialTheme.colorScheme.surfaceVariant
    val fondoBeige = MaterialTheme.colorScheme.secondaryContainer
    val colorVoluntariado = MaterialTheme.colorScheme.secondary

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
                text = "Aportes",
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
                text = "¡Sé parte del cambio!",
                color = colorTitulo,
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "Tu colaboración nos ayuda a seguir promoviendo la inclusión social y laboral.",
                color = colorTexto
            )

            // Dona
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = fondoVerde
                ),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp)
                ) {
                    Text(
                        text = "Donaciones",
                        fontWeight = FontWeight.Bold,
                        color = colorTitulo
                    )

                    Text(
                        text = "Apoya los programas y actividades de CORPALIV.",
                        color = colorTexto
                    )
                }
            }

            // Volun
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = fondoBeige
                ),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp)
                ) {
                    Text(
                        text = "Voluntariado",
                        fontWeight = FontWeight.Bold,
                        color = colorVoluntariado
                    )

                    Text(
                        text = "Participa en nuestras actividades e iniciativas.",
                        color = colorTexto
                    )
                }
            }

            // Ali
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = fondoVerde
                ),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp)
                ) {
                    Text(
                        text = "Alianzas con empresas",
                        fontWeight = FontWeight.Bold,
                        color = colorTitulo
                    )

                    Text(
                        text = "Colabora con nuestros programas de inclusión laboral.",
                        color = colorTexto
                    )
                }
            }

            // Boton contacto
            Button(
                onClick = onContactoClick,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = verde,
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Solicitar contacto")
            }
        }
    }
}