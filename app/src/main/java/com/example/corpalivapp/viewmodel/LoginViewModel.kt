package com.example.corpalivapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.corpalivapp.data.DatabaseProvider
import com.example.corpalivapp.data.PasswordUtils
import com.example.corpalivapp.data.Usuario
import com.example.corpalivapp.network.repository.UsuarioNetworkRepository
import com.example.corpalivapp.viewmodel.validation.LoginValidator
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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
                // 1. Intentar login en el backend mediante Retrofit (network-first)
                val networkResult = withContext(Dispatchers.IO) {
                    usuarioNetworkRepo.login(correoLimpio, estado.clave)
                }

                if (networkResult.isSuccess) {
                    val loginResp = networkResult.getOrNull()!!
                    val esCuentaAdmin = correoLimpio.contains("@admin", ignoreCase = true) ||
                        loginResp.rol?.equals("ADMIN", ignoreCase = true) == true
                    jwtToken = loginResp.token

                    // Sincronizar usuario con Room para compatibilidad con perfil y donaciones locales
                    val idFinal = withContext(Dispatchers.IO) {
                        val dao = DatabaseProvider
                            .obtenerDatabase(getApplication())
                            .usuarioDao()
                        val existente = dao.buscarPorCorreo(correoLimpio)
                        if (existente != null) {
                            existente.id
                        } else {
                            val salt = PasswordUtils.generarSalt()
                            val hash = PasswordUtils.generarHash(estado.clave, salt)
                            val nuevo = Usuario(
                                id = loginResp.id?.toInt() ?: 0,
                                nombre = loginResp.nombre ?: correoLimpio.substringBefore("@"),
                                correo = correoLimpio,
                                claveHash = hash,
                                claveSalt = salt,
                                fotoPerfil = null
                            )
                            val nuevoId = dao.registrarUsuario(nuevo).toInt()
                            if (nuevoId > 0) nuevoId else (loginResp.id?.toInt() ?: 1)
                        }
                    }

                    _uiState.update {
                        it.copy(
                            cargando = false,
                            clave = "",
                            usuarioId = idFinal,
                            autenticado = true,
                            esAdmin = esCuentaAdmin,
                            mensaje = "Inicio de sesión correcto"
                        )
                    }
                    return@launch
                } else {
                    val errorNet = networkResult.exceptionOrNull()?.message.orEmpty()
                    // Si el backend rechazó explícitamente las credenciales
                    if (errorNet.contains("Credenciales incorrectas", ignoreCase = true)) {
                        // Comprobar si existe localmente en Room (cuenta creada offline)
                        val usuarioLocal = withContext(Dispatchers.IO) {
                            val dao = DatabaseProvider.obtenerDatabase(getApplication()).usuarioDao()
                            val u = dao.buscarPorCorreo(correoLimpio)
                            if (u != null && PasswordUtils.verificarClave(estado.clave, u.claveHash, u.claveSalt)) {
                                u
                            } else null
                        }

                        if (usuarioLocal != null) {
                            val esCuentaAdmin = usuarioLocal.correo.contains("@admin", ignoreCase = true)
                            _uiState.update {
                                it.copy(
                                    cargando = false,
                                    clave = "",
                                    usuarioId = usuarioLocal.id,
                                    autenticado = true,
                                    esAdmin = esCuentaAdmin,
                                    mensaje = "Inicio de sesión correcto (local)"
                                )
                            }
                            return@launch
                        }

                        _uiState.update {
                            it.copy(
                                cargando = false,
                                clave = "",
                                usuarioId = null,
                                autenticado = false,
                                mensaje = "Correo o contraseña incorrectos"
                            )
                        }
                        return@launch
                    }

                    // No presentar un error de red como si fuera un problema de credenciales.
                    // En un teléfono físico, 10.0.2.2 no apunta al computador: se debe usar
                    // la IP Wi-Fi del equipo que ejecuta los microservicios.
                    if (esErrorDeConexion(errorNet)) {
                        _uiState.update {
                            it.copy(
                                cargando = false,
                                clave = "",
                                usuarioId = null,
                                autenticado = false,
                                mensaje = "No se pudo conectar al backend. En un teléfono usa la IP Wi-Fi del PC; 10.0.2.2 es solo para el emulador."
                            )
                        }
                        return@launch
                    }
                }

                // 2. Fallback a Room si el backend no estuvo disponible (ej: sin conexión)
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

    private fun esErrorDeConexion(mensaje: String): Boolean {
        val texto = mensaje.lowercase(Locale.ROOT)
        return listOf(
            "failed to connect",
            "connection refused",
            "connect timed out",
            "timeout",
            "unknownhost",
            "unable to resolve host",
            "network is unreachable",
            "no route to host",
            "cleartxt",
            "cleartext"
        ).any(texto::contains)
    }
}
