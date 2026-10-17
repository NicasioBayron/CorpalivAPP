package com.example.corpalivapp

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.corpalivapp.ui.theme.CorpalivAppTheme
import com.example.corpalivapp.ui.components.ProgramaCard

@Composable
fun HomeScreen(modifier: Modifier = Modifier){
    var mostrarMensaje by remember { mutableStateOf(false) }
    Column(modifier = modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(24.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text(
            text = "Corpaliv APP",
            style = MaterialTheme.typography.headlineLarge
        )
        Text(
            text = "Conoce nuestra mision y como colaborar con nosotros!!",
            style = MaterialTheme.typography.bodyLarge
        )
        Text(
            text = "Nuestra mision",
            style = MaterialTheme.typography.headlineSmall
        )
        Text(
            text = "Aqui va la mision",
            style = MaterialTheme.typography.bodyLarge
        )
        Text(
            text = "Nuestro trabajo",
            style = MaterialTheme.typography.headlineSmall
        )

        ProgramaCard(
            titulo = "Escuela Jan Van Dijk",
            descripcion = "Aqui presentaremos el trabajo de la escuela"
        )
        ProgramaCard(
            titulo = "Alba lab",
            descripcion = "Trabajo de alba lab"
        )

        //Boton (falta agregar animacion)
        Button(onClick = {mostrarMensaje = true}, modifier = modifier.fillMaxWidth()) {
            Text(
                text = "Quiero Colaborar"
            )
        }
        if (mostrarMensaje) {
            Text(
                text = "¡Gracias por tu ayuda!",
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun inicioPreview() {
    CorpalivAppTheme {
        HomeScreen()
    }
}