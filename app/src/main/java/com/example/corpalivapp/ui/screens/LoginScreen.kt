package com.example.corpalivapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.saveable.rememberSaveable

import com.example.corpalivapp.ui.theme.VerdeCorpaliv
import com.example.corpalivapp.ui.components.IconoErrorCampo
import com.example.corpalivapp.ui.components.MensajeFormulario
import com.example.corpalivapp.viewmodel.LoginUiState
import com.example.corpalivapp.viewmodel.LoginViewModel


@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    loginViewModel: LoginViewModel = viewModel(),
    onMenuClick: () -> Unit = {},
    onLoginExitoso: (Int) -> Unit = {}
) {
    val estado by loginViewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(estado.autenticado, estado.usuarioId) {
        if (estado.autenticado && estado.usuarioId != null) {
            onLoginExitoso(estado.usuarioId!!)
        }
    }

    var mostrarRegistro by rememberSaveable{ mutableStateOf(false) }

    val verde = VerdeCorpaliv
    val fondo = MaterialTheme.colorScheme.background

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(fondo)
    ) {
        // ENCABEZADO
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(verde)
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "CORPALIV",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            TextButton(onClick = onMenuClick) {
                Text(
                    text = "☰",
                    color = Color.White,
                    fontSize = 26.sp
                )
            }
        }

        if (mostrarRegistro) {
            RegistroContenido(
                onVolverClick = {
                    mostrarRegistro = false
                }
            )
        } else {
            LoginContenido(
                estado = estado,
                onCorreoChange = loginViewModel::actualizarCorreo,
                onClaveChange = loginViewModel::actualizarClave,
                onValidarClick = loginViewModel::validarFormulario,
                onRegistroClick = {
                    mostrarRegistro = true
                }
            )
        }
    }
}

@Composable
fun LoginContenido(
    estado: LoginUiState,
    onCorreoChange: (String) -> Unit,
    onClaveChange: (String) -> Unit,
    onValidarClick: () -> Unit,
    onRegistroClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val verde = VerdeCorpaliv
    val colorTexto = MaterialTheme.colorScheme.onBackground
    val colorTitulo = MaterialTheme.colorScheme.primary
    val fondoCampo = MaterialTheme.colorScheme.surfaceContainer

    val coloresCampo = OutlinedTextFieldDefaults.colors(
        focusedTextColor = colorTexto,
        unfocusedTextColor = colorTexto,
        focusedContainerColor = fondoCampo,
        unfocusedContainerColor = fondoCampo,
        focusedBorderColor = colorTitulo,
        unfocusedBorderColor = MaterialTheme.colorScheme.outline,
        focusedLabelColor = colorTitulo,
        unfocusedLabelColor = colorTexto,
        cursorColor = colorTitulo
    )

    Column(
        modifier = modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Bienvenido",
            color = colorTitulo,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = "Ingresa tus datos para continuar",
            color = colorTexto
        )

        OutlinedTextField(
            value = estado.correo,
            onValueChange = onCorreoChange,
            enabled = !estado.cargando,
            label = { Text("Correo electrónico") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email
            ),
            isError = estado.errorCorreo != null,
            trailingIcon = { IconoErrorCampo(estado.errorCorreo) },
            supportingText = {
                estado.errorCorreo?.let { error ->
                    Text(error)
                }
            },
            colors = coloresCampo
        )

        OutlinedTextField(
            value = estado.clave,
            onValueChange = onClaveChange,
            enabled = !estado.cargando,
            label = { Text("Contraseña") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password
            ),
            isError = estado.errorClave != null,
            trailingIcon = { IconoErrorCampo(estado.errorClave) },
            supportingText = {
                estado.errorClave?.let { error ->
                    Text(error)
                }
            },
            colors = coloresCampo
        )

        Button(
            onClick = onValidarClick,
            enabled = !estado.cargando,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = verde,
                contentColor = Color.White
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            if (estado.cargando) {
                CircularProgressIndicator(modifier = Modifier.size(20.dp), color = Color.White, strokeWidth = 2.dp)
                Spacer(Modifier.width(8.dp))
            }
            Text(if (estado.cargando) "Ingresando..." else "Iniciar sesión")
        }

        MensajeFormulario(estado.mensaje, esError = !estado.autenticado)

        TextButton(onClick = onRegistroClick, enabled = !estado.cargando,) {
            Text(
                text = "¿No tienes una cuenta? Regístrate",
                color = colorTitulo
            )
        }
    }
}
