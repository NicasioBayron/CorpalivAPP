package com.example.corpalivapp

import com.example.corpalivapp.viewmodel.validation.LoginValidator
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class LoginValidatorTest {

    @Test
    fun correoVacio_informaQueEsObligatorio() {
        assertEquals("Ingresa tu correo", LoginValidator.validarCorreo("   "))
    }

    @Test
    fun correoConFormatoInvalido_esRechazado() {
        listOf("ana", "ana@", "@correo.cl", "ana@correo", "ana correo@correo.cl")
            .forEach { correo ->
                assertEquals("Ingresa un correo válido", LoginValidator.validarCorreo(correo))
            }
    }

    @Test
    fun correoValidoConEspacios_esAceptado() {
        assertNull(LoginValidator.validarCorreo("  ana.perez@correo.cl  "))
    }

    @Test
    fun cuentaAdmin_esAceptadaPorLaReglaActual() {
        assertNull(LoginValidator.validarCorreo("administrador@admin"))
    }

    @Test
    fun claveVacia_esRechazada() {
        assertEquals("Ingresa tu contraseña", LoginValidator.validarClave(""))
    }

    @Test
    fun claveDebeTenerOchoCaracteres() {
        assertEquals(
            "La contraseña debe tener al menos 8 caracteres",
            LoginValidator.validarClave("Ab1!abc")
        )
    }

    @Test
    fun claveDebeTenerMayusculaNumeroYSimbolo() {
        assertEquals("Debe contener una letra mayúscula", LoginValidator.validarClave("clave123!"))
        assertEquals("Debe contener al menos un número", LoginValidator.validarClave("ClaveSinNumero!"))
        assertEquals("Debe contener al menos un símbolo", LoginValidator.validarClave("Clave1234"))
    }

    @Test
    fun claveSegura_esAceptada() {
        assertNull(LoginValidator.validarClave("Clave123!"))
    }
}
