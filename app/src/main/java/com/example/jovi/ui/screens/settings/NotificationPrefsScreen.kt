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
import com.example.jovi.viewmodel.SettingsViewModel

@Composable
fun NotificationPrefsScreen(
    settingsViewModel: SettingsViewModel,
    onBack: () -> Unit,
) {
    val settings by settingsViewModel.settings.collectAsState()

    Scaffold(
        topBar = { JoviTopBar(title = "Notificaciones", onBack = onBack) },
        containerColor = BackgroundColor,
    ) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).verticalScroll(rememberScrollState()).padding(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text("Elige qué notificaciones recibir", style = MaterialTheme.typography.bodyMedium, color = TextSecondary)
            Spacer(Modifier.height(8.dp))
            NotifToggle("Matches", "Cuando una empresa hace match contigo", settings?.matchNotifications ?: true) {
                settingsViewModel.toggleMatchNotifications()
            }
            NotifToggle("Mensajes", "Nuevos mensajes en tus conversaciones", settings?.messageNotifications ?: true) {
                settingsViewModel.toggleMessageNotifications()
            }
            NotifToggle("Todas las notificaciones", "Activar o desactivar todo", settings?.notificationsEnabled ?: true) {
                settingsViewModel.toggleNotifications()
            }
        }
    }
}

@Composable
private fun NotifToggle(title: String, subtitle: String, checked: Boolean, onToggle: () -> Unit) {
    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
        Column(modifier = Modifier.weight(1f)) {
            Text(title, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Medium)
            Text(subtitle, style = MaterialTheme.typography.bodySmall, color = TextSecondary)
        }
        Switch(checked = checked, onCheckedChange = { onToggle() }, colors = SwitchDefaults.colors(checkedThumbColor = BackgroundColor, checkedTrackColor = PrimaryDark))
    }
    HorizontalDivider(color = DividerColor)
}
