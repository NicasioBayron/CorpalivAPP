package com.example.corpalivapp

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.shape.RoundedCornerShape

import com.example.corpalivapp.ui.components.ProgramaCard
import com.example.corpalivapp.ui.theme.CorpalivAppTheme

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    modoOscuro: Boolean = false,
    onMenuClick: () -> Unit = {},
    onColaborarClick: () -> Unit = {}
) {
    val verdeCorpaliv = Color(0xFF286657)

    val fondo = if (modoOscuro) {
        Color(0xFF202820)
    } else {
        Color(0xFFFAF8F5)
    }

    val colorTexto = if (modoOscuro) {
        Color.White
    } else {
        Color(0xFF35443D)
    }

    val colorTitulo = if (modoOscuro) {
        Color(0xFFB5E2C8)
    } else {
        verdeCorpaliv
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(fondo)
            .verticalScroll(rememberScrollState())
    ) {

        // Encabezado
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(verdeCorpaliv)
                .statusBarsPadding()
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    text = "CORPALIV",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )

                IconButton(
                    onClick = onMenuClick
                ) {
                    Text(
                        text = "☰",
                        color = Color.White,
                        fontSize = 28.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Construyendo Inclusión Juntos",
                color = Color.White,
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                lineHeight = 36.sp
            )

            Text(
                text = "Conoce nuestra misión y descubre cómo puedes ser parte del cambio.",
                color = Color(0xFFE4F2EC),
                fontSize = 15.sp,
                lineHeight = 23.sp
            )
        }

        // Contenido principal
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            Text(
                text = "Nuestra Misión",
                style = MaterialTheme.typography.headlineSmall,
                color = colorTitulo
            )

            Text(
                text = "Promovemos la autonomía, participación e inclusión social y laboral de personas con discapacidad múltiple.",
                style = MaterialTheme.typography.bodyLarge,
                color = colorTexto
            )

            Text(
                text = "Conoce nuestros programas",
                style = MaterialTheme.typography.headlineSmall,
                color = colorTitulo
            )

            ProgramaCard(
                titulo = "Escuela Jan Van Dijk",
                descripcion = "Educación, formación e inclusión social.",
                modoOscuro = modoOscuro
            )

            ProgramaCard(
                titulo = "Alba Lab",
                descripcion = "Formación e inclusión sociolaboral.",
                modoOscuro = modoOscuro
            )

            Button(
                onClick = onColaborarClick,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = verdeCorpaliv,
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(text = "Quiero Colaborar")
            }
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

