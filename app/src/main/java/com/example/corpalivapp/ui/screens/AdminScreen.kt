package com.example.corpalivapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.corpalivapp.data.Donacion
import com.example.corpalivapp.ui.theme.VerdeCorpaliv
import com.example.corpalivapp.viewmodel.AdminViewModel
import java.text.NumberFormat
import java.util.Locale

@Composable
fun AdminScreen(
    modifier: Modifier = Modifier,
    adminViewModel: AdminViewModel = viewModel(),
    onMenuClick: () -> Unit = {}
) {
    val estado by adminViewModel.uiState.collectAsStateWithLifecycle()

    val verde = VerdeCorpaliv
    val fondo = MaterialTheme.colorScheme.background
    val colorTitulo = MaterialTheme.colorScheme.primary
    val colorTexto = MaterialTheme.colorScheme.onBackground

    val formatoMonto = NumberFormat.getIntegerInstance(Locale.forLanguageTag("es-CL"))

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(fondo)
    ) {
        // ENCABEZADO
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(verde)
                .padding(horizontal = 16.dp, vertical = 14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "CORPALIV",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Panel de Administración",
                    color = Color.White.copy(alpha = 0.9f),
                    fontSize = 13.sp
                )
            }

            TextButton(onClick = onMenuClick) {
                Text(
                    text = "☰",
                    color = Color.White,
                    fontSize = 26.sp
                )
            }
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // TÍTULO Y DESCRIPCIÓN
            item {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        text = "Gestión de Donaciones",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = colorTitulo
                    )
                    Text(
                        text = "Auditoría y control de estado de aportes de usuarios",
                        style = MaterialTheme.typography.bodyMedium,
                        color = colorTexto
                    )
                }
            }

            // TARJETAS DE MÉTRICAS / ESTADÍSTICAS
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    MetricaCard(
                        titulo = "Recaudado",
                        valor = "$" + formatoMonto.format(estado.totalRecaudado),
                        subtitulo = "Aprobado",
                        color = verde,
                        modifier = Modifier.weight(1f)
                    )
                    MetricaCard(
                        titulo = "Aportes",
                        valor = estado.todasLasDonaciones.size.toString(),
                        subtitulo = "${estado.cantidadPendientes} pend.",
                        color = colorTitulo,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            // FILTROS POR ESTADO
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    val filtros = listOf(
                        "TODAS" to "Todas (${estado.todasLasDonaciones.size})",
                        "PENDIENTE" to "Pendientes (${estado.cantidadPendientes})",
                        "APROBADA" to "Aprobadas (${estado.cantidadAprobadas})",
                        "RECHAZADA" to "Rechazadas (${estado.cantidadRechazadas})"
                    )

                    filtros.forEach { (clave, etiqueta) ->
                        val seleccionado = estado.filtroEstado.equals(clave, ignoreCase = true)
                        FilterChip(
                            selected = seleccionado,
                            onClick = { adminViewModel.cambiarFiltro(clave) },
                            label = { Text(etiqueta) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = verde,
                                selectedLabelColor = Color.White
                            )
                        )
                    }
                }
            }

            // MENSAJE TEMPORAL DE ÉXITO O ERROR
            if (estado.mensaje != null) {
                item {
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer
                        ),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = estado.mensaje.orEmpty(),
                            modifier = Modifier.padding(12.dp),
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }

            if (estado.error != null) {
                item {
                    Text(
                        text = estado.error.orEmpty(),
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            // ESTADOS DE CARGA / LISTA VACÍA
            when {
                estado.cargando -> {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(32.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(color = verde)
                        }
                    }
                }

                estado.donacionesFiltradas.isEmpty() -> {
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surfaceVariant
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(32.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Text(
                                    text = "No hay donaciones en esta sección",
                                    fontWeight = FontWeight.Bold,
                                    color = colorTitulo
                                )
                                Text(
                                    text = "Filtro activo: ${estado.filtroEstado}",
                                    color = colorTexto,
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                        }
                    }
                }

                else -> {
                    items(
                        items = estado.donacionesFiltradas,
                        key = { it.id }
                    ) { donacion ->
                        DonacionAdminCard(
                            donacion = donacion,
                            formatoMonto = formatoMonto,
                            onCambiarEstado = { nuevoEstado ->
                                adminViewModel.cambiarEstado(donacion.id, nuevoEstado)
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun MetricaCard(
    titulo: String,
    valor: String,
    subtitulo: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = titulo,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = valor,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = color
            )
            Text(
                text = subtitulo,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun DonacionAdminCard(
    donacion: Donacion,
    formatoMonto: NumberFormat,
    onCambiarEstado: (String) -> Unit
) {
    val verde = VerdeCorpaliv
    val estadoActual = donacion.estado.uppercase()

    val (colorBadge, textoBadge) = when (estadoActual) {
        "APROBADA" -> Pair(verde, "APROBADA")
        "RECHAZADA" -> Pair(MaterialTheme.colorScheme.error, "RECHAZADA")
        else -> Pair(Color(0xFFE67E22), "PENDIENTE")
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // CABECERA: ID Y BADGE DE ESTADO
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Aporte #${donacion.id}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = colorBadge
                ) {
                    Text(
                        text = textoBadge,
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }
            }

            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)

            // DATOS DEL DONANTE Y APORTE
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = donacion.nombre,
                        fontWeight = FontWeight.SemiBold,
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Text(
                        text = donacion.correo,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Text(
                    text = "$" + formatoMonto.format(donacion.monto),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = verde
                )
            }

            // DETALLES ADICIONALES
            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(
                    text = "Fecha aporte: ${donacion.fechaAporte}",
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = "Ubicación: ${donacion.comuna ?: "Sin comuna"}, ${donacion.region ?: "RM"}",
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = if (donacion.fotoComprobante != null) {
                        "Comprobante: Adjuntado"
                    } else {
                        "Comprobante: No registrado"
                    },
                    style = MaterialTheme.typography.bodySmall,
                    color = if (donacion.fotoComprobante != null) verde else MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)

            // ACCIONES DE GESTIÓN DE ESTADO (APROBAR / RECHAZAR / PENDIENTE)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (estadoActual != "APROBADA") {
                    Button(
                        onClick = { onCambiarEstado("APROBADA") },
                        colors = ButtonDefaults.buttonColors(containerColor = verde),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Aprobar", fontSize = 13.sp, color = Color.White)
                    }
                }

                if (estadoActual != "RECHAZADA") {
                    OutlinedButton(
                        onClick = { onCambiarEstado("RECHAZADA") },
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = MaterialTheme.colorScheme.error
                        ),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Rechazar", fontSize = 13.sp)
                    }
                }

                if (estadoActual != "PENDIENTE") {
                    TextButton(
                        onClick = { onCambiarEstado("PENDIENTE") }
                    ) {
                        Text("Reabrir", fontSize = 12.sp)
                    }
                }
            }
        }
    }
}
