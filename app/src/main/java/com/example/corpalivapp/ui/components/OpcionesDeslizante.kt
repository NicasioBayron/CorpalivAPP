package com.example.corpalivapp.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.corpalivapp.R
import kotlinx.coroutines.launch

@Composable
fun OpcionesDeslizante(pantallaActual: String, onPantallaSeleccionada : (String) -> Unit,
                       contenido : @Composable (abrirMenu: () -> Unit) -> Unit){
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet{
                Box(
                    modifier = Modifier.size(120.dp)
                ){
                    Image(
                        painter = painterResource(R.drawable.ic_launcher_background),
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize()
                    )
                    Image(
                        painter = painterResource(R.drawable.ic_launcher_foreground),
                        contentDescription = "Logo",
                        modifier = Modifier.fillMaxSize()
                    )
                }
                val opciones = listOf(
                    "Inicio",
                    "Iniciar sesión",
                    "Ajustes",
                    "Aportes",
                    "Contactanos",
                    "Sobre nosotros"
                )
                opciones.forEach { opcion -> NavigationDrawerItem(
                    label = {Text(opcion)},
                    selected = pantallaActual == opcion,
                    onClick = {onPantallaSeleccionada(opcion)
                    scope.launch { drawerState.close() }}
                ) }
            }
        }
    ) {
        contenido{
            scope.launch {
                drawerState.open()
            }
        }
    }
}