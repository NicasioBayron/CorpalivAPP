package com.example.corpalivapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.corpalivapp.data.Donacion
import com.example.corpalivapp.data.DonacionRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class HistorialAportesUiState(
    val cargando: Boolean = true,
    val aportes: List<Donacion> = emptyList(),
    val error: String? = null
)

class HistorialAportesViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val repositorio = DonacionRepository(application)

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