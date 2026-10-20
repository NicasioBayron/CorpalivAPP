package com.example.corpalivapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ContactoScreen(
    modifier: Modifier = Modifier,
    onMenuClick: () -> Unit = {}
) {

    val verde = Color(0xFF286657)
    val fondo = Color(0xFFFAF8F5)

    // Datos del formulario
    var nombre by remember { mutableStateOf("") }
    var correo by remember { mutableStateOf("") }
    var mensaje by remember { mutableStateOf("") }
    var tipoColaboracion by remember {
        mutableStateOf("Donaciones")
    }
    var mostrarMensaje by remember {
        mutableStateOf(false)
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
                text = "Contáctanos",
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

        // FORMULARIO
        Column(
            modifier = Modifier.padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            Text(
                text = "¡Hablemos!",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = verde
            )

            Text(
                text = "Completa el formulario y cuéntanos cómo te gustaría colaborar con CORPALIV."
            )

            OutlinedTextField(
                value = nombre,
                onValueChange = { nombre = it },
                label = { Text("Nombre completo") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            OutlinedTextField(
                value = correo,
                onValueChange = { correo = it },
                label = { Text("Correo electrónico") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Text(
                text = "¿Cómo deseas colaborar?",
                fontWeight = FontWeight.Bold,
                color = verde
            )

            val opciones = listOf(
                "Donaciones",
                "Voluntariado",
                "Alianzas con empresas",
                "Consulta general"
            )

            opciones.forEach { opcion ->
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = tipoColaboracion == opcion,
                        onClick = {
                            tipoColaboracion = opcion
                        }
                    )

                    Text(text = opcion)
                }
            }

            OutlinedTextField(
                value = mensaje,
                onValueChange = { mensaje = it },
                label = { Text("Mensaje") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(130.dp),
                maxLines = 5
            )

            Button(
                onClick = {
                    mostrarMensaje = true
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = verde
                ),
                shape = RoundedCornerShape(12.dp),
                enabled = nombre.isNotBlank() &&
                        correo.isNotBlank() &&
                        mensaje.isNotBlank()
            ) {
                Text("Enviar solicitud")
            }

            if (mostrarMensaje) {
                Text(
                    text = "Formulario completado correctamente (demostración).",
                    color = verde,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}