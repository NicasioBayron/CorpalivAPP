package com.example.corpalivapp.network.repository

import com.example.corpalivapp.network.RetrofitClient
import com.example.corpalivapp.network.dto.LoginRequest
import com.example.corpalivapp.network.dto.LoginResponse
import com.example.corpalivapp.network.dto.RegisterRequest
import com.example.corpalivapp.network.dto.RegisterResponse

/**
 * Repositorio de red para Usuarios.
 * Usa el microservicio Usuario-service (puerto 8081).
 */
class UsuarioNetworkRepository {

    private val api = RetrofitClient.usuarioApi

    /**
     * Envía credenciales al backend.
     * Devuelve un [Result] con el [LoginResponse] si el servidor respondió,
     * o un fallo si no hay conexión (el ViewModel manejará el fallback a Room).
     */
    suspend fun login(email: String, password: String): Result<LoginResponse> = runCatching {
        val response = api.login(LoginRequest(email = email, password = password))
        if (response.isSuccessful && response.body() != null) {
            response.body()!!
        } else {
            throw Exception(response.errorBody()?.string() ?: "Error de autenticación")
        }
    }

    suspend fun register(nombre: String, email: String, password: String): Result<RegisterResponse> =
        runCatching {
            val response = api.register(RegisterRequest(nombre = nombre, email = email, password = password))
            if (response.isSuccessful && response.body() != null) {
                response.body()!!
            } else {
                throw Exception(response.errorBody()?.string() ?: "Error al registrar")
            }
        }
}
