package com.example.jovi.ui.screens.feedback

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarOutline
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.jovi.ui.components.*
import com.example.jovi.ui.theme.*

@Composable
fun RecruiterFeedbackScreen(
    candidateName: String = "Marcus Thompson",
    candidateTitle: String = "Senior Software Engineer",
    onBack: () -> Unit,
    onSubmit: () -> Unit,
) {
    var starRating by remember { mutableIntStateOf(4) }
    var technicalSkills by remember { mutableFloatStateOf(0.8f) }
    var softSkills by remember { mutableFloatStateOf(0.9f) }
    var culturalFit by remember { mutableFloatStateOf(0.7f) }
    var observations by remember { mutableStateOf("") }
    var recommendToHire by remember { mutableStateOf(true) }

    Scaffold(
        topBar = { JoviTopBar(title = "Retroalimentación del Candidato", onBack = onBack) },
        containerColor = BackgroundColor,
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                ProfileAvatar(candidateName.take(2), size = 52.dp)
                Column {
                    Text(candidateName, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Text(candidateTitle, style = MaterialTheme.typography.bodySmall, color = TextSecondary)
                }
            }

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text("IMPRESIÓN GENERAL", style = MaterialTheme.typography.labelSmall, color = TextSecondary, fontWeight = FontWeight.Bold)
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    (1..5).forEach { i ->
                        IconButton(onClick = { starRating = i }) {
                            Icon(
                                if (i <= starRating) Icons.Default.Star else Icons.Default.StarOutline,
                                contentDescription = null,
                                tint = if (i <= starRating) PrimaryDark else TertiaryColor,
                                modifier = Modifier.size(36.dp)
                            )
                        }
                    }
                }
                Text("${starRating}.0 / 5.0 Rating", style = MaterialTheme.typography.bodySmall, color = TextSecondary)
            }

            Divider(color = DividerColor)

            SectionHeader(title = "Criterios de Evaluación")

            FeedbackSlider(
                label = "Habilidades Técnicas",
                value = technicalSkills,
                onValueChange = { technicalSkills = it },
                minLabel = "BÁSICO",
                maxLabel = "EXPERTO"
            )

            FeedbackSlider(
                label = "Habilidades Blandas",
                value = softSkills,
                onValueChange = { softSkills = it },
                minLabel = "DEFICIENTE",
                maxLabel = "EXCELENTE"
            )

            FeedbackSlider(
                label = "Ajuste Cultural",
                value = culturalFit,
                onValueChange = { culturalFit = it },
                minLabel = "DESALINEADO",
                maxLabel = "PERFECTO"
            )

            Divider(color = DividerColor)

            SectionHeader(title = "Observaciones Generales")

            JoviTextField(
                value = observations,
                onValueChange = { observations = it },
                placeholder = "Describe las fortalezas, debilidades y puntos clave del candidato...",
                singleLine = false,
                minLines = 4,
            )

            Surface(
                shape = androidx.compose.foundation.shape.RoundedCornerShape(14.dp),
                color = SurfaceColor
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        Icon(Icons.Default.ThumbUp, contentDescription = null, tint = PrimaryDark, modifier = Modifier.size(22.dp))
                        Text("Recomendación para Contratar", style = MaterialTheme.typography.labelLarge)
                    }
                    Switch(
                        checked = recommendToHire,
                        onCheckedChange = { recommendToHire = it },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = BackgroundColor,
                            checkedTrackColor = PrimaryColor,
                        )
                    )
                }
            }

            JoviPrimaryButton(text = "Enviar Evaluación", onClick = onSubmit)

            Spacer(Modifier.height(16.dp))
        }
    }
}

@Composable
private fun FeedbackSlider(
    label: String,
    value: Float,
    onValueChange: (Float) -> Unit,
    minLabel: String,
    maxLabel: String,
) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(label, style = MaterialTheme.typography.labelLarge)
            Surface(
                shape = androidx.compose.foundation.shape.RoundedCornerShape(6.dp),
                color = PrimaryColor
            ) {
                Text(
                    "${(value * 10).toInt()}/10",
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                    style = MaterialTheme.typography.labelSmall,
                    color = SecondaryColor,
                    fontWeight = FontWeight.Bold
                )
            }
        }
        Slider(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            colors = SliderDefaults.colors(
                thumbColor = PrimaryColor,
                activeTrackColor = PrimaryColor,
                inactiveTrackColor = TertiaryColor,
            )
        )
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(minLabel, style = MaterialTheme.typography.labelSmall, color = TextSecondary)
            Text(maxLabel, style = MaterialTheme.typography.labelSmall, color = TextSecondary)
        }
    }
}
