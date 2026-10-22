package com.example.corpalivapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.corpalivapp.viewmodel.HistorialAportesViewModel
import java.text.NumberFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun HistorialAportesScreen(
    usuarioId: Int,
    onVolverClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val historialViewModel: HistorialAportesViewModel = viewModel(
        key = "historial_aportes_$usuarioId"
    )

    val estado by historialViewModel.uiState
        .collectAsStateWithLifecycle()

    LaunchedEffect(usuarioId) {
        historialViewModel.cargar(usuarioId)
    }

    val formatoMonto = NumberFormat.getIntegerInstance(
        Locale.forLanguageTag("es-CL")
    )

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            TextButton(onClick = onVolverClick) {
                Text("Volver al perfil")
            }
        }

        item {
            Text(
                text = "Mis aportes",
                style = MaterialTheme.typography.headlineMedium
            )
        }

        item {
            Text("Aqui veras tus aportes pasados")
        }

        when {
            estado.cargando -> {
                item {
                    CircularProgressIndicator()
                }
            }

            estado.error != null -> {
                item {
                    Text(
                        text = estado.error.orEmpty(),
                        color = MaterialTheme.colorScheme.error
                    )

                    TextButton(
                        onClick = {
                            historialViewModel.cargar(usuarioId)
                        }
                    ) {
                        Text("Reintentar")
                    }
                }
            }

            estado.aportes.isEmpty() -> {
                item {
                    Text("Todavía no tienes aportes registrados.")
                }
            }

            else -> {
                items(
                    items = estado.aportes,
                    key = { aporte -> aporte.id }
                ) { aporte ->

                    val fecha = runCatching {
                        LocalDate.parse(aporte.fechaAporte).format(
                            DateTimeFormatter.ofPattern("dd/MM/yyyy")
                        )
                    }.getOrDefault(aporte.fechaAporte)

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor =
                                MaterialTheme.colorScheme.surfaceVariant
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(20.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = "Aporte #${aporte.id}",
                                style = MaterialTheme.typography.titleMedium
                            )

                            Text(
                                text = "Monto: $" +
                                        formatoMonto.format(aporte.monto)
                            )

                            Text("Fecha prevista: $fecha")

                            Text(
                                text = "Región: " +
                                        (aporte.region ?: "No registrada")
                            )

                            Text(
                                text = "Comuna: " +
                                        (aporte.comuna ?: "No registrada")
                            )

                            Text(
                                text = if (aporte.fotoComprobante != null) {
                                    "Documento de prueba: registrado"
                                } else {
                                    "Documento de prueba: no registrado"
                                }
                            )

                            Text(
                                text = if (aporte.recibirSeguimiento) {
                                    "Seguimiento solicitado"
                                } else {
                                    "Sin seguimiento solicitado"
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}