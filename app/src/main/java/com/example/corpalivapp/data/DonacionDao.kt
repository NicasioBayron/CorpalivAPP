package com.example.corpalivapp.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface DonacionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun registrar(donacion: Donacion): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun registrarLista(donaciones: List<Donacion>)

    @Query("SELECT * FROM donaciones WHERE id = :id LIMIT 1")
    suspend fun buscarPorId(id: Int): Donacion?

    @Query(
        """
        SELECT * FROM donaciones
        WHERE usuarioId = :usuarioId
        ORDER BY fechaRegistro DESC, id DESC
        """
    )
    fun observarPorUsuario(usuarioId: Int): Flow<List<Donacion>>

    @Query(
        """
        SELECT * FROM donaciones
        ORDER BY fechaRegistro DESC, id DESC
        """
    )
    fun observarTodas(): Flow<List<Donacion>>

    @Query(
        """
        UPDATE donaciones
        SET estado = :nuevoEstado
        WHERE id = :id
        """
    )
    suspend fun actualizarEstado(id: Int, nuevoEstado: String)
}
