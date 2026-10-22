package com.example.corpalivapp

import androidx.lifecycle.ViewModelProvider
import androidx.test.core.app.ActivityScenario
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.corpalivapp.viewmodel.ContactoViewModel
import com.example.corpalivapp.viewmodel.RegistroViewModel
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith

/** No registra cuentas ni guarda solicitudes: comprueba los borradores en memoria. */
@RunWith(AndroidJUnit4::class)
class FormulariosRecreacionTest {
    @Test
    fun registroConservaBorradorYClaveAlRecrearLaActividad() {
        ActivityScenario.launch(MainActivity::class.java).use { escenario ->
            lateinit var original: RegistroViewModel
            escenario.onActivity { actividad ->
                original = ViewModelProvider(actividad).get("prueba-registro", RegistroViewModel::class.java)
                original.actualizarNombre("Persona de prueba")
                original.actualizarCorreo("prueba@ejemplo.cl")
                original.actualizarClave("ClavePrueba12!")
            }
            escenario.recreate()
            escenario.onActivity { actividad ->
                val restaurado = ViewModelProvider(actividad).get("prueba-registro", RegistroViewModel::class.java)
                assertSame(original, restaurado)
                assertEquals("Persona de prueba", restaurado.uiState.value.nombre)
                assertEquals("prueba@ejemplo.cl", restaurado.uiState.value.correo)
                assertEquals("ClavePrueba12!", restaurado.uiState.value.clave)
                assertFalse(restaurado.uiState.value.registrando)
            }
        }
    }

    @Test
    fun contactoConservaTextoLargoYNoComparteBorradorEntreCuentas() {
        val texto = "Quisiera colaborar con los programas de la organización. ".repeat(30)
        ActivityScenario.launch(MainActivity::class.java).use { escenario ->
            lateinit var original: ContactoViewModel
            escenario.onActivity { actividad ->
                original = ViewModelProvider(actividad).get("prueba-contacto-1", ContactoViewModel::class.java)
                original.actualizarNombre("Persona de prueba")
                original.actualizarCorreo("prueba@ejemplo.cl")
                original.actualizarMensaje(texto)
                original.actualizarTipo("Voluntariado")
            }
            escenario.recreate()
            escenario.onActivity { actividad ->
                val restaurado = ViewModelProvider(actividad).get("prueba-contacto-1", ContactoViewModel::class.java)
                assertSame(original, restaurado)
                assertEquals(texto, restaurado.uiState.value.mensaje)
                assertEquals("Voluntariado", restaurado.uiState.value.tipoColaboracion)
                val otraCuenta = ViewModelProvider(actividad).get("prueba-contacto-2", ContactoViewModel::class.java)
                assertEquals("", otraCuenta.uiState.value.mensaje)
            }
        }
    }
}
