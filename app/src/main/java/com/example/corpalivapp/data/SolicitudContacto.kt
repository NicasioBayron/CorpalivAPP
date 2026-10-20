
package com.example.corpalivapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "solicitudes_contacto")
data class SolicitudContacto(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val nombre: String,
    val correo: String,
    val tipoColaboracion: String,
    val mensaje: String,
    val fechaRegistro: Long = System.currentTimeMillis(),
    val usuarioId: Int? = null
)
