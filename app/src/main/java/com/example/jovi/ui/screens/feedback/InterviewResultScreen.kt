package com.example.jovi.ui.screens.feedback

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.jovi.ui.components.*
import com.example.jovi.ui.theme.*

@Composable
fun InterviewResultScreen(
    companyName: String = "Tech Solutions",
    overallRating: Float = 4.8f,
    technicalScore: Float = 0.95f,
    cultureScore: Float = 0.85f,
    feedback: String = "Excelente desempeño en la entrevista técnica y gran encaje con la cultura del equipo. Su proactividad y conocimiento de React superaron nuestras expectativas. ¡Bienvenido a bordo!",
    onBack: () -> Unit,
    onContinue: () -> Unit,
) {
    Scaffold(
        topBar = { JoviTopBar(title = "Resultado de la Entrevista", onBack = onBack) },
        containerColor = BackgroundColor,
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Icon(
                Icons.Outlined.Celebration,
                contentDescription = null,
                tint = PrimaryDark,
                modifier = Modifier.size(48.dp)
            )

            Text(
                "¡Felicidades, has sido seleccionado!",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            Text(
                "Tu perfil encaja perfectamente con el equipo de $companyName.",
                style = MaterialTheme.typography.bodyLarge,
                color = TextSecondary,
                textAlign = TextAlign.Center
            )

            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                color = SurfaceColor
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "EVALUACIÓN",
                            style = MaterialTheme.typography.labelSmall,
                            color = TextSecondary,
                            fontWeight = FontWeight.Bold
                        )
                        Surface(shape = RoundedCornerShape(8.dp), color = PrimaryColor) {
                            Text(
                                "★ $overallRating",
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                style = MaterialTheme.typography.labelMedium,
                                color = SecondaryColor,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                    ResultBar(label = "Capacidad Técnica", value = technicalScore)
                    ResultBar(label = "Cultura", value = cultureScore)
                }
            }

            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                color = SurfaceColor
            ) {
                Text(
                    "\"$feedback\"",
                    modifier = Modifier.padding(20.dp),
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextPrimary,
                    fontStyle = FontStyle.Italic
                )
            }

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    "Próximos Pasos",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                NextStepRow(label = "Confirmación de Aceptación", done = true)
                NextStepRow(label = "Proceso de Onboarding", done = false, isCurrent = true)
            }

            JoviPrimaryButton(
                text = "Continuar al Onboarding",
                onClick = onContinue,
                trailingIcon = Icons.Default.CheckCircle,
            )

            Spacer(Modifier.height(16.dp))
        }
    }
}

@Composable
private fun ResultBar(label: String, value: Float) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(label, style = MaterialTheme.typography.bodyMedium)
            Text("${(value * 100).toInt()}%", style = MaterialTheme.typography.labelMedium, color = PrimaryDark)
        }
        LinearProgressIndicator(
            progress = { value },
            modifier = Modifier.fillMaxWidth().height(6.dp).clip(RoundedCornerShape(50)),
            color = PrimaryColor,
            trackColor = TertiaryColor,
        )
    }
}

@Composable
private fun NextStepRow(label: String, done: Boolean, isCurrent: Boolean = false) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Icon(
            if (done) Icons.Filled.CheckCircle else Icons.Outlined.RadioButtonUnchecked,
            contentDescription = null,
            tint = if (done) PrimaryDark else if (isCurrent) PrimaryColor else TertiaryColor,
            modifier = Modifier.size(20.dp)
        )
        Text(
            label,
            style = MaterialTheme.typography.bodyMedium,
            color = if (done || isCurrent) TextPrimary else TextSecondary
        )
    }
}
