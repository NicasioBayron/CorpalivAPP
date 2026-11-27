package com.example.corpalivapp

import com.example.corpalivapp.network.RetrofitConfig
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Test

class RetrofitConfigTest {

    @After
    fun restaurarHostPorDefecto() {
        RetrofitConfig.configurarHost(RetrofitConfig.DEFAULT_HOST)
    }

    @Test
    fun getBaseUrl_usaHostYPuertoIndicados() {
        RetrofitConfig.configurarHost("10.0.2.2")

        assertEquals(
            "http://10.0.2.2:8081/",
            RetrofitConfig.getBaseUrl(RetrofitConfig.PUERTO_USUARIO)
        )
    }

    @Test
    fun configurarHost_eliminaProtocoloEspaciosYBarraFinal() {
        RetrofitConfig.configurarHost("  https://servidor.local/  ")

        assertEquals(
            "http://servidor.local:9090/",
            RetrofitConfig.getBaseUrl(RetrofitConfig.PUERTO_API_GATEWAY)
        )
    }

    @Test
    fun configurarHost_vacioConservaLaConfiguracionAnterior() {
        RetrofitConfig.configurarHost("servidor-inicial")
        RetrofitConfig.configurarHost("   ")

        assertEquals(
            "http://servidor-inicial:8082/",
            RetrofitConfig.getBaseUrl(RetrofitConfig.PUERTO_DONACIONES)
        )
    }
}
