package com.example.corpalivapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope

import com.example.corpalivapp.data.DatabaseProvider
import com.example.corpalivapp.data.PasswordUtils
import com.example.corpalivapp.network.repository.UsuarioNetworkRepository
import com.example.corpalivapp.viewmodel.validation.LoginValidator

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

import java.util.Locale

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState = _uiState.asStateFlow()

    private val usuarioNetworkRepo = UsuarioNetworkRepository()

    // JWT token en memoria para usar en peticiones autenticadas
    var jwtToken: String? = null
        private set

    // Actualizar correo
    fun actualizarCorreo(valor: String) {
        if (_uiState.value.cargando) return
        _uiState.update { estado ->
            estado.copy(
                correo = valor,
                errorCorreo = null,
                mensaje = null
            )
        }
    }

    // Actualizar contraseña
    fun actualizarClave(valor: String) {
        if (_uiState.value.cargando) return
        _uiState.update { estado ->
            estado.copy(
                clave = valor,
                errorClave = null,
                mensaje = null
            )
        }
    }

    // Iniciar sesion
    fun validarFormulario() {
        if (_uiState.value.cargando) return

        val estado = _uiState.value

        val correoLimpio = estado.correo
            .trim()
            .lowercase(Locale.ROOT)

        val errorCorreo = LoginValidator.validarCorreo(correoLimpio)

        val errorClave = if (estado.clave.isBlank()) {
            "Ingresa tu contraseña"
        } else {
            null
        }

        // Comprobar campos
        if (errorCorreo != null || errorClave != null) {
            _uiState.update {
                it.copy(
                    errorCorreo = errorCorreo,
                    errorClave = errorClave,
                    mensaje = "Revisa los datos ingresados"
                )
            }
            return
        }

        if (_uiState.value.cargando) return

        _uiState.update {
            it.copy(
                cargando = true,
                mensaje = null,
                autenticado = false,
                usuarioId = null
            )
        }

        viewModelScope.launch {
            try {
                // 1. Intentar login en el backend (network-first)
                val networkResult = withContext(Dispatchers.IO) {
                    usuarioNetworkRepo.login(correoLimpio, estado.clave)
                }

                if (networkResult.isSuccess) {
                    val loginResp = networkResult.getOrNull()!!
                    val esCuentaAdmin = correoLimpio.contains("@admin", ignoreCase = true) ||
                        loginResp.rol?.equals("ADMIN", ignoreCase = true) == true
                    jwtToken = loginResp.token
                    _uiState.update {
                        it.copy(
                            cargando = false,
                            clave = "",
                            usuarioId = loginResp.id?.toInt(),
                            autenticado = true,
                            esAdmin = esCuentaAdmin,
                            mensaje = "Inicio de sesión correcto"
                        )
                    }
                    return@launch
                }

                // 2. Fallback a Room si el backend no está disponible
                val usuarioAutenticado = withContext(Dispatchers.IO) {
                    val dao = DatabaseProvider
                        .obtenerDatabase(getApplication())
                        .usuarioDao()
                    val usuario = dao.buscarPorCorreo(correoLimpio)
                    if (
                        usuario != null &&
                        PasswordUtils.verificarClave(
                            claveIngresada = estado.clave,
                            hashGuardado = usuario.claveHash,
                            saltGuardado = usuario.claveSalt
                        )
                    ) usuario else null
                }

                val esCuentaAdmin = usuarioAutenticado != null &&
                    usuarioAutenticado.correo.contains("@admin", ignoreCase = true)

                _uiState.update {
                    it.copy(
                        cargando = false,
                        clave = "",
                        usuarioId = usuarioAutenticado?.id,
                        autenticado = usuarioAutenticado != null,
                        esAdmin = esCuentaAdmin,
                        mensaje = if (usuarioAutenticado != null) {
                            "Inicio de sesión correcto (sin conexión)"
                        } else {
                            "Correo o contraseña incorrectos"
                        }
                    )
                }

            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        cargando = false,
                        clave = "",
                        autenticado = false,
                        usuarioId = null,
                        mensaje = "No se pudo iniciar sesión"
                    )
                }
            }
        }
    }

    fun cerrarSesion() {
        jwtToken = null
        _uiState.value = LoginUiState()
    }
}