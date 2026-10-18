package com.example.corpalivapp.ui.screens

import android.R
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.corpalivapp.viewmodel.LoginUiState
import com.example.corpalivapp.viewmodel.LoginViewModel

@Composable
fun LoginScreen(modifier: Modifier = Modifier, loginViewModel: LoginViewModel = viewModel()) {
    val estado by loginViewModel.uiState.collectAsStateWithLifecycle()

    LoginContenido(
        estado = estado,
        onCorreoChange = loginViewModel::actualizarCorreo,
        onClaveChange = loginViewModel::actualizarClave,
        onValidarClick = loginViewModel::validarFormulario,
        modifier = modifier
    )
}
//Aqui se puede trabajar el diseño
//Agrega una oocion de registrarse q pida nombre, email y contraseña
@Composable
fun LoginContenido(
    estado: LoginUiState,
    onCorreoChange: (String) -> Unit,
    onClaveChange: (String) -> Unit,
    onValidarClick: () -> Unit,
    modifier: Modifier = Modifier
){
    Column(
        modifier = modifier.verticalScroll(rememberScrollState())
                           .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Iniciar sesión",
            style = MaterialTheme.typography.headlineMedium
        )
        OutlinedTextField(
            value = estado.correo,
            onValueChange = onCorreoChange,
            label = {Text("Correo electrónico")},
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email
            ),
            isError = estado.errorCorreo != null,
            supportingText = {
                estado.errorCorreo?.let { error -> Text(error) }
            },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = estado.clave,
            onValueChange = onClaveChange,
            label = {Text("Contraseña")},
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password
            ),
            isError = estado.errorClave != null,
            supportingText = {
                estado.errorClave?.let { error -> Text(error) }
            },
            modifier = Modifier.fillMaxWidth()
        )
        Button(
            onClick = onValidarClick,
            enabled = !estado.cargando,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Iniciar sesión")
        }
        estado.mensaje?.let { mensaje ->
            Text(
                text = mensaje,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}