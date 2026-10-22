package com.example.corpalivapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.corpalivapp.data.FormulariosRepository
import com.example.corpalivapp.data.SolicitudContacto
import com.example.corpalivapp.viewmodel.validation.ContactoValidator
import java.util.Locale
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ContactoUiState(
    val nombre: String = "",
    val correo: String = "",
    val mensaje: String = "",
    val tipoColaboracion: String = "Donaciones",
    val errorNombre: String? = null,
    val errorCorreo: String? = null,
    val errorMensaje: String? = null,
    val errorTipo: String? = null,
    val guardando: Boolean = false,
    val resultado: String? = null,
    val esError: Boolean = false
)

class ContactoViewModel(
    application: Application,
    private val savedState: SavedStateHandle
) : AndroidViewModel(application) {
    private val repositorio = FormulariosRepository(application)
    private val _uiState = MutableStateFlow(ContactoUiState(
        nombre = savedState["nombre"] ?: "",
        correo = savedState["correo"] ?: "",
        mensaje = savedState["mensaje"] ?: "",
        tipoColaboracion = savedState["tipo"] ?: "Donaciones"
    ))
    val uiState = _uiState.asStateFlow()

    fun actualizarNombre(valor: String) {
        if (_uiState.value.guardando) return
        savedState["nombre"] = valor
        _uiState.update { it.copy(nombre = valor, errorNombre = null, resultado = null) }
    }

    fun actualizarCorreo(valor: String) {
        if (_uiState.value.guardando) return
        savedState["correo"] = valor
        _uiState.update { it.copy(correo = valor, errorCorreo = null, resultado = null) }
    }

    fun actualizarMensaje(valor: String) {
        if (_uiState.value.guardando) return
        savedState["mensaje"] = valor
        _uiState.update { it.copy(mensaje = valor, errorMensaje = null, resultado = null) }
    }

    fun actualizarTipo(valor: String) {
        if (_uiState.value.guardando) return
        savedState["tipo"] = valor
        _uiState.update { it.copy(tipoColaboracion = valor, errorTipo = null, resultado = null) }
    }

    fun enviar(usuarioId: Int?) {
        if (_uiState.value.guardando) return
        val datos = _uiState.value
        val errorNombre = ContactoValidator.validarNombre(datos.nombre)
        val errorCorreo = ContactoValidator.validarCorreo(datos.correo)
        val errorMensaje = ContactoValidator.validarMensaje(datos.mensaje)
        val errorTipo = ContactoValidator.validarTipo(datos.tipoColaboracion)
        _uiState.update { it.copy(errorNombre = errorNombre, errorCorreo = errorCorreo,
            errorMensaje = errorMensaje, errorTipo = errorTipo, resultado = null, esError = false) }
        if (listOf(errorNombre, errorCorreo, errorMensaje, errorTipo).any { it != null }) return

        _uiState.update { it.copy(guardando = true) }
        viewModelScope.launch {
            try {
                repositorio.enviarContacto(SolicitudContacto(
                    nombre = datos.nombre.trim(), correo = datos.correo.trim().lowercase(Locale.ROOT),
                    mensaje = datos.mensaje.trim(), tipoColaboracion = datos.tipoColaboracion,
                    usuarioId = usuarioId
                ))
                savedState["nombre"] = ""
                savedState["correo"] = ""
                savedState["mensaje"] = ""
                savedState["tipo"] = "Donaciones"
                _uiState.value = ContactoUiState(guardando = true,
                    resultado = "Solicitud guardada correctamente en este dispositivo.")
            } catch (error: CancellationException) {
                throw error
            } catch (error: Exception) {
                _uiState.update { it.copy(resultado = "No se pudo guardar la solicitud. Intenta nuevamente.",
                    esError = true) }
            } finally {
                _uiState.update { it.copy(guardando = false) }
            }
        }
    }
}
