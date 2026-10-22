package com.example.corpalivapp

import com.example.corpalivapp.viewmodel.validation.ContactoValidator
import com.example.corpalivapp.viewmodel.validation.DonacionValidator
import com.example.corpalivapp.viewmodel.validation.RegistroValidator
import org.junit.Assert.*
import org.junit.Test

class FormulariosValidatorTest {
    @Test fun nombreVacioOCortoNoPermiteRegistrar() {
        assertNotNull(RegistroValidator.validarNombre("   "))
        assertNotNull(RegistroValidator.validarNombre(" a "))
        assertNull(RegistroValidator.validarNombre("Ana Pérez"))
    }

    @Test fun correoSinDominioNoPasaYCorreoCompletoSi() {
        assertNotNull(RegistroValidator.validarCorreo("persona"))
        assertNotNull(RegistroValidator.validarCorreo("persona@"))
        assertNull(RegistroValidator.validarCorreo(" persona@empresa.cl "))
    }

    @Test fun claveDeRegistroDebeCumplirLasReglasExistentes() {
        assertNotNull(RegistroValidator.validarClave("a"))
        assertNotNull(RegistroValidator.validarClave("Abcdef12"))
        assertNull(RegistroValidator.validarClave("Abcdef12!"))
    }

    @Test fun contactoValidaMensajeYTipoIndependientemente() {
        assertNotNull(ContactoValidator.validarMensaje(" "))
        assertNotNull(ContactoValidator.validarMensaje("Hola"))
        assertNull(ContactoValidator.validarMensaje("Quiero colaborar con ustedes."))
        assertNotNull(ContactoValidator.validarTipo("Tipo inventado"))
        ContactoValidator.tipos.forEach { assertNull(ContactoValidator.validarTipo(it)) }
    }

    @Test fun montoRechazaDecimalesCeroYDesbordamiento() {
        listOf("", "0", "-1", "12.5", "999999999999999").forEach {
            assertNotNull(DonacionValidator.validarMonto(it))
        }
        assertNull(DonacionValidator.validarMonto("15000"))
    }

    @Test fun aporteRequiereFechaYCondiciones() {
        assertNotNull(DonacionValidator.validarFecha(null))
        assertNotNull(DonacionValidator.validarFecha(java.time.LocalDate.now().minusDays(1)))
        assertNull(DonacionValidator.validarFecha(java.time.LocalDate.now()))
        assertNotNull(DonacionValidator.validarCondiciones(false))
        assertNull(DonacionValidator.validarCondiciones(true))
    }
}
