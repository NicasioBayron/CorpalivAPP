package com.example.corpalivapp.viewmodel

import android.app.Application
import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.corpalivapp.data.PerfilRepository
import com.example.corpalivapp.data.Usuario
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class PerfilUiState(
    val usuario: Usuario? = null,
    val foto: Bitmap? = null,
    val cargando: Boolean = true,
    val guardandoFoto: Boolean = false,
    val mensaje: String? = null,
    val esError: Boolean = false
)

class PerfilViewModel(application: Application) : AndroidViewModel(application) {
    private val repositorio = PerfilRepository(application)
    private val _uiState = MutableStateFlow(PerfilUiState())
    val uiState = _uiState.asStateFlow()
    private var usuarioActual: Int? = null
    private var trabajo: Job? = null

    fun cargar(id: Int) {
        if (usuarioActual == id && trabajo?.isActive == true) return
        if (usuarioActual == id && _uiState.value.usuario != null) return
        trabajo?.cancel()
        usuarioActual = id
        _uiState.value = PerfilUiState()
        trabajo = viewModelScope.launch {
            try {
                val usuario = repositorio.cargarUsuario(id)
                val foto = repositorio.cargarFoto(usuario.fotoPerfil)
                _uiState.value = PerfilUiState(usuario = usuario, foto = foto, cargando = false)
            } catch (error: CancellationException) {
                throw error
            } catch (error: Exception) {
                _uiState.value = PerfilUiState(
                    cargando = false,
                    mensaje = "No se pudo cargar el perfil. Intenta nuevamente.",
                    esError = true
                )
            }
        }
    }

    fun cambiarFoto(id: Int, uri: Uri) {
        if (usuarioActual != id || _uiState.value.usuario == null ||
            _uiState.value.cargando || _uiState.value.guardandoFoto) return
        _uiState.update { it.copy(guardandoFoto = true, mensaje = null, esError = false) }
        trabajo = viewModelScope.launch {
            try {
                val usuario = repositorio.guardarFoto(id, uri)
                val foto = repositorio.cargarFoto(usuario.fotoPerfil)
                _uiState.value = PerfilUiState(
                    usuario = usuario, foto = foto, cargando = false,
                    mensaje = "Foto de perfil actualizada."
                )
            } catch (error: CancellationException) {
                throw error
            } catch (error: Exception) {
                _uiState.update {
                    it.copy(guardandoFoto = false, esError = true,
                        mensaje = "No se pudo guardar la foto. Prueba con otra imagen.")
                }
            }
        }
    }
}