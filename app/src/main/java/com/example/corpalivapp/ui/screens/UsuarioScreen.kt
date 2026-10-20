package com.example.corpalivapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
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
import com.example.corpalivapp.data.Usuario

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun UsuarioScreen(
    usuarioId: Int,
    modifier: Modifier = Modifier,
    modoOscuro: Boolean = false,
    onMenuClick: () -> Unit = {},
    onInicioClick: () -> Unit = {},
    onCerrarSesion: () -> Unit = {}
) {
    val verde = Color(0xFF286657)

    // Colores segun modo
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
        Color(0xFFE8EFE9)
    }

    val context = LocalContext.current

    var usuario by remember(usuarioId) {
        mutableStateOf<Usuario?>(null)
    }

    var cargando by remember(usuarioId) {
        mutableStateOf(true)
    }

    // BUSCAR LOS DATOS DEL USUARIO EN ROOM
    LaunchedEffect(usuarioId) {
        cargando = true

        usuario = try {
            withContext(Dispatchers.IO) {
                val dao = DatabaseProvider
                    .obtenerDatabase(context)
                    .usuarioDao()

                dao.buscarPorId(usuarioId)
            }
        } catch (e: Exception) {
            null
        }

        cargando = false
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

        // Contenido
        Column(
            modifier = Modifier.padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            Text(
                text = "Mi perfil",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = colorTitulo
            )

            Text(
                text = "Información de tu cuenta",
                color = colorTexto
            )

            if (cargando) {
                CircularProgressIndicator(
                    color = colorTitulo
                )
            } else if (usuario == null) {
                Text(
                    text = "No se pudo cargar la información del usuario",
                    color = colorTexto
                )
            } else {

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = fondoTarjeta
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
                            color = colorTitulo
                        )

                        Text(
                            text = usuario!!.nombre,
                            color = colorTexto
                        )

                        HorizontalDivider(
                            color = if (modoOscuro) {
                                Color(0xFF64776A)
                            } else {
                                Color(0xFFCED8D0)
                            }
                        )

                        Text(
                            text = "Correo electrónico",
                            fontWeight = FontWeight.Bold,
                            color = colorTitulo
                        )

                        Text(
                            text = usuario!!.correo,
                            color = colorTexto
                        )
                    }
                }
            }

            // Volver al inicio
            Button(
                onClick = onInicioClick,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = verde,
                    contentColor = Color.White
                )
            ) {
                Text("Volver al inicio")
            }

            // Cerrar sesion
            OutlinedButton(
                onClick = onCerrarSesion,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = colorTitulo
                )
            ) {
                Text(
                    text = "Cerrar sesión",
                    color = colorTitulo
                )
            }
        }
    }
}