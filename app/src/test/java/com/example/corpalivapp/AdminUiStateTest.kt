package com.example.corpalivapp

import com.example.corpalivapp.data.Donacion
import com.example.corpalivapp.viewmodel.AdminUiState
import org.junit.Assert.assertEquals
import org.junit.Test

class AdminUiStateTest {

    @Test
    fun metricas_soloConsideranDonacionesConElEstadoCorrecto() {
        val estado = AdminUiState(
            todasLasDonaciones = listOf(
                donacion(id = 1, monto = 10_000, estado = "APROBADA"),
                donacion(id = 2, monto = 5_000, estado = "pendiente"),
                donacion(id = 3, monto = 2_000, estado = "rechazada"),
                donacion(id = 4, monto = 8_000, estado = "aprobada")
            )
        )

        assertEquals(18_000L, estado.totalRecaudado)
        assertEquals(25_000L, estado.totalRecaudadoGeneral)
        assertEquals(1, estado.cantidadPendientes)
        assertEquals(2, estado.cantidadAprobadas)
        assertEquals(1, estado.cantidadRechazadas)
    }

    @Test
    fun metricas_sinDonacionesSonCero() {
        val estado = AdminUiState()

        assertEquals(0L, estado.totalRecaudado)
        assertEquals(0L, estado.totalRecaudadoGeneral)
        assertEquals(0, estado.cantidadPendientes)
        assertEquals(0, estado.cantidadAprobadas)
        assertEquals(0, estado.cantidadRechazadas)
    }

    private fun donacion(id: Int, monto: Int, estado: String) = Donacion(
        id = id,
        usuarioId = 1,
        nombre = "Persona de prueba",
        correo = "persona@correo.cl",
        monto = monto,
        fechaAporte = "2026-11-27",
        aceptaCondiciones = true,
        recibirSeguimiento = false,
        fechaRegistro = 0L,
        estado = estado
    )
}
