package com.example.corpalivapp.viewmodel.validation

object LoginValidator{
    private val patronCorreo = Regex(
        """^[A-Za-z0-9][A-Za-z0-9._%+-]*@[A-Za-z0-9]+(?:[.-][A-Za-z0-9]+)*\.[A-Za-z]{2,}$"""
    )
    fun validarCorreo(correo: String): String? {
        val correoLimpio = correo.trim()
        return when {
            correoLimpio.isBlank() -> "Ingresa tu correo"
            !patronCorreo.matches(correoLimpio) -> "Ingresa un correo válido"
            else -> null
        }
    }
    fun validarClave(clave: String): String? {
        return when {
            clave.isBlank() ->
                "Ingresa tu contraseña"

            clave.length < 8 ->
                "La contraseña debe tener al menos 8 caracteres"

            !clave.any { it.isUpperCase() } ->
                "Debe contener una letra mayúscula"

            !clave.any { it.isDigit() } ->
                "Debe contener al menos un número"

            clave.all { it.isLetterOrDigit() } ->
                "Debe contener al menos un símbolo"

            else -> null
        }
    }
}