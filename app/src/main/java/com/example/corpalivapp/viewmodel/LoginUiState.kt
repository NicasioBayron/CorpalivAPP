package com.example.corpalivapp.viewmodel

data  class LoginUiState(
    val correo: String = "",
    val clave: String = "",
    val errorCorreo: String? = null,
    val errorClave: String? = null,
    val cargando: Boolean = false,
    val mensaje: String? = null,
    val usuarioId: Int? = null,
    val autenticado: Boolean = false
)