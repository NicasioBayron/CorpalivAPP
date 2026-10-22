package com.example.corpalivapp.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.corpalivapp.ui.components.IconoErrorCampo
import com.example.corpalivapp.ui.components.MensajeFormulario
import com.example.corpalivapp.ui.theme.TextoSobreVerde
import com.example.corpalivapp.ui.theme.VerdeCorpaliv
import com.example.corpalivapp.viewmodel.ContactoViewModel
import com.example.corpalivapp.viewmodel.validation.ContactoValidator

@Composable
fun ContactoScreen(
    modifier: Modifier = Modifier,
    usuarioId: Int? = null,
    onMenuClick: () -> Unit = {},
    contactoViewModel: ContactoViewModel = viewModel(key = "contacto_${usuarioId ?: "invitado"}")
) {
    val estado by contactoViewModel.uiState.collectAsStateWithLifecycle()
    val colorTexto = MaterialTheme.colorScheme.onBackground
    val colorTitulo = MaterialTheme.colorScheme.primary
    val coloresCampo = OutlinedTextFieldDefaults.colors(
        focusedTextColor = colorTexto, unfocusedTextColor = colorTexto,
        focusedContainerColor = MaterialTheme.colorScheme.surfaceContainer,
        unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainer,
        focusedBorderColor = colorTitulo,
        unfocusedBorderColor = MaterialTheme.colorScheme.outline,
        focusedLabelColor = colorTitulo, unfocusedLabelColor = colorTexto,
        cursorColor = colorTitulo
    )

    Column(modifier = modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)
        .verticalScroll(rememberScrollState())) {
        Row(
            modifier = Modifier.fillMaxWidth().background(VerdeCorpaliv).padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Contáctanos", color = TextoSobreVerde, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            TextButton(onClick = onMenuClick) {
                Text("☰", color = TextoSobreVerde, fontSize = 26.sp)
            }
        }

        Column(modifier = Modifier.padding(24.dp).navigationBarsPadding(),
            verticalArrangement = Arrangement.spacedBy(16.dp)) {
            Text("¡Hablemos!", fontSize = 26.sp, fontWeight = FontWeight.Bold, color = colorTitulo)
            Text("Completa el formulario y cuéntanos cómo te gustaría colaborar con CORPALIV.", color = colorTexto)
            OutlinedTextField(
                value = estado.nombre, onValueChange = contactoViewModel::actualizarNombre,
                label = { Text("Nombre completo") }, modifier = Modifier.fillMaxWidth(),
                singleLine = true, enabled = !estado.guardando, colors = coloresCampo,
                isError = estado.errorNombre != null,
                supportingText = { estado.errorNombre?.let { Text(it) } },
                trailingIcon = { IconoErrorCampo(estado.errorNombre) }
            )
            OutlinedTextField(
                value = estado.correo, onValueChange = contactoViewModel::actualizarCorreo,
                label = { Text("Correo electrónico") }, modifier = Modifier.fillMaxWidth(),
                singleLine = true, enabled = !estado.guardando, colors = coloresCampo,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                isError = estado.errorCorreo != null,
                supportingText = { estado.errorCorreo?.let { Text(it) } },
                trailingIcon = { IconoErrorCampo(estado.errorCorreo) }
            )
            Text("¿Cómo deseas colaborar?", fontWeight = FontWeight.Bold, color = colorTitulo)
            ContactoValidator.tipos.forEach { opcion ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(selected = estado.tipoColaboracion == opcion,
                        onClick = { contactoViewModel.actualizarTipo(opcion) }, enabled = !estado.guardando,
                        colors = RadioButtonDefaults.colors(selectedColor = colorTitulo, unselectedColor = colorTexto))
                    Text(opcion, color = colorTexto)
                }
            }
            MensajeFormulario(estado.errorTipo, esError = true)
            OutlinedTextField(
                value = estado.mensaje, onValueChange = contactoViewModel::actualizarMensaje,
                label = { Text("Mensaje") },
                modifier = Modifier.fillMaxWidth().heightIn(min = 130.dp),
                minLines = 3, maxLines = 5, enabled = !estado.guardando, colors = coloresCampo,
                isError = estado.errorMensaje != null,
                supportingText = { estado.errorMensaje?.let { Text(it) } },
                trailingIcon = { IconoErrorCampo(estado.errorMensaje) }
            )
            Button(
                onClick = { contactoViewModel.enviar(usuarioId) }, enabled = !estado.guardando,
                modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = VerdeCorpaliv, contentColor = TextoSobreVerde)
            ) {
                if (estado.guardando) {
                    CircularProgressIndicator(modifier = Modifier.size(20.dp), color = TextoSobreVerde, strokeWidth = 2.dp)
                    Spacer(Modifier.width(8.dp))
                }
                Text(if (estado.guardando) "Guardando..." else "Enviar solicitud")
            }
            MensajeFormulario(estado.resultado, estado.esError)
        }
    }
}
