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
import androidx.compose.ui.platform.LocalContext

import com.example.corpalivapp.data.DatabaseProvider
import com.example.corpalivapp.data.SolicitudContacto
import com.example.corpalivapp.viewmodel.validation.LoginValidator

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun ContactoScreen(
    modifier: Modifier = Modifier,
    usuarioId: Int? = null,
    onMenuClick: () -> Unit = {}
) {
    val verde = Color(0xFF286657)
    val fondo = Color(0xFFFAF8F5)

    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var nombre by remember { mutableStateOf("") }
    var correo by remember { mutableStateOf("") }
    var mensaje by remember { mutableStateOf("") }
    var tipoColaboracion by remember {
        mutableStateOf("Donaciones")
    }

    var guardando by remember { mutableStateOf(false) }
    var mensajeResultado by remember { mutableStateOf("") }
    var guardadoExitoso by remember { mutableStateOf(false) }

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
                onValueChange = {
                    nombre = it
                    mensajeResultado = ""
                },
                label = { Text("Nombre completo") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                enabled = !guardando
            )

            OutlinedTextField(
                value = correo,
                onValueChange = {
                    correo = it
                    mensajeResultado = ""
                },
                label = { Text("Correo electrónico") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                enabled = !guardando
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
                            mensajeResultado = ""
                        },
                        enabled = !guardando
                    )

                    Text(text = opcion)
                }
            }

            OutlinedTextField(
                value = mensaje,
                onValueChange = {
                    mensaje = it
                    mensajeResultado = ""
                },
                label = { Text("Mensaje") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(130.dp),
                maxLines = 5,
                enabled = !guardando
            )

            // BOTÓN GUARDAR
            Button(
                onClick = {
                    val nombreLimpio = nombre.trim()
                    val correoLimpio = correo.trim().lowercase()
                    val mensajeLimpio = mensaje.trim()

                    val errorCorreo =
                        LoginValidator.validarCorreo(correoLimpio)

                    if (errorCorreo != null) {
                        guardadoExitoso = false
                        mensajeResultado = errorCorreo
                    } else {
                        guardando = true
                        mensajeResultado = ""

                        scope.launch {
                            try {
                                val solicitud = SolicitudContacto(
                                    nombre = nombreLimpio,
                                    correo = correoLimpio,
                                    tipoColaboracion = tipoColaboracion,
                                    mensaje = mensajeLimpio,
                                    usuarioId = usuarioId
                                )

                                withContext(Dispatchers.IO) {
                                    DatabaseProvider
                                        .obtenerDatabase(context)
                                        .solicitudContactoDao()
                                        .guardarSolicitud(solicitud)
                                }

                                // LIMPIAR FORMULARIO
                                nombre = ""
                                correo = ""
                                mensaje = ""
                                tipoColaboracion = "Donaciones"

                                guardadoExitoso = true
                                mensajeResultado =
                                    "Solicitud guardada correctamente."

                            } catch (e: Exception) {
                                guardadoExitoso = false
                                mensajeResultado =
                                    "No se pudo guardar la solicitud."
                            } finally {
                                guardando = false
                            }
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = verde
                ),
                shape = RoundedCornerShape(12.dp),
                enabled = !guardando &&
                        nombre.isNotBlank() &&
                        correo.isNotBlank() &&
                        mensaje.isNotBlank()
            ) {
                if (guardando) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = Color.White,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text("Enviar solicitud")
                }
            }

            if (mensajeResultado.isNotEmpty()) {
                Text(
                    text = mensajeResultado,
                    color = if (guardadoExitoso) {
                        verde
                    } else {
                        MaterialTheme.colorScheme.error
                    },
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}