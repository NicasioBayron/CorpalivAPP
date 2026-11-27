package com.example.corpalivapp

import com.example.corpalivapp.data.CatalogoComunas
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class CatalogoComunasTest {

    @Test
    fun busquedaVacia_noEntregaSugerencias() {
        assertTrue(CatalogoComunas.filtrar("   ").isEmpty())
    }

    @Test
    fun encuentraComuna_ignorandoMayusculasYAcentos() {
        assertEquals(listOf("Ñuñoa"), CatalogoComunas.filtrar("nunoa"))
        assertEquals(listOf("Maipú"), CatalogoComunas.filtrar("MAIPU"))
    }

    @Test
    fun encuentraCoincidenciasParciales() {
        assertEquals(listOf("Estación Central"), CatalogoComunas.filtrar("central"))
    }

    @Test
    fun textoSinCoincidencias_retornaListaVacia() {
        assertTrue(CatalogoComunas.filtrar("Comuna inexistente").isEmpty())
    }

    @Test
    fun listadoDeComunas_estaOrdenado() {
        assertEquals(CatalogoComunas.comunas.sorted(), CatalogoComunas.comunas)
    }
}
