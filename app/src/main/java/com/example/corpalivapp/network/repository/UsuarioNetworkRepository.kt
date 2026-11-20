package com.example.corpalivapp.network.repository

import com.example.corpalivapp.network.RetrofitClient
import com.example.corpalivapp.network.dto.LoginRequest
import com.example.corpalivapp.network.dto.LoginResponse
import com.example.corpalivapp.network.dto.RegisterRequest
import com.example.corpalivapp.network.dto.RegisterResponse

/**
 * Repositorio de red para Usuarios.
 * Conecta con el microservicio Usuario-service (puerto 8081) a través de Retrofit.
 */
class UsuarioNetworkRepository {

    private val api get() = RetrofitClient.usuarioApi

    /**
     * Envía credenciales al backend mediante Retrofit.
     * Retorna éxito solo si las credenciales son válidas y el servidor devuelve un token.
     */
    suspend fun login(email: String, password: String): Result<LoginResponse> = runCatching {
        val response = api.login(LoginRequest(email = email, password = password))
        if (response.isSuccessful && response.body() != null) {
            val body = response.body()!!
            if (body.status.equals("error", ignoreCase = true) || body.token.isNullOrBlank()) {
                throw Exception(body.message ?: "Credenciales incorrectas")
            }
            body
        } else {
            throw Exception(response.errorBody()?.string() ?: "Error de autenticación (${response.code()})")
        }
    }

    /**
     * Registra un nuevo usuario en el backend.
     * Lanza excepción si el backend responde con error (por ejemplo, si el correo ya existe).
     */
    suspend fun register(nombre: String, email: String, password: String): Result<RegisterResponse> =
        runCatching {
            val response = api.register(RegisterRequest(nombre = nombre, email = email, password = password))
            if (response.isSuccessful && response.body() != null) {
                val body = response.body()!!
                if (body.status.equals("error", ignoreCase = true)) {
                    throw Exception(body.message)
                }
                body
            } else {
                throw Exception(response.errorBody()?.string() ?: "Error al registrar (${response.code()})")
            }
        }

    /**
     * Consulta los datos de un usuario por su ID en el backend.
     */
    suspend fun obtenerPorId(id: Long): Result<LoginResponse> = runCatching {
        val response = api.obtenerPorId(id)
        if (response.isSuccessful && response.body() != null) {
            response.body()!!
        } else {
            throw Exception("Error al obtener usuario: ${response.code()}")
        }
    }
}
