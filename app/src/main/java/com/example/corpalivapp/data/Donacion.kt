package com.example.corpalivapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "donaciones")
data class Donacion(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val usuarioId: Int,
    val nombre: String,
    val correo: String,
    val monto: Int,
    val fechaAporte: String,
    val aceptaCondiciones: Boolean,
    val recibirSeguimiento: Boolean,
    val fechaRegistro: Long,

    val fotoComprobante: String? = null,
    val region: String? = null,
    val comuna: String? = null
)