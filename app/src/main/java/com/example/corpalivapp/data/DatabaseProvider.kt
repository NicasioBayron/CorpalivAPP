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

    private val MIGRATION_4_5 = object : Migration(4, 5) {

        override fun migrate(db: SupportSQLiteDatabase) {
            db.execSQL(
                """
            CREATE TABLE IF NOT EXISTS donaciones (
                id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                usuarioId INTEGER NOT NULL,
                nombre TEXT NOT NULL,
                correo TEXT NOT NULL,
                monto INTEGER NOT NULL,
                fechaAporte TEXT NOT NULL,
                aceptaCondiciones INTEGER NOT NULL,
                recibirSeguimiento INTEGER NOT NULL,
                fechaRegistro INTEGER NOT NULL,
                fotoComprobante TEXT
            )
            """.trimIndent()
            )
        }
    }
    private val MIGRATION_5_6 = object : Migration(5, 6) {

        override fun migrate(db: SupportSQLiteDatabase) {
            db.execSQL(
                "ALTER TABLE donaciones ADD COLUMN region TEXT"
            )

            db.execSQL(
                "ALTER TABLE donaciones ADD COLUMN comuna TEXT"
            )
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
                    MIGRATION_3_4,
                    MIGRATION_4_5,
                    MIGRATION_5_6
                )
                .build()
                .also {
                    instancia = it
                }
        }
    }
}