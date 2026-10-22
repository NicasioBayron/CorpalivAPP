package com.example.corpalivapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.corpalivapp.ui.theme.VerdeCorpaliv
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import com.example.corpalivapp.ui.components.ProgramaCard

@Composable
fun NosotrosScreen(
    modifier: Modifier = Modifier,
    onMenuClick: () -> Unit = {}
) {

   val verde = VerdeCorpaliv
   val fondo = MaterialTheme.colorScheme.background
   val colorTexto = MaterialTheme.colorScheme.onBackground
   val colorTitulo = MaterialTheme.colorScheme.primary

    val fondoTarjetaVerde = MaterialTheme.colorScheme.surfaceVariant
    val fondoTarjetaBeige = MaterialTheme.colorScheme.secondaryContainer
    val colorTituloBeige = MaterialTheme.colorScheme.secondary

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(fondo)
            .verticalScroll(rememberScrollState())
    ) {

        // Encabezado
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(verde)
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "CORPALIV",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            IconButton(onClick = onMenuClick) {
                Text(
                    text = "☰",
                    color = Color.White,
                    fontSize = 28.sp,
                    modifier = Modifier.semantics {
                        contentDescription = "Abrir menú principal"
                    }
                )
            }
        }

        // Contenido
        Column(
            modifier = Modifier.padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            Text(
                text = "Quiénes somos",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = colorTitulo
            )
            Text(
                text = "Acompañamos a personas con discapacidad múltiple y sus familias.",
                color = colorTexto
            )


            // Quienes somos
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = fondoTarjetaVerde
                ),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp)
                ) {
                    Text(
                        text = "¿Quiénes somos?",
                        fontWeight = FontWeight.Bold,
                        color = colorTitulo,
                        fontSize = 18.sp
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "CORPALIV, Corporación de Padres y Amigos por el Limitado Visual, " +
                                "es una organización sin fines de lucro fundada en 1993. " +
                                "Surgió de familias que buscaban oportunidades educativas " +
                                "adecuadas para sus hijos e hijas.",
                        color = colorTexto
                    )
                }
            }

            // Mision
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = fondoTarjetaBeige
                ),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp)
                ) {
                    Text(
                        text = "Nuestra misión",
                        fontWeight = FontWeight.Bold,
                        color = colorTituloBeige,
                        fontSize = 18.sp
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Acompañar durante toda la vida a niños, niñas, jóvenes y adultos " +
                                "con discapacidad múltiple, mediante una atención integral " +
                                "que considere sus necesidades y diferencias.",
                        color = colorTexto
                    )
                }
            }

            Text(
                text = "Nuestra visión",
                style = MaterialTheme.typography.titleLarge,
                color = colorTitulo
            )

            Text(
                text = "Contribuir a un país sin barreras para las personas " +
                        "con discapacidad múltiple.",
                color = colorTexto
            )

            Text(
                text = "Nuestros valores",
                style = MaterialTheme.typography.titleLarge,
                color = colorTitulo
            )

            Text(
                text = "Compromiso · Respeto · Empatía · Solidaridad",
                color = colorTexto
            )

            // Programas
            Text(
                text = "Nuestros programas",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = colorTitulo
            )

            ProgramaCard(
                titulo = "Escuela Jan Van Dijk",
                descripcion = "Educación especializada para niños, niñas y jóvenes " +
                        "con discapacidad múltiple o sordoceguera. Promueve la " +
                        "comunicación, la autonomía y el aprendizaje junto a sus familias."
            )

            ProgramaCard(
                titulo = "Alba Lab",
                descripcion = "Red de inclusión sociolaboral para jóvenes y adultos " +
                        "con discapacidad múltiple. Desarrolla habilidades personales " +
                        "y laborales mediante formación, talleres y oportunidades de participación."
            )
        }
    }
}