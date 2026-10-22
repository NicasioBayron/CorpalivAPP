package com.example.corpalivapp.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [
        Usuario::class,
        SolicitudContacto::class,
        Donacion::class
    ],
    version = 6,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun usuarioDao(): UsuarioDao

    abstract fun solicitudContactoDao(): SolicitudContactoDao

    abstract fun donacionDao(): DonacionDao
}