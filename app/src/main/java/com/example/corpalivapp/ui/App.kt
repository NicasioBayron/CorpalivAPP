package com.example.corpalivapp.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.example.corpalivapp.HomeScreen
import com.example.corpalivapp.ui.components.MenuDeslizante
import com.example.corpalivapp.ui.components.OpcionesDeslizante
import com.example.corpalivapp.ui.screens.AjustesScreen
import com.example.corpalivapp.ui.screens.AportesScreen
import com.example.corpalivapp.ui.screens.ContactoScreen
import com.example.corpalivapp.ui.screens.LoginScreen
import com.example.corpalivapp.ui.screens.NosotrosScreen
import com.example.corpalivapp.ui.screens.UsuarioScreen

@Composable
fun App(){
    var pantallaActual by rememberSaveable { mutableStateOf("Inicio") }
    OpcionesDeslizante(
        pantallaActual = pantallaActual,
        onPantallaSeleccionada = { opcion -> pantallaActual = opcion}
    ) {
        abrirMenu -> Scaffold(modifier = Modifier.fillMaxSize(),
            topBar = { MenuDeslizante(onMenuClick = abrirMenu) })
    { innerPadding -> val contenidoModifier = Modifier.padding(innerPadding)
        when (pantallaActual){
            "Inicio" -> HomeScreen(
                modifier = contenidoModifier
            )
            "Usuario" -> UsuarioScreen(
                modifier = contenidoModifier
            )
            "Iniciar sesión" -> LoginScreen(
                modifier = contenidoModifier
            )
            "Ajustes" -> AjustesScreen(
                modifier = contenidoModifier
            )
            "Aportes" -> AportesScreen(
                modifier = contenidoModifier
            )
            "Contactanos" -> ContactoScreen(
                modifier = contenidoModifier
            )
            "Sobre nosotros" -> NosotrosScreen(
                modifier = contenidoModifier
            )
        }
    }
    }
}