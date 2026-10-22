package com.example.corpalivapp.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val ColoresClaros = lightColorScheme(
    primary = VerdeCorpaliv,
    onPrimary = Color.White,
    background = FondoClaro,
    onBackground = TextoClaro,
    surface = FondoClaro,
    onSurface = TextoClaro,
    surfaceVariant = TarjetaClara,
    onSurfaceVariant = Color(0xFF48534D),
    secondaryContainer = BeigeClaro,
    onSecondaryContainer = TextoClaro,
    primaryContainer = Color(0xFFD0E2D6),
    onPrimaryContainer = VerdeCorpaliv,
    secondary = Color(0xFF935A30),
    tertiaryContainer = Color(0xFFEBD5BC),
    outline = Color.Gray,
    outlineVariant = Color(0xFFCED8D0),
    surfaceContainer = Color.White
)

private val ColoresOscuros = darkColorScheme(
    primary = TituloOscuro,
    onPrimary = FondoOscuro,
    background = FondoOscuro,
    onBackground = TextoOscuro,
    surface = FondoOscuro,
    onSurface = TextoOscuro,
    surfaceVariant = TarjetaOscura,
    onSurfaceVariant = Color(0xFFE5EAE5),
    secondaryContainer = BeigeOscuro,
    onSecondaryContainer = TextoOscuro,
    primaryContainer = Color(0xFF466453),
    onPrimaryContainer = TituloOscuro,
    secondary = Color(0xFFFFD3A8),
    tertiaryContainer = Color(0xFF665043),
    outline = Color(0xFF91A79A),
    outlineVariant = Color(0xFF64776A),
    surfaceContainer = Color(0xFF304037)
)

@Composable
fun CorpalivAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
){
    MaterialTheme(
        colorScheme = if (darkTheme){
            ColoresOscuros
        } else {
            ColoresClaros
        },
        typography = Typography,
        content = content
    )
}