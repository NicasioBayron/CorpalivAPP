package com.example.corpalivapp.ui.screens

import android.app.DatePickerDialog
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.corpalivapp.viewmodel.DonacionViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.core.content.FileProvider
import java.io.File
import androidx.compose.animation.AnimatedVisibility
import com.example.corpalivapp.data.CatalogoComunas

@Composable
fun DonacionScreen(
    usuarioId: Int?,
    modifier: Modifier = Modifier,
    onVolverClick: () -> Unit,
    donacionViewModel: DonacionViewModel = viewModel()
) {
    val estado by donacionViewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    var fotoPendiente by rememberSaveable {
        mutableStateOf<String?>(null)
    }

    var mensajeCamara by rememberSaveable {
        mutableStateOf<String?>(null)
    }

    val tomarFoto = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { confirmada ->
        val ruta = fotoPendiente
        fotoPendiente = null

        if (ruta != null) {
            val archivo = File(ruta)

            if (confirmada && archivo.exists() && archivo.length() > 0) {
                val fotoAnterior = estado.fotoComprobante

                donacionViewModel.actualizarFoto(ruta)
                mensajeCamara = null

                if (fotoAnterior != null && fotoAnterior != ruta) {
                    File(fotoAnterior).delete()
                }
            } else {
                archivo.delete()
                mensajeCamara = "No se adjuntó una nueva foto."
            }
        }
    }
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        TextButton(onClick = onVolverClick, enabled = !estado.guardando,) {
            Text("Volver")
        }

        Text(
            text = "Aporte personal",
            style = MaterialTheme.typography.headlineMedium
        )

        OutlinedTextField(
            enabled = !estado.guardando,
            value = estado.nombre,
            onValueChange = donacionViewModel::actualizarNombre,
            label = { Text("Nombre completo") },
            singleLine = true,
            isError = estado.errorNombre != null,
            supportingText = {
                estado.errorNombre?.let { Text(it) }
            },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            enabled = !estado.guardando,
            value = estado.correo,
            onValueChange = donacionViewModel::actualizarCorreo,
            label = { Text("Correo electrónico") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email
            ),
            isError = estado.errorCorreo != null,
            supportingText = {
                estado.errorCorreo?.let { Text(it) }
            },
            modifier = Modifier.fillMaxWidth()
        )
        Text(
            text = "Residencia del aportante",
            style = MaterialTheme.typography.titleMedium
        )

        Text(
            text = "${CatalogoComunas.REGION}. " +
                    "Selecciona una de las ${CatalogoComunas.comunas.size} comuna disponibles"
        )

        OutlinedTextField(
            value = estado.busquedaComuna,
            onValueChange = donacionViewModel::actualizarBusquedaComuna,
            label = { Text("Buscar comuna") },
            supportingText = {
                Text(
                    estado.errorComuna
                        ?: "Escribe y selecciona una opción de la lista."
                )
            },
            singleLine = true,
            enabled = !estado.guardando,
            modifier = Modifier.fillMaxWidth(),
            isError = estado.errorComuna != null,
        )

        AnimatedVisibility(
            visible = estado.busquedaComuna.isNotBlank() &&
                    estado.comuna.isBlank()
        ) {
            val coincidencias = CatalogoComunas.filtrar(
                estado.busquedaComuna
            )

            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.surfaceContainer,
                contentColor = MaterialTheme.colorScheme.onSurface,
                border = BorderStroke(
                    width = 2.dp,
                    color = MaterialTheme.colorScheme.onSurface
                )
            ) {
                Column(
                    modifier = Modifier.padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = "Selecciona una comuna",
                        style = MaterialTheme.typography.titleSmall
                    )

                    HorizontalDivider()

                    if (coincidencias.isEmpty()) {
                        Text(
                            text = "No hay coincidencias en este catálogo.",
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    } else {
                        coincidencias.forEach { comuna ->
                            TextButton(
                                onClick = {
                                    donacionViewModel.seleccionarComuna(comuna)
                                },
                                enabled = !estado.guardando,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(comuna)
                            }
                        }
                    }
                }
            }
        }

        if (estado.comuna.isNotBlank()) {
            Text(
                text = "Comuna seleccionada: ${estado.comuna}"
            )
        }
        OutlinedTextField(
            enabled = !estado.guardando,
            value = estado.monto,
            onValueChange = donacionViewModel::actualizarMonto,
            label = { Text("Monto en pesos") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            ),
            isError = estado.errorMonto != null,
            supportingText = {
                estado.errorMonto?.let { Text(it) }
            },
            modifier = Modifier.fillMaxWidth()
        )

        val fechaTexto = estado.fecha?.format(
            DateTimeFormatter.ofPattern("dd/MM/yyyy")
        ) ?: "Seleccionar fecha del aporte"

        OutlinedButton(
            enabled = !estado.guardando,
            onClick = {
                val inicial = estado.fecha ?: LocalDate.now()

                DatePickerDialog(
                    context,
                    { _, anio, mes, dia ->
                        donacionViewModel.actualizarFecha(
                            LocalDate.of(anio, mes + 1, dia)
                        )
                    },
                    inicial.year,
                    inicial.monthValue - 1,
                    inicial.dayOfMonth
                ).show()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(fechaTexto)
        }

        estado.errorFecha?.let {
            Text(
                text = it,
                color = MaterialTheme.colorScheme.error
            )
        }

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = estado.aceptaCondiciones,
                enabled = !estado.guardando,
                onCheckedChange =
                    donacionViewModel::actualizarCondiciones
            )

            Text(
                text = "Acepto los terminos y servicios",
                modifier = Modifier.weight(1f)
            )
        }

        estado.errorCondiciones?.let {
            Text(
                text = it,
                color = MaterialTheme.colorScheme.error
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "Deseo recibir seguimiento de este aporte",
                modifier = Modifier.weight(1f)
            )

            Switch(
                checked = estado.recibirSeguimiento,
                enabled = !estado.guardando,
                onCheckedChange =
                    donacionViewModel::actualizarSeguimiento
            )
        }

        Text(
            text = "Documento de prueba",
            style = MaterialTheme.typography.titleMedium
        )

        Text(
            text = "Adjunta una fotografia de tu cedula de identidad para confirmar tu identidad"
        )

        OutlinedButton(
            enabled = !estado.guardando && fotoPendiente == null,
            onClick = {
                var archivoNuevo: File? = null

                try {
                    val carpeta = File(context.filesDir, "comprobantes")

                    check(carpeta.exists() || carpeta.mkdirs()) {
                        "No se pudo crear la carpeta."
                    }

                    val archivo = File.createTempFile(
                        "documento_",
                        ".jpg",
                        carpeta
                    )

                    archivoNuevo = archivo
                    fotoPendiente = archivo.absolutePath

                    val uri = FileProvider.getUriForFile(
                        context,
                        "${context.packageName}.fileprovider",
                        archivo
                    )

                    mensajeCamara = null
                    tomarFoto.launch(uri)
                } catch (error: Exception) {
                    archivoNuevo?.delete()
                    fotoPendiente = null
                    mensajeCamara =
                        "No se pudo abrir la cámara. Comprueba que esté disponible."
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                if (estado.fotoComprobante == null) {
                    "Fotografiar documento"
                } else {
                    "Volver a tomar foto"
                }
            )
        }

        if (estado.fotoComprobante != null) {
            Text(
                text = "Documento adjunto — verificación lista."
            )
        }

        mensajeCamara?.let {
            Text(
                text = it,
                color = MaterialTheme.colorScheme.error
            )
        }

        Button(
            onClick = {
                donacionViewModel.guardar(usuarioId)
            },
            enabled = !estado.guardando,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                if (estado.guardando) {
                    "Guardando..."
                } else {
                    "Registrar aporte"
                }
            )
        }

        estado.mensaje?.let {
            Text(it)
        }
    }
}