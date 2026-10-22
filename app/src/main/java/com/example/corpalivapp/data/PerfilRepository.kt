package com.example.corpalivapp.data

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.os.Build
import java.io.File
import java.io.IOException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PerfilRepository(context: Context) {
    private val contexto = context.applicationContext
    private val dao = DatabaseProvider.obtenerDatabase(contexto).usuarioDao()
    private val carpeta = File(contexto.filesDir, "fotos_perfil")

    suspend fun cargarUsuario(id: Int): Usuario = withContext(Dispatchers.IO) {
        dao.buscarPorId(id) ?: throw IOException("Usuario no encontrado")
    }

    suspend fun cargarFoto(nombre: String?): Bitmap? = withContext(Dispatchers.IO) {
        nombre?.let { BitmapFactory.decodeFile(File(carpeta, File(it).name).path) }
    }

    suspend fun guardarFoto(id: Int, uri: Uri): Usuario = withContext(Dispatchers.IO) {
        val anterior = dao.buscarPorId(id) ?: throw IOException("Usuario no encontrado")
        val bitmap = decodificar(uri)
        if (!carpeta.exists() && !carpeta.mkdirs()) {
            bitmap.recycle()
            throw IOException("No se pudo crear la carpeta")
        }
        // Un archivo nuevo evita sobrescribir la foto anterior si falla el guardado.
        val nuevo = File.createTempFile("usuario_${id}_", ".jpg", carpeta)
        try {
            nuevo.outputStream().use { salida ->
                if (!bitmap.compress(Bitmap.CompressFormat.JPEG, 85, salida)) {
                    throw IOException("No se pudo guardar la imagen")
                }
            }
            if (dao.actualizarFoto(id, nuevo.name) != 1) {
                throw IOException("No se pudo actualizar el perfil")
            }
        } catch (error: Exception) {
            nuevo.delete()
            throw error
        } finally {
            bitmap.recycle()
        }
        anterior.fotoPerfil?.let { File(carpeta, File(it).name).delete() }
        anterior.copy(fotoPerfil = nuevo.name)
    }

    private fun decodificar(uri: Uri): Bitmap {
        val resolver = contexto.contentResolver
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            // ImageDecoder respeta la orientacion y reduce antes de cargar en memoria.
            return ImageDecoder.decodeBitmap(ImageDecoder.createSource(resolver, uri)) {
                    decoder, info, _ ->
                val mayor = maxOf(info.size.width, info.size.height)
                val escala = minOf(1.0, 1024.0 / mayor)
                decoder.setTargetSize(
                    maxOf(1, (info.size.width * escala).toInt()),
                    maxOf(1, (info.size.height * escala).toInt())
                )
                decoder.allocator = ImageDecoder.ALLOCATOR_SOFTWARE
            }
        }

        // Compatibilidad con Android 8: limitar memoria y respetar EXIF.
        val limites = BitmapFactory.Options().apply { inJustDecodeBounds = true }
        resolver.openInputStream(uri)?.use { BitmapFactory.decodeStream(it, null, limites) }
        if (limites.outWidth <= 0 || limites.outHeight <= 0) throw IOException("Imagen invalida")
        var muestra = 1
        while (maxOf(limites.outWidth, limites.outHeight) / muestra > 1024) muestra *= 2
        val opciones = BitmapFactory.Options().apply { inSampleSize = muestra }
        val original = resolver.openInputStream(uri)?.use {
            BitmapFactory.decodeStream(it, null, opciones)
        } ?: throw IOException("No se pudo leer la imagen")
        val orientacion = resolver.openInputStream(uri)?.use {
            ExifInterface(it).getAttributeInt(ExifInterface.TAG_ORIENTATION, 1)
        } ?: 1
        val matriz = Matrix().apply {
            when (orientacion) {
                2 -> setScale(-1f, 1f)
                3 -> setRotate(180f)
                4 -> setScale(1f, -1f)
                5 -> { setRotate(90f); postScale(-1f, 1f) }
                6 -> setRotate(90f)
                7 -> { setRotate(-90f); postScale(-1f, 1f) }
                8 -> setRotate(-90f)
            }
        }
        val resultado = Bitmap.createBitmap(original, 0, 0, original.width, original.height, matriz, true)
        if (resultado !== original) original.recycle()
        return resultado
    }
}