package com.example.jovi.ui.screens.settings

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.jovi.ui.components.JoviTopBar
import com.example.jovi.ui.theme.*

@Composable
fun PrivacySettingsScreen(onBack: () -> Unit) {
    var profilePublic by remember { mutableStateOf(true) }
    var showEmail by remember { mutableStateOf(false) }
    var showUniversity by remember { mutableStateOf(true) }
    var showStreak by remember { mutableStateOf(true) }
    var allowMessages by remember { mutableStateOf(true) }
    var indexableInSearch by remember { mutableStateOf(true) }
    var dataAnalytics by remember { mutableStateOf(true) }

    Scaffold(
        containerColor = BackgroundColor,
        topBar = { JoviTopBar(title = "Privacidad", onBack = onBack) }
    ) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(0.dp),
        ) {
            PrivacySection(title = "Visibilidad del perfil") {
                PrivacyToggle("Perfil público", "Cualquier usuario puede ver tu perfil", profilePublic) { profilePublic = it }
                HorizontalDivider(color = DividerColor)
                PrivacyToggle("Mostrar correo electrónico", "Visible para reclutadores con match", showEmail) { showEmail = it }
                HorizontalDivider(color = DividerColor)
                PrivacyToggle("Mostrar universidad", "Aparece en tu perfil público", showUniversity) { showUniversity = it }
                HorizontalDivider(color = DividerColor)
                PrivacyToggle("Mostrar racha", "Tu racha de actividad es visible", showStreak) { showStreak = it }
            }

            Spacer(Modifier.height(8.dp))
            HorizontalDivider(thickness = 8.dp, color = SurfaceColor)
            Spacer(Modifier.height(8.dp))

            PrivacySection(title = "Comunicación") {
                PrivacyToggle("Permitir mensajes directos", "Reclutadores pueden iniciarte conversaciones", allowMessages) { allowMessages = it }
                HorizontalDivider(color = DividerColor)
                PrivacyToggle("Aparecer en búsquedas", "Tu perfil aparece en resultados de búsqueda", indexableInSearch) { indexableInSearch = it }
            }

            Spacer(Modifier.height(8.dp))
            HorizontalDivider(thickness = 8.dp, color = SurfaceColor)
            Spacer(Modifier.height(8.dp))

            PrivacySection(title = "Datos y analíticas") {
                PrivacyToggle("Compartir datos de uso anónimos", "Ayuda a mejorar la aplicación", dataAnalytics) { dataAnalytics = it }
            }

            Spacer(Modifier.height(8.dp))
            HorizontalDivider(thickness = 8.dp, color = SurfaceColor)
            Spacer(Modifier.height(8.dp))

            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text("Datos personales", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold, color = TextSecondary, modifier = Modifier.padding(vertical = 6.dp))
                PrivacyActionRow("Descargar mis datos", Icons.Outlined.Download, onClick = {})
                HorizontalDivider(color = DividerColor)
                PrivacyActionRow("Eliminar mi cuenta", Icons.Outlined.DeleteForever, isDestructive = true, onClick = {})
            }

            Spacer(Modifier.height(24.dp))
        }
    }
}

@Composable
private fun PrivacySection(title: String, content: @Composable ColumnScope.() -> Unit) {
    Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp)) {
        Text(title, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold, color = TextSecondary, modifier = Modifier.padding(bottom = 8.dp))
        content()
    }
}

@Composable
private fun PrivacyToggle(title: String, subtitle: String, checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
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

@Composable
private fun PrivacyActionRow(label: String, icon: androidx.compose.ui.graphics.vector.ImageVector, isDestructive: Boolean = false, onClick: () -> Unit) {
    Surface(onClick = onClick, color = BackgroundColor) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(vertical = 14.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(icon, contentDescription = null, tint = if (isDestructive) StatusRejected else TextPrimary, modifier = Modifier.size(20.dp))
            Text(label, style = MaterialTheme.typography.bodyMedium, color = if (isDestructive) StatusRejected else TextPrimary, modifier = Modifier.weight(1f))
            Icon(Icons.Outlined.ChevronRight, contentDescription = null, tint = TextSecondary, modifier = Modifier.size(18.dp))
        }
    }
}
