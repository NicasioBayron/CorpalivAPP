package com.example.corpalivapp.network

import com.example.corpalivapp.network.api.ContactoApi
import com.example.corpalivapp.network.api.DonacionesApi
import com.example.corpalivapp.network.api.UsuarioApi
import okhttp3.OkHttpClient

/**
 * Cliente Retrofit singleton para los tres microservicios:
 * - UsuarioApi: Puerto 8081
 * - DonacionesApi: Puerto 8082
 * - ContactoApi: Puerto 8083
 */
object RetrofitClient {

    private var okHttpClient: OkHttpClient = RetrofitConfig.crearOkHttpClient()

    @Volatile
    private var _usuarioApi: UsuarioApi? = null

    @Volatile
    private var _donacionesApi: DonacionesApi? = null

    @Volatile
    private var _contactoApi: ContactoApi? = null

    val usuarioApi: UsuarioApi
        get() = _usuarioApi ?: synchronized(this) {
            _usuarioApi ?: RetrofitConfig.crearRetrofit(
                puerto = RetrofitConfig.PUERTO_USUARIO,
                okHttpClient = okHttpClient
            ).create(UsuarioApi::class.java).also { _usuarioApi = it }
        }

    val donacionesApi: DonacionesApi
        get() = _donacionesApi ?: synchronized(this) {
            _donacionesApi ?: RetrofitConfig.crearRetrofit(
                puerto = RetrofitConfig.PUERTO_DONACIONES,
                okHttpClient = okHttpClient
            ).create(DonacionesApi::class.java).also { _donacionesApi = it }
        }

    val contactoApi: ContactoApi
        get() = _contactoApi ?: synchronized(this) {
            _contactoApi ?: RetrofitConfig.crearRetrofit(
                puerto = RetrofitConfig.PUERTO_CONTACTO,
                okHttpClient = okHttpClient
            ).create(ContactoApi::class.java).also { _contactoApi = it }
        }

    /**
     * Reinicia las instancias de las APIs cuando cambia la configuración del host.
     */
    fun reiniciarClientes() {
        synchronized(this) {
            okHttpClient = RetrofitConfig.crearOkHttpClient()
            _usuarioApi = null
            _donacionesApi = null
            _contactoApi = null
        }
    }
}
