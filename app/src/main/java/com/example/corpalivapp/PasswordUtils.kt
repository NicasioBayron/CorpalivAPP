package com.example.corpalivapp.data

import android.util.Base64
import java.security.SecureRandom
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec
import java.security.MessageDigest

object PasswordUtils {

    private const val ITERACIONES = 600000
    private const val LONGITUD_HASH = 256

    fun generarSalt(): String {
        val bytes = ByteArray(16)
        SecureRandom().nextBytes(bytes)
        return Base64.encodeToString(bytes, Base64.NO_WRAP)
    }

    fun generarHash(clave: String, salt: String): String {
        val saltBytes = Base64.decode(salt, Base64.NO_WRAP)

        val spec = PBEKeySpec(
            clave.toCharArray(),
            saltBytes,
            ITERACIONES,
            LONGITUD_HASH
        )

        return try {
            val factory = SecretKeyFactory.getInstance(
                "PBKDF2WithHmacSHA256"
            )

            val hash = factory.generateSecret(spec).encoded
            Base64.encodeToString(hash, Base64.NO_WRAP)
        } finally {
            spec.clearPassword()
        }
    }

    fun verificarClave(
        claveIngresada: String,
        hashGuardado: String,
        saltGuardado: String
    ): Boolean {
        val hashCalculado = generarHash(
            claveIngresada,
            saltGuardado
        )

        return MessageDigest.isEqual(
            Base64.decode(hashCalculado, Base64.NO_WRAP),
            Base64.decode(hashGuardado, Base64.NO_WRAP)
        )
    }
}