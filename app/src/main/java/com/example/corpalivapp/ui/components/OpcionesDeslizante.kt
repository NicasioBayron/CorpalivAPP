package com.example.corpalivapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.corpalivapp.ui.theme.VerdeCorpaliv
import kotlinx.coroutines.launch

@Composable
fun OpcionesDeslizante(
    pantallaActual: String,
    onPantallaSeleccionada: (String) -> Unit,
    contenido: @Composable (abrirMenu: () -> Unit) -> Unit
) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val verdeCorpaliv = VerdeCorpaliv
    val fondoMenu = MaterialTheme.colorScheme.background
    val colorTexto = MaterialTheme.colorScheme.onBackground
    val fondoSeleccionado = MaterialTheme.colorScheme.surfaceVariant
    val textoSeleccionado = MaterialTheme.colorScheme.primary

    val opciones = listOf(
        "Inicio",
        "Iniciar sesión",
        "Ajustes",
        "Aportes",
        "Mis solicitudes",
        "Contactanos",
        "Sobre nosotros"
    )

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                drawerContainerColor = fondoMenu
            ) {

                // Encabezado del menu
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(verdeCorpaliv)
                        .padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "CORPALIV",
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = "Construyendo inclusión juntos",
                        color = Color.White
                    )
                }

                // Las opciones del menu
                Column(
                    modifier = Modifier.padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    opciones.forEach { opcion ->
                        NavigationDrawerItem(
                            label = {
                                Text(text = opcion)
                            },
                            selected = pantallaActual == opcion,
                            onClick = {
                                onPantallaSeleccionada(opcion)

                                scope.launch {
                                    drawerState.close()
                                }
                            },
                            colors = NavigationDrawerItemDefaults.colors(
                                selectedContainerColor = fondoSeleccionado,
                                selectedTextColor = textoSeleccionado,
                                unselectedTextColor = colorTexto
                            )
                        )
                    }
                }
            }
        }
    ) {
        contenido {
            scope.launch {
                drawerState.open()
            }
        }
    }
}