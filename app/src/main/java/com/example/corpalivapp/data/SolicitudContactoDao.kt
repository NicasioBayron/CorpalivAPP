package com.example.corpalivapp.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface SolicitudContactoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun guardarSolicitud(solicitud: SolicitudContacto): Long

    @Query(
        "SELECT * FROM solicitudes_contacto ORDER BY fechaRegistro DESC"
    )
    suspend fun obtenerSolicitudes(): List<SolicitudContacto>

    @Query(
        """
        SELECT * FROM solicitudes_contacto
        WHERE usuarioId = :usuarioId
        ORDER BY fechaRegistro DESC
        """
    )
    suspend fun obtenerSolicitudesPorUsuario(
        usuarioId: Int
    ): List<SolicitudContacto>
}
