package com.example.corpalivapp.data

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.coroutines.flow.Flow
class DonacionRepository(context: Context) {

    private val database = DatabaseProvider.obtenerDatabase(
        context.applicationContext
    )

    suspend fun registrar(donacion: Donacion): Long =
        withContext(Dispatchers.IO) {
            check(
                database.usuarioDao()
                    .buscarPorId(donacion.usuarioId) != null
            ) {
                "El usuario no existe."
            }

            database.donacionDao().registrar(donacion)
        }

    fun observarPorUsuario(usuarioId: Int): Flow<List<Donacion>>{
        return database.donacionDao().observarPorUsuario(usuarioId)
    }
}