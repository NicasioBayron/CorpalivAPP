package com.example.corpalivapp.data

import android.content.Context
import androidx.room.Room

object DatabaseProvider {

    @Volatile
    private var instancia: AppDatabase? = null

    fun obtenerDatabase(context: Context): AppDatabase {
        return instancia ?: synchronized(this) {
            instancia ?: Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "corpaliv_database"
            ).build().also {
                instancia = it
            }
        }
    }
}