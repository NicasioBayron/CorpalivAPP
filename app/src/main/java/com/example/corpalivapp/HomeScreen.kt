package com.example.corpalivapp

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material3.ButtonDefaults
import androidx.compose.foundation.shape.RoundedCornerShape

import com.example.corpalivapp.ui.components.ProgramaCard
import com.example.corpalivapp.ui.theme.CorpalivAppTheme

@Composable
fun HomeScreen(modifier: Modifier = Modifier) {

    var mostrarMensaje by remember { mutableStateOf(false) }

    val verdeCorpaliv = Color(0xFF286657)
    val fondoCrema = Color(0xFFFAF8F5)

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(fondoCrema)
            .verticalScroll(rememberScrollState())
    ) {

        // Aqui esta el encabezado verde
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(verdeCorpaliv)
                .statusBarsPadding()
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    text = "CORPALIV",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )

                IconButton(
                    onClick = {
                        // Aquí agregaremos el menú
                    }
                ) {
                    Text(
                        text = "☰",
                        color = Color.White,
                        fontSize = 28.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Construyendo Inclusión Juntos",
                color = Color.White,
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                lineHeight = 36.sp
            )

            Text(
                text = "Conoce nuestra misión y descubre cómo puedes ser parte del cambio.",
                color = Color(0xFFE4F2EC),
                fontSize = 15.sp,
                lineHeight = 23.sp
            )
        }

        // CONTENIDO PRINCIPAL
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            Text(
                text = "Nuestra Misión",
                style = MaterialTheme.typography.headlineSmall,
                color = verdeCorpaliv
            )

            Text(
                text = "Promovemos la autonomía, participación e inclusión social y laboral de personas con discapacidad múltiple.",
                style = MaterialTheme.typography.bodyLarge
            )

            Text(
                text = "Conoce nuestros programas",
                style = MaterialTheme.typography.headlineSmall,
                color = verdeCorpaliv
            )

            ProgramaCard(
                titulo = "Escuela Jan Van Dijk",
                descripcion = "Educación, formación e inclusión social."
            )

            ProgramaCard(
                titulo = "Alba Lab",
                descripcion = "Formación e inclusión sociolaboral."
            )

            Button(
                onClick = {
                    mostrarMensaje = true
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = verdeCorpaliv,
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(text = "Quiero Colaborar")
            }

            if (mostrarMensaje) {
                Text(
                    text = "¡Gracias por Colaborar!",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
} 

// Vista previa
@Preview(showBackground = true)
@Composable
fun inicioPreview() {
    CorpalivAppTheme {
        HomeScreen()
    }
}

