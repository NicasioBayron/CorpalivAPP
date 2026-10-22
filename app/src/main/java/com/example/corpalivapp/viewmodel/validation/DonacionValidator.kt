package com.example.corpalivapp.viewmodel.validation

import com.example.corpalivapp.data.CatalogoComunas
import java.time.LocalDate

object DonacionValidator {

    fun validarNombre(nombre: String): String? {
        return if (nombre.trim().length < 3) {
            "Ingresa un nombre de al menos 3 caracteres."
        } else {
            null
        }
    }

    fun validarCorreo(correo: String): String? {
        return LoginValidator.validarCorreo(correo)
    }

    fun validarMonto(valor: String): String? {
        val monto = valor.trim().toIntOrNull()

        return when {
            monto == null -> "Ingresa un monto entero válido."
            monto <= 0 -> "El monto debe ser mayor que cero."
            else -> null
        }
    }

    fun validarFecha(fecha: LocalDate?): String? {
        return when {
            fecha == null -> "Selecciona una fecha."
            fecha.isBefore(LocalDate.now()) ->
                "Selecciona hoy o una fecha futura."
            else -> null
        }
    }

    fun validarCondiciones(aceptadas: Boolean): String? {
        return if (!aceptadas) {
            "Debes aceptar las condiciones de la demostración."
        } else {
            null
        }
    }

    fun validarComuna(comuna: String): String? {
        return if (comuna !in CatalogoComunas.comunas) {
            "Selecciona una comuna de las sugerencias."
        } else {
            null
        }
    }
}