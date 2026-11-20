package com.example.corpalivapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.corpalivapp.data.Donacion
import com.example.corpalivapp.data.DonacionRepository
import com.example.corpalivapp.network.repository.DonacionNetworkRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class AdminUiState(
    val cargando: Boolean = true,
    val todasLasDonaciones: List<Donacion> = emptyList(),
    val donacionesFiltradas: List<Donacion> = emptyList(),
    val filtroEstado: String = "TODAS",
    val mensaje: String? = null,
    val error: String? = null
) {
    val totalRecaudado: Long
        get() = todasLasDonaciones.filter { it.estado.equals("APROBADA", ignoreCase = true) }
            .fold(0L) { acc, d -> acc + d.monto }

    val totalRecaudadoGeneral: Long
        get() = todasLasDonaciones.fold(0L) { acc, d -> acc + d.monto }

    val cantidadPendientes: Int
        get() = todasLasDonaciones.count { it.estado.equals("PENDIENTE", ignoreCase = true) }

    val cantidadAprobadas: Int
        get() = todasLasDonaciones.count { it.estado.equals("APROBADA", ignoreCase = true) }

    val cantidadRechazadas: Int
        get() = todasLasDonaciones.count { it.estado.equals("RECHAZADA", ignoreCase = true) }
}

class AdminViewModel(application: Application) : AndroidViewModel(application) {

    private val repositorio = DonacionRepository(application)
    private val networkRepo = DonacionNetworkRepository()

    private val _uiState = MutableStateFlow(AdminUiState())
    val uiState = _uiState.asStateFlow()

    init {
        observarDonaciones()
    }

    fun observarDonaciones() {
        viewModelScope.launch {
            _uiState.update { it.copy(cargando = true, error = null) }
            repositorio.observarTodas()
                .catch { ex ->
                    _uiState.update {
                        it.copy(
                            cargando = false,
                            error = ex.message ?: "Error al cargar donaciones"
                        )
                    }
                }
                .collect { lista ->
                    _uiState.update { estado ->
                        estado.copy(
                            cargando = false,
                            todasLasDonaciones = lista,
                            donacionesFiltradas = filtrar(lista, estado.filtroEstado)
                        )
                    }
                }
        }
    }

    fun cambiarFiltro(filtro: String) {
        _uiState.update { estado ->
            estado.copy(
                filtroEstado = filtro,
                donacionesFiltradas = filtrar(estado.todasLasDonaciones, filtro)
            )
        }
    }

    fun cambiarEstado(id: Int, nuevoEstado: String) {
        viewModelScope.launch {
            try {
                // 1. Actualizar en Room
                repositorio.actualizarEstado(id, nuevoEstado)

                // 2. Intentar actualizar en el microservicio
                runCatching {
                    networkRepo.cambiarEstado(id.toLong(), nuevoEstado)
                }

                _uiState.update {
                    it.copy(mensaje = "Donación #$id actualizada a $nuevoEstado")
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(error = "No se pudo actualizar el estado: ${e.message}")
                }
            }
        }
    }

    fun limpiarMensaje() {
        _uiState.update { it.copy(mensaje = null, error = null) }
    }

    private fun filtrar(lista: List<Donacion>, filtro: String): List<Donacion> {
        return when (filtro.uppercase()) {
            "PENDIENTE" -> lista.filter { it.estado.equals("PENDIENTE", ignoreCase = true) }
            "APROBADA" -> lista.filter { it.estado.equals("APROBADA", ignoreCase = true) }
            "RECHAZADA" -> lista.filter { it.estado.equals("RECHAZADA", ignoreCase = true) }
            else -> lista
        }
    }
}
