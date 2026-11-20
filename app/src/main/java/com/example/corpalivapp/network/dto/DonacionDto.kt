package com.example.corpalivapp.network.dto

import com.google.gson.annotations.SerializedName

data class DonacionRequest(
    @SerializedName("usuario_id")   val usuarioId: Long,
    @SerializedName("nombre_donante") val nombreDonante: String,
    @SerializedName("correo_donante") val correoDonante: String,
    val monto: Int,
    @SerializedName("fecha_aporte")  val fechaAporte: String,
    @SerializedName("acepta_condiciones") val aceptaCondiciones: Boolean,
    @SerializedName("recibir_seguimiento") val recibirSeguimiento: Boolean,
    @SerializedName("foto_comprobante") val fotoComprobante: String?,
    val region: String?,
    val comuna: String?,
    @SerializedName("fecha_registro") val fechaRegistro: Long
)

data class DonacionResponse(
    val id: Long?,
    @SerializedName("usuario_id")   val usuarioId: Long?,
    @SerializedName("nombre_donante") val nombreDonante: String?,
    @SerializedName("correo_donante") val correoDonante: String?,
    val monto: Int?,
    @SerializedName("fecha_aporte")  val fechaAporte: String?,
    val estado: String?,
    val region: String?,
    val comuna: String?,
    @SerializedName("foto_comprobante") val fotoComprobante: String?,
    @SerializedName("fecha_registro") val fechaRegistro: Long?,
    val status: String?,
    val message: String?,
    val donacion: DonacionResponse?
)

data class EstadoRequest(
    val estado: String
)
