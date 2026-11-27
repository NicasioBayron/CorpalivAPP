package com.example.corpalivapp.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Archivo de configuración centralizado para Retrofit y la capa de red HTTP.
 *
 * Microservicios soportados:
 * - Usuario-service    → Puerto 8081
 * - Donaciones-service → Puerto 8082
 * - Contacto-service   → Puerto 8083
 *
 * Hosts soportados:
 * - Emulador Android: "10.0.2.2" (apunta al localhost de la PC).
 * - Dispositivo físico: IP local de la PC en Wi-Fi (ej: "192.168.100.164").
 */
object RetrofitConfig {

    const val HOST_EMULADOR = "10.0.2.2"
    const val HOST_PC_WIFI = "192.168.100.164"
    const val DEFAULT_HOST = HOST_EMULADOR

    @Volatile
    var host: String = DEFAULT_HOST
        private set

    // Puertos directos de los tres microservicios backend
    const val PUERTO_USUARIO = 8081
    const val PUERTO_DONACIONES = 8082
    const val PUERTO_CONTACTO = 8083

    // Tiempos de espera
    const val TIMEOUT_CONNECT = 5L
    const val TIMEOUT_READ = 15L
    const val TIMEOUT_WRITE = 15L

    fun configurarHost(nuevoHost: String) {
        val hostLimpio = nuevoHost.trim()
            .removePrefix("http://")
            .removePrefix("https://")
            .trimEnd('/')
        if (hostLimpio.isNotBlank()) {
            host = hostLimpio
            RetrofitClient.reiniciarClientes()
        }
    }

    fun getBaseUrl(puerto: Int): String {
        return "http://$host:$puerto/"
    }

    fun crearOkHttpClient(connectTimeoutSec: Long = TIMEOUT_CONNECT): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .connectTimeout(connectTimeoutSec, TimeUnit.SECONDS)
            .readTimeout(TIMEOUT_READ, TimeUnit.SECONDS)
            .writeTimeout(TIMEOUT_WRITE, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
            .build()
    }

    fun crearRetrofit(
        puerto: Int,
        hostTarget: String = host,
        okHttpClient: OkHttpClient = crearOkHttpClient()
    ): Retrofit {
        val baseUrl = "http://$hostTarget:$puerto/"
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}
