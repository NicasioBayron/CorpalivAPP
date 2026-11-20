package com.example.corpalivapp.network.api

import com.example.corpalivapp.network.dto.*
import retrofit2.Response
import retrofit2.http.*

interface ContactoApi {
    @POST("contacto")
    suspend fun enviar(@Body body: ContactoRequest): Response<ContactoResponse>

    @GET("contacto/usuario/{usuarioId}")
    suspend fun listarPorUsuario(@Path("usuarioId") usuarioId: Long): Response<List<ContactoItemResponse>>

    @GET("contacto")
    suspend fun listarTodos(): Response<List<ContactoItemResponse>>
}
