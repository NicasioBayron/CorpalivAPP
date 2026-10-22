package com.example.corpalivapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.corpalivapp.ui.components.IconoErrorCampo
import com.example.corpalivapp.ui.components.MensajeFormulario
import com.example.corpalivapp.ui.theme.TextoSobreVerde
import com.example.corpalivapp.ui.theme.VerdeCorpaliv
import com.example.corpalivapp.viewmodel.RegistroViewModel

@Composable
fun RegistroContenido(
    onVolverClick: () -> Unit,
    registroViewModel: RegistroViewModel = viewModel()
) {
    val estado by registroViewModel.uiState.collectAsStateWithLifecycle()
    val colorTexto = MaterialTheme.colorScheme.onBackground
    val colorTitulo = MaterialTheme.colorScheme.primary
    val coloresCampo = OutlinedTextFieldDefaults.colors(
        focusedTextColor = colorTexto,
        unfocusedTextColor = colorTexto,
        focusedContainerColor = MaterialTheme.colorScheme.surfaceContainer,
        unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainer,
        focusedBorderColor = colorTitulo,
        unfocusedBorderColor = MaterialTheme.colorScheme.outline,
        focusedLabelColor = colorTitulo,
        unfocusedLabelColor = colorTexto,
        cursorColor = colorTitulo
    )

    Column(
        modifier = Modifier.fillMaxWidth().verticalScroll(rememberScrollState()).padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Crear cuenta", fontSize = 26.sp, fontWeight = FontWeight.Bold, color = colorTitulo)
        Text("Completa tus datos para registrarte", color = colorTexto)

        OutlinedTextField(
            value = estado.nombre,
            onValueChange = registroViewModel::actualizarNombre,
            label = { Text("Nombre completo") },
            modifier = Modifier.fillMaxWidth(), singleLine = true,
            enabled = !estado.registrando,
            isError = estado.errorNombre != null,
            supportingText = { estado.errorNombre?.let { Text(it) } },
            trailingIcon = { IconoErrorCampo(estado.errorNombre) },
            colors = coloresCampo
        )
        OutlinedTextField(
            value = estado.correo,
            onValueChange = registroViewModel::actualizarCorreo,
            label = { Text("Correo electrónico") },
            modifier = Modifier.fillMaxWidth(), singleLine = true,
            enabled = !estado.registrando,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            isError = estado.errorCorreo != null,
            supportingText = { estado.errorCorreo?.let { Text(it) } },
            trailingIcon = { IconoErrorCampo(estado.errorCorreo) },
            colors = coloresCampo
        )
        OutlinedTextField(
            value = estado.clave,
            onValueChange = registroViewModel::actualizarClave,
            label = { Text("Contraseña") },
            modifier = Modifier.fillMaxWidth(), singleLine = true,
            enabled = !estado.registrando,
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            isError = estado.errorClave != null,
            supportingText = {
                Text(estado.errorClave ?: "Mínimo 8 caracteres, una mayúscula, un número y un símbolo.")
            },
            trailingIcon = { IconoErrorCampo(estado.errorClave) },
            colors = coloresCampo
        )

        Button(
            onClick = registroViewModel::registrar,
            enabled = !estado.registrando,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = VerdeCorpaliv, contentColor = TextoSobreVerde),
            shape = RoundedCornerShape(12.dp)
        ) {
            if (estado.registrando) {
                CircularProgressIndicator(modifier = Modifier.size(20.dp), strokeWidth = 2.dp,
                    color = TextoSobreVerde)
                Spacer(Modifier.width(8.dp))
            }
            Text(if (estado.registrando) "Registrando..." else "Registrarse")
        }
        MensajeFormulario(estado.mensaje, estado.esError)
        TextButton(
            enabled = !estado.registrando,
            onClick = {
                registroViewModel.limpiar()
                onVolverClick()
            }
        ) {
            Text("Volver a iniciar sesión", color = colorTitulo)
        }
    }
}
