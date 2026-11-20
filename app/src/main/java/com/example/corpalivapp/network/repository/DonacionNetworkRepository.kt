package com.example.corpalivapp.network.repository

import com.example.corpalivapp.network.RetrofitClient
import com.example.corpalivapp.network.dto.DonacionRequest
import com.example.corpalivapp.network.dto.DonacionResponse
import com.example.corpalivapp.network.dto.EstadoRequest

/**
 * Repositorio de red para Donaciones.
 * Conecta con el microservicio Donaciones-service (puerto 8082) a través de Retrofit.
 */
class DonacionNetworkRepository {

    private val api get() = RetrofitClient.donacionesApi

    suspend fun registrar(
        usuarioId: Long,
        nombre: String,
        correo: String,
        monto: Int,
        fechaAporte: String,
        aceptaCondiciones: Boolean,
        recibirSeguimiento: Boolean,
        fotoComprobante: String?,
        region: String?,
        comuna: String?
    ): Result<DonacionResponse> = runCatching {
        val body = DonacionRequest(
            usuarioId = usuarioId,
            nombreDonante = nombre,
            correoDonante = correo,
            monto = monto,
            fechaAporte = fechaAporte,
            aceptaCondiciones = aceptaCondiciones,
            recibirSeguimiento = recibirSeguimiento,
            fotoComprobante = fotoComprobante,
            region = region,
            comuna = comuna,
            fechaRegistro = System.currentTimeMillis()
        )
        val response = api.registrar(body)
        if (response.isSuccessful && response.body() != null) {
            val resBody = response.body()!!
            if (resBody.status.equals("error", ignoreCase = true)) {
                throw Exception(resBody.message ?: "Error al registrar donación")
            }
            resBody
        } else {
            throw Exception(response.errorBody()?.string() ?: "Error al registrar donación (${response.code()})")
        }
    }

    suspend fun listarPorUsuario(usuarioId: Long): Result<List<DonacionResponse>> =
        runCatching {
            val response = api.listarPorUsuario(usuarioId)
            if (response.isSuccessful && response.body() != null) {
                response.body()!!
            } else {
                throw Exception("Error al obtener donaciones: ${response.code()}")
            }
        }

    suspend fun listarTodas(): Result<List<DonacionResponse>> = runCatching {
        val response = api.listarTodas()
        if (response.isSuccessful && response.body() != null) {
            response.body()!!
        } else {
            throw Exception("Error al obtener todas las donaciones: ${response.code()}")
        }
    }

    suspend fun cambiarEstado(id: Long, nuevoEstado: String): Result<DonacionResponse> =
        runCatching {
            val response = api.cambiarEstado(id, EstadoRequest(estado = nuevoEstado))
            if (response.isSuccessful && response.body() != null) {
                val resBody = response.body()!!
                if (resBody.status.equals("error", ignoreCase = true)) {
                    throw Exception(resBody.message ?: "Error al cambiar estado")
                }
                resBody
            } else {
                throw Exception(response.errorBody()?.string() ?: "Error al cambiar estado (${response.code()})")
            }
        }
}
