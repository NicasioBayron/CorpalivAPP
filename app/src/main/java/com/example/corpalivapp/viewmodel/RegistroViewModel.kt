package com.example.corpalivapp.viewmodel

import android.app.Application
import android.database.sqlite.SQLiteConstraintException
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.corpalivapp.data.CorreoRegistradoException
import com.example.corpalivapp.data.FormulariosRepository
import com.example.corpalivapp.viewmodel.validation.RegistroValidator
import java.util.Locale
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class RegistroUiState(
    val nombre: String = "",
    val correo: String = "",
    val clave: String = "",
    val errorNombre: String? = null,
    val errorCorreo: String? = null,
    val errorClave: String? = null,
    val registrando: Boolean = false,
    val mensaje: String? = null,
    val esError: Boolean = false
)

class RegistroViewModel(
    application: Application,
    private val savedState: SavedStateHandle
) : AndroidViewModel(application) {
    private val repositorio = FormulariosRepository(application)
    // La contraseña vive solo en memoria: no se serializa en SavedStateHandle.
    private val _uiState = MutableStateFlow(RegistroUiState(
        nombre = savedState["nombre"] ?: "",
        correo = savedState["correo"] ?: ""
    ))
    val uiState = _uiState.asStateFlow()

    fun actualizarNombre(valor: String) {
        if (_uiState.value.registrando) return
        savedState["nombre"] = valor
        _uiState.update { it.copy(nombre = valor, errorNombre = null, mensaje = null) }
    }

    fun actualizarCorreo(valor: String) {
        if (_uiState.value.registrando) return
        savedState["correo"] = valor
        _uiState.update { it.copy(correo = valor, errorCorreo = null, mensaje = null) }
    }

    fun actualizarClave(valor: String) {
        if (_uiState.value.registrando) return
        _uiState.update { it.copy(clave = valor, errorClave = null, mensaje = null) }
    }

    fun limpiar() {
        if (_uiState.value.registrando) return
        savedState["nombre"] = ""
        savedState["correo"] = ""
        _uiState.value = RegistroUiState()
    }

    fun registrar() {
        if (_uiState.value.registrando) return
        val datos = _uiState.value
        val errorNombre = RegistroValidator.validarNombre(datos.nombre)
        val errorCorreo = RegistroValidator.validarCorreo(datos.correo)
        val errorClave = RegistroValidator.validarClave(datos.clave)
        _uiState.update { it.copy(errorNombre = errorNombre, errorCorreo = errorCorreo,
            errorClave = errorClave, mensaje = null, esError = false) }
        if (listOf(errorNombre, errorCorreo, errorClave).any { it != null }) return

        _uiState.update { it.copy(registrando = true) }
        viewModelScope.launch {
            try {
                repositorio.registrar(datos.nombre.trim(),
                    datos.correo.trim().lowercase(Locale.ROOT), datos.clave)
                savedState["nombre"] = ""
                savedState["correo"] = ""
                _uiState.value = RegistroUiState(registrando = true,
                    mensaje = "¡Cuenta registrada correctamente! Ya puedes iniciar sesión.")
            } catch (error: CancellationException) {
                throw error
            } catch (error: Exception) {
                if (error is CorreoRegistradoException || error is SQLiteConstraintException) {
                    _uiState.update { it.copy(errorCorreo = "Este correo ya está registrado.",
                        mensaje = null, esError = true) }
                } else {
                    _uiState.update { it.copy(mensaje = "No se pudo registrar la cuenta. Intenta nuevamente.",
                        esError = true) }
                }
            } finally {
                _uiState.update { it.copy(registrando = false) }
            }
        }
    }
}
