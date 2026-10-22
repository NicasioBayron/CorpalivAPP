package com.example.corpalivapp.ui.screens

import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.checkSelfPermission
import com.example.corpalivapp.notifications.Notificaciones
import com.example.corpalivapp.ui.theme.TextoSecundarioSobreVerde
import com.example.corpalivapp.ui.theme.TextoSobreVerde
import com.example.corpalivapp.ui.theme.VerdeCorpaliv
import java.util.jar.Manifest
import kotlin.contracts.contract


@Composable
fun AjustesScreen(
    modifier: Modifier = Modifier,
    modoOscuro: Boolean = false,
    onModoOscuroChange: (Boolean) -> Unit = {},
    onMenuClick: () -> Unit = {},
    onPerfilClick: () -> Unit = {},
    onSolicitudesClick: () -> Unit = {}
) {
    val verde = VerdeCorpaliv
    val fondo = MaterialTheme.colorScheme.background
    val colorTexto = MaterialTheme.colorScheme.onBackground
    val colorTarjeta = MaterialTheme.colorScheme.surfaceVariant

    val context = LocalContext.current
    var mensajeNotificacion by remember {
        mutableStateOf("")
    }
    val enviarPrueba: () -> Unit = {
        val enviada = Notificaciones.enviarPrueba(context)
        mensajeNotificacion = if (enviada){
            "Notificacion enviada, revisa tu bandeja"
        } else {
            "Notificaciones no permitidas, revisa los ajustes, ojala poder dormir"
        }
    }
    val solicitarPermisoNotificaciones = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { permitido ->
        if (permitido) {
            enviarPrueba()
        } else {
            mensajeNotificacion = "Nose concedio permiso, concede pe"
        }
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
                text = "Ajustes",
                color = TextoSobreVerde,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            TextButton(onClick = onMenuClick) {
                Text(
                    text = "☰",
                    color = TextoSobreVerde,
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
                text = "Configuración",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            Text(
                text = "Personaliza tu experiencia en CORPALIV.",
                color = colorTexto
            )

            // APARIENCIA
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = colorTarjeta
                ),
                shape = RoundedCornerShape(16.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = "Modo oscuro",
                            fontWeight = FontWeight.Bold,
                            color = colorTexto
                        )

                        Text(
                            text = "Cambia la apariencia de Ajustes.",
                            fontSize = 12.sp,
                            color = colorTexto
                        )
                    }

                    Switch(
                        checked = modoOscuro,
                        onCheckedChange = onModoOscuroChange
                    )
                }
            }
            Text(
                text = "Notificaciones",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = colorTexto
            )
            Button(
                onClick = {
                    Notificaciones.crearCanal(context)
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
                        val permiso = ContextCompat.checkSelfPermission(
                            context,
                            android.Manifest.permission.POST_NOTIFICATIONS
                        )
                        if (permiso == PackageManager.PERMISSION_GRANTED){
                            enviarPrueba()
                        } else {
                            solicitarPermisoNotificaciones.launch(
                                android.Manifest.permission.POST_NOTIFICATIONS
                            )
                        }
                    } else {
                        enviarPrueba()
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Enviar notificaciones de prueba")
            }

            if (mensajeNotificacion.isNotEmpty()){
                Text(
                    text = mensajeNotificacion,
                    color = colorTexto
                )
            }

            // ACCESOS RÁPIDOS
            Text(
                text = "Mi cuenta",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = colorTexto
            )

            Button(
                onClick = onPerfilClick,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = verde
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Ver mi perfil")
            }

            Button(
                onClick = onSolicitudesClick,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = verde
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Mis solicitudes")
            }

            // INFORMACIÓN
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer
                ),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Acerca de la aplicación",
                        fontWeight = FontWeight.Bold,
                        color = colorTexto
                    )

                    Text(
                        text = "CORPALIV es una aplicación orientada a promover la inclusión y facilitar la colaboración.",
                        color = colorTexto
                    )

                    Text(
                        text = "Versión de demostración 1.0",
                        color = colorTexto,
                        fontSize = 12.sp
                    )
                }
            }
        }
    }
}