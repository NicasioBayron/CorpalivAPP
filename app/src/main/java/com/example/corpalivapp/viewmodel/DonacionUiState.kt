package com.example.corpalivapp.viewmodel

import java.time.LocalDate

data class DonacionUiState(
    val nombre: String = "",
    val correo: String = "",
    val monto: String = "",
    val fecha: LocalDate? = null,
    val aceptaCondiciones: Boolean = false,
    val recibirSeguimiento: Boolean = false,

    val errorNombre: String? = null,
    val errorCorreo: String? = null,
    val errorMonto: String? = null,
    val errorFecha: String? = null,
    val errorCondiciones: String? = null,
    val guardando: Boolean = false,
    val fotoComprobante: String? = null,
    val busquedaComuna: String = "",
    val comuna: String = "",
    val errorComuna: String? = null,
    val mensaje: String? = null
)