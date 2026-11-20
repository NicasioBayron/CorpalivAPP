package com.example.corpalivapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.corpalivapp.data.Donacion
import com.example.corpalivapp.data.DonacionRepository
import com.example.corpalivapp.network.repository.DonacionNetworkRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

data class HistorialAportesUiState(
    val cargando: Boolean = true,
    val aportes: List<Donacion> = emptyList(),
    val error: String? = null
)

class HistorialAportesViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val repositorio = DonacionRepository(application)
    private val donacionNetworkRepo = DonacionNetworkRepository()

    private val _uiState = MutableStateFlow(
        HistorialAportesUiState()
    )
    val uiState = _uiState.asStateFlow()

    private var observacion: Job? = null

    fun cargar(usuarioId: Int) {
        observacion?.cancel()
        _uiState.value = HistorialAportesUiState()

        observacion = viewModelScope.launch {
            try {
                // Sincronizar aportes desde el backend vía Retrofit
                runCatching {
                    val remoteResult = withContext(Dispatchers.IO) {
                        donacionNetworkRepo.listarPorUsuario(usuarioId.toLong())
                    }
                    if (remoteResult.isSuccess) {
                        val donacionesRemotas = remoteResult.getOrNull().orEmpty()
                        withContext(Dispatchers.IO) {
                            for (dr in donacionesRemotas) {
                                val id = dr.id?.toInt() ?: continue
                                if (repositorio.obtenerPorId(id) == null) {
                                    repositorio.registrarOActualizar(
                                        Donacion(
                                            id = id,
                                            usuarioId = usuarioId,
                                            nombre = dr.nombreDonante ?: "",
                                            correo = dr.correoDonante ?: "",
                                            monto = dr.monto ?: 0,
                                            fechaAporte = dr.fechaAporte ?: "",
                                            aceptaCondiciones = true,
                                            recibirSeguimiento = false,
                                            fotoComprobante = dr.fotoComprobante,
                                            fechaRegistro = dr.fechaRegistro ?: System.currentTimeMillis(),
                                            region = dr.region,
                                            comuna = dr.comuna,
                                            estado = dr.estado ?: "PENDIENTE"
                                        )
                                    )
                                }
                            }
                        }
                    }
                }

                repositorio.observarPorUsuario(usuarioId).collect { aportes ->
                    _uiState.value = HistorialAportesUiState(
                        cargando = false,
                        aportes = aportes
                    )
                }
            } catch (error: CancellationException) {
                throw error
            } catch (error: Exception) {
                _uiState.value = HistorialAportesUiState(
                    cargando = false,
                    error = "No se pudieron cargar tus aportes."
                )
            }
        }
    }
}
