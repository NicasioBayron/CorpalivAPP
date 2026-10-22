package com.example.corpalivapp.viewmodel.validation

object RegistroValidator {
    fun validarNombre(nombre: String): String? = when {
        nombre.isBlank() -> "Ingresa tu nombre completo."
        nombre.trim().length < 3 -> "El nombre debe tener al menos 3 caracteres."
        else -> null
    }

    fun validarCorreo(correo: String): String? = LoginValidator.validarCorreo(correo)
    fun validarClave(clave: String): String? = LoginValidator.validarClave(clave)
}
