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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.saveable.rememberSaveable

import com.example.corpalivapp.data.DatabaseProvider
import com.example.corpalivapp.data.PasswordUtils
import com.example.corpalivapp.data.Usuario
import com.example.corpalivapp.ui.theme.VerdeCorpaliv
import com.example.corpalivapp.viewmodel.LoginUiState
import com.example.corpalivapp.viewmodel.LoginViewModel
import com.example.corpalivapp.viewmodel.validation.LoginValidator

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Locale

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
            var nombre by rememberSaveable { mutableStateOf("") }
            var correo by rememberSaveable { mutableStateOf("") }
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
            Text("Iniciar sesión")
        }

        estado.mensaje?.let { mensaje ->
            Text(
                text = mensaje,
                color = colorTexto
            )
        }

        TextButton(onClick = onRegistroClick, enabled = !estado.cargando,) {
            Text(
                text = "¿No tienes una cuenta? Regístrate",
                color = colorTitulo
            )
        }
    }
}

@Composable
fun RegistroContenido(
    onVolverClick: () -> Unit,
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

    var nombre by remember { mutableStateOf("") }
    var correo by remember { mutableStateOf("") }
    var clave by remember { mutableStateOf("") }
    var mensaje by remember { mutableStateOf("") }

    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var registrando by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Crear cuenta",
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            color = colorTitulo
        )

        Text(
            text = "Completa tus datos para registrarte",
            color = colorTexto
        )

        OutlinedTextField(
            value = nombre,
            onValueChange = { nombre = it },
            label = { Text("Nombre completo") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            enabled = !registrando,
            colors = coloresCampo
        )

        OutlinedTextField(
            value = correo,
            onValueChange = { correo = it },
            label = { Text("Correo electrónico") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            enabled = !registrando,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email
            ),
            colors = coloresCampo
        )

        OutlinedTextField(
            value = clave,
            onValueChange = { clave = it },
            label = { Text("Contraseña") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            enabled = !registrando,
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password
            ),
            colors = coloresCampo
        )

        Button(
            onClick = {
                val nombreLimpio = nombre.trim()
                val correoLimpio = correo.trim().lowercase(Locale.ROOT)

                val errorCorreo = LoginValidator.validarCorreo(correoLimpio)
                val errorClave = LoginValidator.validarClave(clave)

                when {
                    nombreLimpio.isBlank() -> {
                        mensaje = "Ingresa tu nombre"
                    }

                    errorCorreo != null -> {
                        mensaje = errorCorreo
                    }

                    errorClave != null -> {
                        mensaje = errorClave
                    }

                    else -> {
                        registrando = true
                        mensaje = ""

                        scope.launch {
                            try {
                                val resultado = withContext(Dispatchers.IO) {
                                    val dao = DatabaseProvider
                                        .obtenerDatabase(context)
                                        .usuarioDao()

                                    val existente = dao.buscarPorCorreo(correoLimpio)

                                    if (existente != null) {
                                        false
                                    } else {
                                        val salt = PasswordUtils.generarSalt()
                                        val hash = PasswordUtils.generarHash(clave, salt)

                                        val usuario = Usuario(
                                            nombre = nombreLimpio,
                                            correo = correoLimpio,
                                            claveHash = hash,
                                            claveSalt = salt
                                        )

                                        dao.registrarUsuario(usuario)
                                        true
                                    }
                                }

                                if (resultado) {
                                    mensaje = "¡Cuenta registrada correctamente!"
                                    nombre = ""
                                    correo = ""
                                    clave = ""
                                } else {
                                    mensaje = "Este correo ya está registrado"
                                }

                            } catch (e: Exception) {
                                android.util.Log.e(
                                    "CORPALIV_REGISTRO",
                                    "Error al registrar usuario",
                                    e
                                )
                                mensaje = "No se pudo registrar la cuenta"
                            } finally {
                                registrando = false
                            }
                        }
                    }
                }
            },
            enabled = !registrando &&
                    nombre.isNotBlank() &&
                    correo.isNotBlank() &&
                    clave.isNotBlank(),
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = verde,
                contentColor = Color.White
            )
        ) {
            Text(
                if (registrando) "Registrando..."
                else "Registrarse"
            )
        }

        if (mensaje.isNotEmpty()) {
            Text(
                text = mensaje,
                color = colorTitulo
            )
        }

        TextButton(
            onClick = onVolverClick,
            enabled = !registrando
        ) {
            Text(
                text = "Volver a iniciar sesión",
                color = colorTitulo
            )
        }
    }
}