package com.example.corpalivapp.ui.screens

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.corpalivapp.viewmodel.PerfilViewModel
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp



import com.example.corpalivapp.ui.theme.VerdeCorpaliv




@Composable
fun UsuarioScreen(
    usuarioId: Int,
    modifier: Modifier = Modifier,
    onMenuClick: () -> Unit = {},
    onInicioClick: () -> Unit = {},
    onCerrarSesion: () -> Unit = {},
    perfilViewModel: PerfilViewModel = viewModel()
) {
    val verde = VerdeCorpaliv

    // Colores segun modo
    val fondo = MaterialTheme.colorScheme.background

    val colorTexto = MaterialTheme.colorScheme.onBackground

    val colorTitulo = MaterialTheme.colorScheme.primary

    val fondoTarjeta = MaterialTheme.colorScheme.surfaceVariant

    val estado by perfilViewModel.uiState.collectAsStateWithLifecycle()
    val usuario = estado.usuario
    val cargando = estado.cargando

    LaunchedEffect(usuarioId) {
        perfilViewModel.cargar(usuarioId)
    }

    val selectorFoto = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        // Cancelar el selector no modifica la foto actual.
        if (uri != null) perfilViewModel.cambiarFoto(usuarioId, uri)
    }
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

            if (usuario != null && !cargando) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    val foto = estado.foto
                    if (foto != null) {
                        Image(
                            bitmap = foto.asImageBitmap(),
                            contentDescription = "Foto de perfil",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.size(112.dp).clip(CircleShape)
                        )
                    } else {
                        Surface(
                            modifier = Modifier.size(112.dp),
                            shape = CircleShape,
                            color = MaterialTheme.colorScheme.surfaceVariant
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    text = usuario.nombre.take(1).uppercase(),
                                    style = MaterialTheme.typography.headlineLarge,
                                    color = colorTitulo
                                )
                            }
                        }
                    }

                    OutlinedButton(
                        enabled = !estado.guardandoFoto,
                        onClick = {
                            selectorFoto.launch(
                                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                            )
                        }
                    ) {
                        Text(if (estado.guardandoFoto) "Guardando foto…" else "Cambiar foto")
                    }
                    if (estado.guardandoFoto) CircularProgressIndicator()
                }
            }

            estado.mensaje?.let { mensaje ->
                Text(
                    text = mensaje,
                    color = if (estado.esError) MaterialTheme.colorScheme.error else colorTexto
                )
            }
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
                            color = MaterialTheme.colorScheme.outlineVariant
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