package com.example.corpalivapp

import com.example.corpalivapp.viewmodel.validation.DonacionValidator
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class DonacionValidatorComunaTest {

    @Test
    fun nombre_debeTenerAlMenosTresCaracteresReales() {
        assertEquals(
            "Ingresa un nombre de al menos 3 caracteres.",
            DonacionValidator.validarNombre("  ab ")
        )
        assertNull(DonacionValidator.validarNombre(" Ana "))
    }

    @Test
    fun comunaDebePertenecerAlCatalogo() {
        assertEquals(
            "Selecciona una comuna de las sugerencias.",
            DonacionValidator.validarComuna("Valparaíso")
        )
        assertNull(DonacionValidator.validarComuna("Santiago"))
    }

    @Test
    fun montoConEspacios_esAceptadoSiEsEnteroPositivo() {
        assertNull(DonacionValidator.validarMonto(" 15000 "))
    }
}
