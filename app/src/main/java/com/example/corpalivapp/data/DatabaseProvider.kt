package com.example.corpalivapp.data

import android.content.Context
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

object DatabaseProvider {

    @Volatile
    private var instancia: AppDatabase? = null

    // Migracion 1 A 2
    // Agrega la tabla de solicitudes de contacto
    private val MIGRATION_1_2 = object : Migration(1, 2) {

        override fun migrate(db: SupportSQLiteDatabase) {
            db.execSQL(
                """
                CREATE TABLE IF NOT EXISTS solicitudes_contacto (
                    id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                    nombre TEXT NOT NULL,
                    correo TEXT NOT NULL,
                    tipoColaboracion TEXT NOT NULL,
                    mensaje TEXT NOT NULL,
                    fechaRegistro INTEGER NOT NULL
                )
                """.trimIndent()
            )
        }
    }

    // Migracion 2 A 3
    // Agrega el ID del usuario a las solicitudes existentes
    private val MIGRATION_2_3 = object : Migration(2, 3) {

        override fun migrate(db: SupportSQLiteDatabase) {
            db.execSQL(
                """
                ALTER TABLE solicitudes_contacto
                ADD COLUMN usuarioId INTEGER
                """.trimIndent()
            )
        }
    }

    // Conserva las cuentas existentes y agrega la referencia a su foto privada.
    private val MIGRATION_3_4 = object : Migration(3, 4) {
        override fun migrate(db: SupportSQLiteDatabase) {
            db.execSQL("ALTER TABLE usuarios ADD COLUMN fotoPerfil TEXT")
        }
    }
    // obtener la base de datos
    fun obtenerDatabase(context: Context): AppDatabase {

        return instancia ?: synchronized(this) {

            instancia ?: Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "corpaliv_database"
            )
                .addMigrations(
                    MIGRATION_1_2,
                    MIGRATION_2_3,
                    MIGRATION_3_4
                )
                .build()
                .also {
                    instancia = it
                }
        }
    }
}