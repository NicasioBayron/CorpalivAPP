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
 * - Usuario-service  → Puerto 8081
 * - Donaciones-service → Puerto 8082
 * - Contacto-service → Puerto 8083
 *
 * Configuración de Host:
 * - Emulador Android Studio: "10.0.2.2" (mapea al localhost de la máquina host).
 * - Dispositivo físico: IP local de la computadora en la red Wi-Fi (ej: "192.168.1.100").
 */
object RetrofitConfig {

    /**
     * Host por defecto para el emulador de Android.
     */
    const val DEFAULT_HOST = "10.0.2.2"

    /**
     * Host actual utilizado para construir las URLs base de Retrofit.
     */
    @Volatile
    var host: String = DEFAULT_HOST
        private set

    // Puerto del API Gateway centralizado
    const val PUERTO_API_GATEWAY = 9090

    // Puertos individuales de los microservicios backend (modo directo / fallback)
    const val PUERTO_USUARIO = 8081
    const val PUERTO_DONACIONES = 8082
    const val PUERTO_CONTACTO = 8083

    /**
     * Si está activo (true), todo el tráfico pasa a través del API Gateway (puerto 9090).
     * Si está desactivado (false), se conecta directamente a cada microservicio en su puerto respectivo.
     */
    @Volatile
    var usarApiGateway: Boolean = true
        private set

    // Tiempos de espera (timeouts) en segundos
    const val TIMEOUT_CONNECT = 10L
    const val TIMEOUT_READ = 15L
    const val TIMEOUT_WRITE = 15L

    /**
     * Permite cambiar la dirección IP o host del servidor backend en tiempo de ejecución.
     */
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

    /**
     * Permite alternar entre usar el API Gateway (puerto 9090) o conectar directo a cada microservicio.
     */
    fun configurarModoApiGateway(activo: Boolean) {
        usarApiGateway = activo
        RetrofitClient.reiniciarClientes()
    }

    /**
     * Genera la URL base completa para un puerto dado.
     */
    fun getBaseUrl(puerto: Int): String {
        return "http://$host:$puerto/"
    }

    /**
     * Genera la URL base para el API Gateway.
     */
    fun getApiGatewayBaseUrl(): String {
        return "http://$host:$PUERTO_API_GATEWAY/"
    }

    /**
     * Crea y configura la instancia de [OkHttpClient] con interceptores de logging y timeouts.
     */
    fun crearOkHttpClient(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .connectTimeout(TIMEOUT_CONNECT, TimeUnit.SECONDS)
            .readTimeout(TIMEOUT_READ, TimeUnit.SECONDS)
            .writeTimeout(TIMEOUT_WRITE, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
            .build()
    }

    /**
     * Construye una instancia de [Retrofit] con GsonConverterFactory y OkHttpClient para un puerto específico.
     */
    fun crearRetrofit(puerto: Int, okHttpClient: OkHttpClient = crearOkHttpClient()): Retrofit {
        return Retrofit.Builder()
            .baseUrl(getBaseUrl(puerto))
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}
