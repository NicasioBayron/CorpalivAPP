package com.example.corpalivapp.network

import com.example.corpalivapp.network.api.ContactoApi
import com.example.corpalivapp.network.api.DonacionesApi
import com.example.corpalivapp.network.api.UsuarioApi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Cliente Retrofit singleton para los tres microservicios.
 *
 * Puertos:
 *   - Usuario-service  → 8081
 *   - Donaciones-service → 8082
 *   - Contacto-service → 8083
 *
 * Para emulador de Android Studio se usa 10.0.2.2 (apunta al localhost de tu PC).
 * Para dispositivo físico conectado por USB en la misma red, usa la IP local
 * de tu PC (ej: 192.168.1.100).
 */
object RetrofitClient {

    // ─── Cambia aquí si usas dispositivo físico (IP de tu PC en la red WiFi) ───
    private const val HOST = "10.0.2.2"
    // ────────────────────────────────────────────────────────────────────────────

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private fun buildOkHttpClient(): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(15, TimeUnit.SECONDS)
            .writeTimeout(15, TimeUnit.SECONDS)
            .build()

    private fun buildRetrofit(port: Int): Retrofit =
        Retrofit.Builder()
            .baseUrl("http://$HOST:$port/")
            .client(buildOkHttpClient())
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    val usuarioApi: UsuarioApi by lazy {
        buildRetrofit(8081).create(UsuarioApi::class.java)
    }

    val donacionesApi: DonacionesApi by lazy {
        buildRetrofit(8082).create(DonacionesApi::class.java)
    }

    val contactoApi: ContactoApi by lazy {
        buildRetrofit(8083).create(ContactoApi::class.java)
    }
}
