package com.example.corpalivapp.data

import java.text.Normalizer
import java.util.Locale

object CatalogoComunas {

    const val REGION = "Región Metropolitana de Santiago"

    val comunas = listOf(
        "Alhué",
        "Buin",
        "Calera de Tango",
        "Cerrillos",
        "Cerro Navia",
        "Colina",
        "Conchalí",
        "Curacaví",
        "El Bosque",
        "El Monte",
        "Estación Central",
        "Huechuraba",
        "Independencia",
        "Isla de Maipo",
        "La Cisterna",
        "La Florida",
        "La Granja",
        "La Pintana",
        "La Reina",
        "Lampa",
        "Las Condes",
        "Lo Barnechea",
        "Lo Espejo",
        "Lo Prado",
        "Macul",
        "Maipú",
        "María Pinto",
        "Melipilla",
        "Ñuñoa",
        "Padre Hurtado",
        "Paine",
        "Pedro Aguirre Cerda",
        "Peñaflor",
        "Peñalolén",
        "Pirque",
        "Providencia",
        "Pudahuel",
        "Puente Alto",
        "Quilicura",
        "Quinta Normal",
        "Recoleta",
        "Renca",
        "San Bernardo",
        "San Joaquín",
        "San José de Maipo",
        "San Miguel",
        "San Pedro",
        "San Ramón",
        "Santiago",
        "Talagante",
        "Til Til",
        "Vitacura"
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