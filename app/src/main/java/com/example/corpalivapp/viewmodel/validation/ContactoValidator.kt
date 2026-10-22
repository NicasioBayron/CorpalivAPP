package com.example.corpalivapp.viewmodel.validation

object ContactoValidator {
    val tipos = listOf("Donaciones", "Voluntariado", "Alianzas con empresas", "Consulta general")

    fun validarNombre(nombre: String): String? = RegistroValidator.validarNombre(nombre)
    fun validarCorreo(correo: String): String? = LoginValidator.validarCorreo(correo)
    fun validarMensaje(mensaje: String): String? = when {
        mensaje.isBlank() -> "Escribe el mensaje de tu solicitud."
        mensaje.trim().length < 5 -> "El mensaje debe tener al menos 5 caracteres."
        else -> null
    }

    fun validarTipo(tipo: String): String? =
        if (tipo in tipos) null else "Selecciona un tipo de colaboración válido."
}
