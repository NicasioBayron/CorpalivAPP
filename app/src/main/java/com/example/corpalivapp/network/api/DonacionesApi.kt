package com.example.corpalivapp.network.api

import com.example.corpalivapp.network.dto.*
import retrofit2.Response
import retrofit2.http.*

interface DonacionesApi {
    @POST("donaciones")
    suspend fun registrar(@Body body: DonacionRequest): Response<DonacionResponse>

    @GET("donaciones/usuario/{usuarioId}")
    suspend fun listarPorUsuario(@Path("usuarioId") usuarioId: Long): Response<List<DonacionResponse>>

    @GET("donaciones")
    suspend fun listarTodas(): Response<List<DonacionResponse>>

    @PUT("donaciones/{id}/estado")
    suspend fun cambiarEstado(
        @Path("id") id: Long,
        @Body body: EstadoRequest
    ): Response<DonacionResponse>
}
