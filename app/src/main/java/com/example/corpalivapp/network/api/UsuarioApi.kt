package com.example.corpalivapp.network.api

import com.example.corpalivapp.network.dto.*
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface UsuarioApi {
    @POST("usuarios/login")
    suspend fun login(@Body body: LoginRequest): Response<LoginResponse>

    @POST("usuarios/register")
    suspend fun register(@Body body: RegisterRequest): Response<RegisterResponse>

    @GET("usuarios/{id}")
    suspend fun obtenerPorId(@Path("id") id: Long): Response<LoginResponse>
}
