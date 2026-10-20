package com.example.corpalivapp.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.viewmodel.compose.viewModel

import com.example.corpalivapp.viewmodel.LoginViewModel
import com.example.corpalivapp.HomeScreen
import com.example.corpalivapp.ui.components.OpcionesDeslizante
import com.example.corpalivapp.ui.screens.AjustesScreen
import com.example.corpalivapp.ui.screens.AportesScreen
import com.example.corpalivapp.ui.screens.ContactoScreen
import com.example.corpalivapp.ui.screens.LoginScreen
import com.example.corpalivapp.ui.screens.NosotrosScreen
import com.example.corpalivapp.ui.screens.UsuarioScreen

@Composable
fun App() {
    val loginViewModel: LoginViewModel = viewModel()

    var pantallaActual by rememberSaveable {
        mutableStateOf("Inicio")
    }

    var usuarioId by rememberSaveable {
        mutableStateOf<Int?>(null)
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
                    onMenuClick = abrirMenu
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
                    onMenuClick = abrirMenu
                )

                "Sobre nosotros" -> NosotrosScreen(
                    modifier = Modifier.fillMaxSize(),
                    onMenuClick = abrirMenu
                )
            }
        }
    }
}