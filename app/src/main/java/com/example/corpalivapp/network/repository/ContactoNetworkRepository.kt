package com.example.corpalivapp.network.repository

import com.example.corpalivapp.network.RetrofitClient
import com.example.corpalivapp.network.dto.ContactoRequest
import com.example.corpalivapp.network.dto.ContactoResponse
import com.example.corpalivapp.network.dto.ContactoItemResponse

/**
 * Repositorio de red para Contacto.
 * Usa el microservicio Contacto-service (puerto 8083).
 */
class ContactoNetworkRepository {

    private val api = RetrofitClient.contactoApi

    suspend fun enviar(
        usuarioId: Long?,
        nombre: String,
        correo: String,
        tipoColaboracion: String,
        mensaje: String
    ): Result<ContactoResponse> = runCatching {
        val body = ContactoRequest(
            usuarioId = usuarioId,
            nombre = nombre,
            correo = correo,
            tipoColaboracion = tipoColaboracion,
            mensaje = mensaje,
            fechaRegistro = System.currentTimeMillis()
        )
        val response = api.enviar(body)
        if (response.isSuccessful && response.body() != null) {
            response.body()!!
        } else {
            throw Exception(response.errorBody()?.string() ?: "Error al enviar contacto")
        }
    }

    suspend fun listarPorUsuario(usuarioId: Long): Result<List<ContactoItemResponse>> =
        runCatching {
            val response = api.listarPorUsuario(usuarioId)
            if (response.isSuccessful && response.body() != null) {
                response.body()!!
            } else {
                throw Exception("Error al obtener solicitudes: ${response.code()}")
            }
        }
}
