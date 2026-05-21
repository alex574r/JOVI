package com.example.jovi.ui.screens.tracking

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.Chat
import androidx.compose.material.icons.outlined.RadioButtonUnchecked
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.jovi.ui.components.*
import com.example.jovi.ui.theme.*

private data class TimelineStep(
    val title: String,
    val date: String,
    val detail: String,
    val state: StepState,
)

private enum class StepState { DONE, CURRENT, PENDING }

private val timeline = listOf(
    TimelineStep("Solicitud Enviada", "28 Sep, 2023", "Enviada exitosamente", StepState.DONE),
    TimelineStep("Perfil Revisado", "2 Oct, 2023", "El reclutador vio tu perfil", StepState.DONE),
    TimelineStep("Entrevista Agendada", "5 Oct, 2023", "Confirmado para el 12 de Oct", StepState.DONE),
    TimelineStep("Entrevista Completada", "12 Oct, 2023", "Esperando feedback", StepState.CURRENT),
    TimelineStep("Decisión Final", "Est. 20 Oct, 2023", "Fase de selección final", StepState.PENDING),
)

@Composable
fun ApplicationTrackerScreen(
    jobTitle: String = "Product Designer",
    companyName: String = "TechCorp",
    progress: Float = 0.75f,
    onBack: () -> Unit,
    onMessageRecruiter: () -> Unit,
) {
    Scaffold(
        topBar = {
            JoviTopBar(
                title = "Estado de la Solicitud",
                onBack = onBack,
                actions = {
                    IconButton(onClick = {}) {
                        Icon(Icons.Default.MoreVert, contentDescription = null, tint = TextPrimary)
                    }
                }
            )
        },
        containerColor = BackgroundColor,
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(0.dp)
        ) {
            // Header card
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .background(PrimaryLight),
                contentAlignment = Alignment.BottomStart
            ) {
                Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Surface(shape = RoundedCornerShape(6.dp), color = PrimaryColor) {
                            Text("En Progreso", modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp), style = MaterialTheme.typography.labelSmall, color = SecondaryColor, fontWeight = FontWeight.Bold)
                        }
                        Text("PRIORIDAD", style = MaterialTheme.typography.labelSmall, color = PrimaryDark, fontWeight = FontWeight.Bold)
                    }
                    Text("Próximo: Esperando Feedback", style = MaterialTheme.typography.bodySmall, color = TextSecondary)
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text("$jobTitle", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
                    Text(companyName, style = MaterialTheme.typography.bodyLarge, color = TextSecondary)
                    Text("Avance Actual: ${(progress * 100).toInt()}%", style = MaterialTheme.typography.bodySmall, color = PrimaryDark)
                    LinearProgressIndicator(
                        progress = { progress },
                        modifier = Modifier.fillMaxWidth().height(6.dp).clip(RoundedCornerShape(50)),
                        color = PrimaryColor,
                        trackColor = TertiaryColor,
                    )
                }

                Divider(color = DividerColor)

                SectionHeader(title = "Línea de Tiempo")

                timeline.forEachIndexed { index, step ->
                    TimelineItem(step, isLast = index == timeline.lastIndex)
                }

                Spacer(Modifier.height(8.dp))

                JoviPrimaryButton(
                    text = "Mensaje al Reclutador",
                    onClick = onMessageRecruiter,
                    leadingIcon = Icons.Outlined.Chat,
                )

                Spacer(Modifier.height(16.dp))
            }
        }
    }
}

@Composable
private fun TimelineItem(step: TimelineStep, isLast: Boolean) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(14.dp),
        verticalAlignment = Alignment.Top
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(
                        when (step.state) {
                            StepState.DONE -> PrimaryColor
                            StepState.CURRENT -> SecondaryColor
                            StepState.PENDING -> SurfaceColor
                        }
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    when (step.state) {
                        StepState.DONE -> Icons.Default.CheckCircle
                        StepState.CURRENT -> Icons.Outlined.Schedule
                        StepState.PENDING -> Icons.Outlined.RadioButtonUnchecked
                    },
                    contentDescription = null,
                    tint = when (step.state) {
                        StepState.DONE -> SecondaryColor
                        StepState.CURRENT -> BackgroundColor
                        StepState.PENDING -> TertiaryColor
                    },
                    modifier = Modifier.size(18.dp)
                )
            }
            if (!isLast) {
                Box(modifier = Modifier.width(2.dp).height(50.dp).background(TertiaryColor))
            }
        }

        Column(
            modifier = Modifier.weight(1f).padding(bottom = if (!isLast) 18.dp else 0.dp),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(step.title, style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.SemiBold,
                    color = if (step.state == StepState.CURRENT) PrimaryDark else TextPrimary)
                if (step.state == StepState.CURRENT) {
                    Surface(shape = RoundedCornerShape(50), color = PrimaryLight) {
                        Text("ACTUAL", modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp), style = MaterialTheme.typography.labelSmall, color = PrimaryDark)
                    }
                }
            }
            Text(step.date, style = MaterialTheme.typography.bodySmall, color = PrimaryDark)
            Text(step.detail, style = MaterialTheme.typography.bodySmall, color = TextSecondary)
        }
    }
}
