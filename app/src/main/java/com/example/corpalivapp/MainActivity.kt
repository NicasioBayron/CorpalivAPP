package com.example.corpalivapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
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

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CorpalivAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    inicioScreen(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun inicioScreen(modifier: Modifier = Modifier){
    var mostrarMensaje by remember { mutableStateOf(false) }
    Column(modifier = modifier.fillMaxSize().padding(24.dp), verticalArrangement = Arrangement.spacedBy(26.dp)) {
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

        //Boton
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
        inicioScreen()
    }
}