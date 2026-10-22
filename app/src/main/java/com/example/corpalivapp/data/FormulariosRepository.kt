package com.example.corpalivapp.data

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CorreoRegistradoException : Exception()

class FormulariosRepository(context: Context) {
    private val database = DatabaseProvider.obtenerDatabase(context.applicationContext)

    suspend fun registrar(nombre: String, correo: String, clave: String) =
        withContext(Dispatchers.IO) {
            val dao = database.usuarioDao()
            if (dao.buscarPorCorreo(correo) != null) throw CorreoRegistradoException()
            val salt = PasswordUtils.generarSalt()
            val hash = PasswordUtils.generarHash(clave, salt)
            dao.registrarUsuario(Usuario(nombre = nombre, correo = correo,
                claveHash = hash, claveSalt = salt))
        }

    suspend fun enviarContacto(solicitud: SolicitudContacto) = withContext(Dispatchers.IO) {
        database.solicitudContactoDao().guardarSolicitud(solicitud)
    }
}
