package com.example.corpalivapp.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel

import com.example.corpalivapp.HomeScreen
import com.example.corpalivapp.ui.components.OpcionesDeslizante
import com.example.corpalivapp.ui.screens.AjustesScreen
import com.example.corpalivapp.ui.screens.AportesScreen
import com.example.corpalivapp.ui.screens.ContactoScreen
import com.example.corpalivapp.ui.screens.LoginScreen
import com.example.corpalivapp.ui.screens.MisSolicitudesScreen
import com.example.corpalivapp.ui.screens.NosotrosScreen
import com.example.corpalivapp.ui.screens.UsuarioScreen
import com.example.corpalivapp.viewmodel.LoginViewModel

@Composable
fun App() {

    val loginViewModel: LoginViewModel = viewModel()

    var pantallaActual by rememberSaveable {
        mutableStateOf("Inicio")
    }

    var usuarioId by rememberSaveable {
        mutableStateOf<Int?>(null)
    }

    // ESTADO GLOBAL DEL MODO OSCURO
    var modoOscuro by rememberSaveable {
        mutableStateOf(false)
    }

    OpcionesDeslizante(
        pantallaActual = pantallaActual,
        onPantallaSeleccionada = { opcion ->
            pantallaActual = opcion
        }
    ) { abrirMenu ->

        Surface(
            modifier = Modifier.fillMaxSize()
        ) {

            when (pantallaActual) {

                "Inicio" -> HomeScreen(
                    modifier = Modifier.fillMaxSize(),
                    modoOscuro = modoOscuro,
                    onMenuClick = abrirMenu,
                    onColaborarClick = {
                        pantallaActual = "Aportes"
                    }
                )

                "Usuario" -> {
                    if (usuarioId != null) {
                        UsuarioScreen(
                            usuarioId = usuarioId!!,
                            modifier = Modifier.fillMaxSize(),
                            onMenuClick = abrirMenu,
                            onInicioClick = {
                                pantallaActual = "Inicio"
                            },
                            onCerrarSesion = {
                                loginViewModel.cerrarSesion()
                                usuarioId = null
                                pantallaActual = "Iniciar sesión"
                            }
                        )
                    } else {
                        LaunchedEffect(Unit) {
                            pantallaActual = "Iniciar sesión"
                        }
                    }
                }

                "Iniciar sesión" -> LoginScreen(
                    modifier = Modifier.fillMaxSize(),
                    loginViewModel = loginViewModel,
                    onMenuClick = abrirMenu,
                    onLoginExitoso = { id ->
                        usuarioId = id
                        pantallaActual = "Usuario"
                    }
                )

                "Ajustes" -> AjustesScreen(
                    modifier = Modifier.fillMaxSize(),
                    modoOscuro = modoOscuro,
                    onModoOscuroChange = { nuevoValor ->
                        modoOscuro = nuevoValor
                    },
                    onMenuClick = abrirMenu,
                    onPerfilClick = {
                        pantallaActual = if (usuarioId != null) {
                            "Usuario"
                        } else {
                            "Iniciar sesión"
                        }
                    },
                    onSolicitudesClick = {
                        pantallaActual = if (usuarioId != null) {
                            "Mis solicitudes"
                        } else {
                            "Iniciar sesión"
                        }
                    }
                )

                "Aportes" -> AportesScreen(
                    modifier = Modifier.fillMaxSize(),
                    onMenuClick = abrirMenu,
                    onContactoClick = {
                        pantallaActual = "Contactanos"
                    }
                )

                "Contactanos" -> ContactoScreen(
                    modifier = Modifier.fillMaxSize(),
                    usuarioId = usuarioId,
                    onMenuClick = abrirMenu
                )

                "Sobre nosotros" -> NosotrosScreen(
                    modifier = Modifier.fillMaxSize(),
                    onMenuClick = abrirMenu
                )

                "Mis solicitudes" -> {
                    if (usuarioId != null) {
                        MisSolicitudesScreen(
                            usuarioId = usuarioId!!,
                            modifier = Modifier.fillMaxSize(),
                            onMenuClick = abrirMenu
                        )
                    } else {
                        LaunchedEffect(Unit) {
                            pantallaActual = "Iniciar sesión"
                        }
                    }
                }
            }
        }
    }
}