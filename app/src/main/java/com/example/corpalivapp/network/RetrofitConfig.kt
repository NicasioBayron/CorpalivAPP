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

=======
 * Configuración de Host:
 * - Host del backend en la red local: "192.168.1.87".
 */
object RetrofitConfig {

    /**
     * Host por defecto del backend en la red local.
     */
    const val DEFAULT_HOST = "192.168.1.87"
>>>>>>> 05a235dc08a5a965af5a627046ed9ba86a1ba459

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

<<<<<<< HEAD
=======


    /**
     * Genera la URL base completa para un puerto dado.
     */
>>>>>>> 05a235dc08a5a965af5a627046ed9ba86a1ba459
    fun getBaseUrl(puerto: Int): String {
        return "http://$host:$puerto/"
    }

<<<<<<< HEAD
    fun crearOkHttpClient(connectTimeoutSec: Long = TIMEOUT_CONNECT): OkHttpClient {
=======


    /**
     * Crea y configura la instancia de [OkHttpClient] con interceptores de logging y timeouts.
     */
    fun crearOkHttpClient(): OkHttpClient {
>>>>>>> 05a235dc08a5a965af5a627046ed9ba86a1ba459
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
