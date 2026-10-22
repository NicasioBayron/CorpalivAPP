package com.example.corpalivapp.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.example.corpalivapp.R

@Composable
fun IconoErrorCampo(error: String?) {
    if (error != null) {
        Icon(painter = painterResource(R.drawable.ic_estado_error),
            contentDescription = "Campo con error", tint = MaterialTheme.colorScheme.error)
    }
}

@Composable
fun MensajeFormulario(
    mensaje: String?,
    esError: Boolean = false,
    modifier: Modifier = Modifier
) {
    AnimatedVisibility(visible = !mensaje.isNullOrBlank(), modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth().animateContentSize()
                .semantics { liveRegion = LiveRegionMode.Polite },
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val color = if (esError) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
            Icon(
                painter = painterResource(if (esError) R.drawable.ic_estado_error else R.drawable.ic_estado_correcto),
                contentDescription = if (esError) "Error" else "Operación completada",
                tint = color
            )
            Text(text = mensaje.orEmpty(), color = color, modifier = Modifier.weight(1f))
        }
    }
}
