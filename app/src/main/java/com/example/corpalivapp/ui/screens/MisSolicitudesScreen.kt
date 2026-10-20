package com.example.corpalivapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import com.example.corpalivapp.data.DatabaseProvider
import com.example.corpalivapp.data.SolicitudContacto

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun MisSolicitudesScreen(
    usuarioId: Int,
    modifier: Modifier = Modifier,
    modoOscuro: Boolean = false,
    onMenuClick: () -> Unit = {}
) {
    val verde = Color(0xFF286657)

    // COLORES SEGÚN EL MODO
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

    val colorTitulo = if (modoOscuro) {
        Color(0xFFB5E2C8)
    } else {
        verde
    }

    val fondoTarjeta = if (modoOscuro) {
        Color(0xFF30483C)
    } else {
        Color.White
    }

    val colorSecundario = if (modoOscuro) {
        Color(0xFFCBD5CD)
    } else {
        Color.Gray
    }

    val colorDivisor = if (modoOscuro) {
        Color(0xFF64776A)
    } else {
        Color(0xFFDDDDDD)
    }

    val context = LocalContext.current

    var solicitudes by remember(usuarioId) {
        mutableStateOf<List<SolicitudContacto>>(emptyList())
    }

    var cargando by remember(usuarioId) {
        mutableStateOf(true)
    }

    var error by remember(usuarioId) {
        mutableStateOf(false)
    }

    // Consultar solicitudes usuario room
    LaunchedEffect(usuarioId) {
        cargando = true
        error = false

        try {
            solicitudes = withContext(Dispatchers.IO) {
                DatabaseProvider
                    .obtenerDatabase(context)
                    .solicitudContactoDao()
                    .obtenerSolicitudesPorUsuario(usuarioId)
            }
        } catch (e: Exception) {
            error = true
        } finally {
            cargando = false
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(fondo)
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
                text = "Mis solicitudes",
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
        when {
            cargando -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        color = colorTitulo
                    )
                }
            }

            error -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No se pudieron cargar las solicitudes.",
                        color = colorTexto
                    )
                }
            }

            solicitudes.isEmpty() -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Todavía no tienes solicitudes registradas.",
                        color = colorSecundario
                    )
                }
            }

            else -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(solicitudes) { solicitud ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = fondoTarjeta
                            )
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Text(
                                    text = solicitud.tipoColaboracion,
                                    color = colorTitulo,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold
                                )

                                Text(
                                    text = solicitud.mensaje,
                                    color = colorTexto
                                )

                                HorizontalDivider(
                                    color = colorDivisor
                                )

                                Text(
                                    text = "Nombre: ${solicitud.nombre}",
                                    fontSize = 13.sp,
                                    color = colorTexto
                                )

                                Text(
                                    text = "Correo: ${solicitud.correo}",
                                    fontSize = 13.sp,
                                    color = colorTexto
                                )

                                val fecha = remember(
                                    solicitud.fechaRegistro
                                ) {
                                    SimpleDateFormat(
                                        "dd/MM/yyyy HH:mm",
                                        Locale.getDefault()
                                    ).format(
                                        Date(solicitud.fechaRegistro)
                                    )
                                }

                                Text(
                                    text = "Fecha: $fecha",
                                    fontSize = 12.sp,
                                    color = colorSecundario
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}