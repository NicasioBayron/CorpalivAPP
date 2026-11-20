package com.example.corpalivapp.network.repository

import com.example.corpalivapp.network.RetrofitClient
import com.example.corpalivapp.network.dto.ContactoRequest
import com.example.corpalivapp.network.dto.ContactoResponse
import com.example.corpalivapp.network.dto.ContactoItemResponse

/**
 * Repositorio de red para Contacto.
 * Conecta con el microservicio Contacto-service (puerto 8083) a través de Retrofit.
 */
class ContactoNetworkRepository {

    private val api get() = RetrofitClient.contactoApi

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
            val resBody = response.body()!!
            if (resBody.status.equals("error", ignoreCase = true)) {
                throw Exception(resBody.message ?: "Error al enviar contacto")
            }
            resBody
        } else {
            throw Exception(response.errorBody()?.string() ?: "Error al enviar contacto (${response.code()})")
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
