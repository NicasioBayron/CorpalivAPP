package com.example.corpalivapp.network.dto

import com.google.gson.annotations.SerializedName

data class ContactoRequest(
    @SerializedName("usuario_id")        val usuarioId: Long?,
    val nombre: String,
    val correo: String,
    @SerializedName("tipo_colaboracion") val tipoColaboracion: String,
    val mensaje: String,
    @SerializedName("fecha_registro")    val fechaRegistro: Long
)

data class ContactoResponse(
    val id: Long?,
    val estado: String?,
    val status: String?,
    val message: String?,
    val contacto: ContactoItemResponse?
)

data class ContactoItemResponse(
    val id: Long?,
    @SerializedName("usuario_id")        val usuarioId: Long?,
    val nombre: String?,
    val correo: String?,
    @SerializedName("tipo_colaboracion") val tipoColaboracion: String?,
    val mensaje: String?,
    val estado: String?,
    @SerializedName("respuesta_admin")   val respuestaAdmin: String?,
    @SerializedName("fecha_registro")    val fechaRegistro: Long?
)
