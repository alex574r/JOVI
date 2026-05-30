package com.example.jovi.ui.screens.settings

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.jovi.ui.components.JoviTopBar
import com.example.jovi.ui.theme.*

@Composable
fun NotificationPrefsScreen(onBack: () -> Unit) {
    var pushEnabled by remember { mutableStateOf(true) }
    var newMatch by remember { mutableStateOf(true) }
    var newMessage by remember { mutableStateOf(true) }
    var newLike by remember { mutableStateOf(false) }
    var newComment by remember { mutableStateOf(true) }
    var interviewReminder by remember { mutableStateOf(true) }
    var weeklyReport by remember { mutableStateOf(false) }
    var streakAlert by remember { mutableStateOf(true) }
    var vacancyRecommendations by remember { mutableStateOf(true) }
    var emailSummary by remember { mutableStateOf(false) }

    Scaffold(
        containerColor = BackgroundColor,
        topBar = { JoviTopBar(title = "Notificaciones", onBack = onBack) }
    ) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).verticalScroll(rememberScrollState()),
        ) {
            NotifSection(title = "General") {
                NotifToggle("Notificaciones push", "Activar o desactivar todas las notificaciones", pushEnabled) { pushEnabled = it }
            }

            Spacer(Modifier.height(8.dp))
            HorizontalDivider(thickness = 8.dp, color = SurfaceColor)
            Spacer(Modifier.height(8.dp))

            NotifSection(title = "Actividad social") {
                NotifToggle("Nuevo match", "Cuando alguien hace match contigo", newMatch) { newMatch = it }
                HorizontalDivider(color = DividerColor)
                NotifToggle("Nuevo mensaje", "Cuando recibes un mensaje", newMessage) { newMessage = it }
                HorizontalDivider(color = DividerColor)
                NotifToggle("Likes en tus publicaciones", "Cuando alguien da like a tu contenido", newLike) { newLike = it }
                HorizontalDivider(color = DividerColor)
                NotifToggle("Comentarios", "Cuando alguien comenta en tu publicacion", newComment) { newComment = it }
            }

            Spacer(Modifier.height(8.dp))
            HorizontalDivider(thickness = 8.dp, color = SurfaceColor)
            Spacer(Modifier.height(8.dp))

            NotifSection(title = "Proceso de contratacion") {
                NotifToggle("Recordatorio de entrevista", "Aviso 30 min antes de una entrevista", interviewReminder) { interviewReminder = it }
                HorizontalDivider(color = DividerColor)
                NotifToggle("Alerta de racha", "Cuando llevas 3+ horas sin actividad", streakAlert) { streakAlert = it }
                HorizontalDivider(color = DividerColor)
                NotifToggle("Vacantes recomendadas", "Nuevas vacantes que coinciden con tu perfil", vacancyRecommendations) { vacancyRecommendations = it }
            }

            Spacer(Modifier.height(8.dp))
            HorizontalDivider(thickness = 8.dp, color = SurfaceColor)
            Spacer(Modifier.height(8.dp))

            NotifSection(title = "Correo electronico") {
                NotifToggle("Resumen semanal", "Resumen de actividad cada lunes por correo", emailSummary) { emailSummary = it }
                HorizontalDivider(color = DividerColor)
                NotifToggle("Informe mensual", "Reporte de progreso mensual", weeklyReport) { weeklyReport = it }
            }

            Spacer(Modifier.height(24.dp))
        }
    }
}

@Composable
private fun NotifSection(title: String, content: @Composable ColumnScope.() -> Unit) {
    Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp)) {
        Text(title, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold, color = TextSecondary, modifier = Modifier.padding(bottom = 6.dp))
        content()
    }
}

@Composable
private fun NotifToggle(title: String, subtitle: String, checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(modifier = Modifier.weight(1f).padding(end = 12.dp)) {
            Text(title, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Medium)
            Text(subtitle, style = MaterialTheme.typography.bodySmall, color = TextSecondary)
        }
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(checkedThumbColor = BackgroundColor, checkedTrackColor = PrimaryDark),
        )
    }
}
