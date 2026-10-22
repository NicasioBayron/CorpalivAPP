package com.example.corpalivapp.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface DonacionDao {

    @Insert
    suspend fun registrar(donacion: Donacion): Long

    @Query(
        """
        SELECT * FROM donaciones
        WHERE usuarioId = :usuarioId
        ORDER BY fechaRegistro DESC, id DESC
        """
    )
    fun observarPorUsuario(usuarioId: Int): Flow<List<Donacion>>
}