package com.example.corpalivapp.data

import java.text.Normalizer
import java.util.Locale

object CatalogoComunas {

    const val REGION = "Región Metropolitana de Santiago"

    val comunas = listOf(
        "La Florida",
        "Maipú",
        "Ñuñoa",
        "Providencia",
        "Santiago"
    ).sorted()

    fun filtrar(busqueda: String): List<String> {
        val texto = normalizar(busqueda.trim())

        if (texto.isBlank()) return emptyList()

        return comunas.filter { comuna ->
            normalizar(comuna).contains(texto)
        }
    }

    private fun normalizar(texto: String): String {
        return Normalizer.normalize(
            texto,
            Normalizer.Form.NFD
        )
            .replace(Regex("\\p{M}+"), "")
            .lowercase(Locale.ROOT)
    }
}