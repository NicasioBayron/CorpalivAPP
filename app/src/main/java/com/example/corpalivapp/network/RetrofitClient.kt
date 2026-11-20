package com.example.corpalivapp.network

import com.example.corpalivapp.network.api.ContactoApi
import com.example.corpalivapp.network.api.DonacionesApi
import com.example.corpalivapp.network.api.UsuarioApi
import okhttp3.OkHttpClient
import retrofit2.Retrofit

/**
 * Cliente Retrofit singleton para los microservicios.
 * Administra las instancias de las APIs basándose en [RetrofitConfig].
 * Si [RetrofitConfig.usarApiGateway] es true, dirige las peticiones al API Gateway (puerto 9090);
 * si es false, se conecta directamente al puerto de cada microservicio (8081, 8082, 8083).
 */
object RetrofitClient {

    private var okHttpClient: OkHttpClient = RetrofitConfig.crearOkHttpClient()

    @Volatile
    private var _gatewayRetrofit: Retrofit? = null

    @Volatile
    private var _usuarioApi: UsuarioApi? = null

    @Volatile
    private var _donacionesApi: DonacionesApi? = null

    @Volatile
    private var _contactoApi: ContactoApi? = null

    private fun getOrCreateGatewayRetrofit(): Retrofit {
        return _gatewayRetrofit ?: synchronized(this) {
            _gatewayRetrofit ?: RetrofitConfig.crearRetrofit(
                RetrofitConfig.PUERTO_API_GATEWAY,
                okHttpClient
            ).also { _gatewayRetrofit = it }
        }
    }

    val usuarioApi: UsuarioApi
        get() = _usuarioApi ?: synchronized(this) {
            _usuarioApi ?: run {
                val retrofit = if (RetrofitConfig.usarApiGateway) {
                    getOrCreateGatewayRetrofit()
                } else {
                    RetrofitConfig.crearRetrofit(RetrofitConfig.PUERTO_USUARIO, okHttpClient)
                }
                retrofit.create(UsuarioApi::class.java).also { _usuarioApi = it }
            }
        }

    val donacionesApi: DonacionesApi
        get() = _donacionesApi ?: synchronized(this) {
            _donacionesApi ?: run {
                val retrofit = if (RetrofitConfig.usarApiGateway) {
                    getOrCreateGatewayRetrofit()
                } else {
                    RetrofitConfig.crearRetrofit(RetrofitConfig.PUERTO_DONACIONES, okHttpClient)
                }
                retrofit.create(DonacionesApi::class.java).also { _donacionesApi = it }
            }
        }

    val contactoApi: ContactoApi
        get() = _contactoApi ?: synchronized(this) {
            _contactoApi ?: run {
                val retrofit = if (RetrofitConfig.usarApiGateway) {
                    getOrCreateGatewayRetrofit()
                } else {
                    RetrofitConfig.crearRetrofit(RetrofitConfig.PUERTO_CONTACTO, okHttpClient)
                }
                retrofit.create(ContactoApi::class.java).also { _contactoApi = it }
            }
        }

    /**
     * Reinicia las instancias de las APIs cuando cambia la configuración del host o timeouts.
     */
    fun reiniciarClientes() {
        synchronized(this) {
            okHttpClient = RetrofitConfig.crearOkHttpClient()
            _gatewayRetrofit = null
            _usuarioApi = null
            _donacionesApi = null
            _contactoApi = null
        }
    }
}
