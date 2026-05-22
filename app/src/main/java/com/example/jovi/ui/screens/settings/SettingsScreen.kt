package com.example.jovi.ui.screens.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.jovi.ui.components.*
import com.example.jovi.ui.theme.*

@Composable
fun SettingsScreen(onBack: () -> Unit, onLogout: () -> Unit) {
    var notificationsEnabled by remember { mutableStateOf(true) }
    var matchNotifications by remember { mutableStateOf(true) }
    var messageNotifications by remember { mutableStateOf(true) }
    var profilePublic by remember { mutableStateOf(true) }

    Scaffold(
        topBar = { JoviTopBar(title = "Configuracion", onBack = onBack) },
        containerColor = BackgroundColor,
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
        ) {
            SectionLabel("Notificaciones")
            SettingsToggleRow(icon = Icons.Outlined.Notifications, title = "Notificaciones", subtitle = "Activar todas las notificaciones", checked = notificationsEnabled, onCheckedChange = { notificationsEnabled = it })
            SettingsToggleRow(icon = Icons.Outlined.Favorite, title = "Nuevos matches", subtitle = "Notificar cuando haya un match", checked = matchNotifications, onCheckedChange = { matchNotifications = it })
            SettingsToggleRow(icon = Icons.Outlined.Message, title = "Mensajes", subtitle = "Notificar nuevos mensajes", checked = messageNotifications, onCheckedChange = { messageNotifications = it })

            SectionLabel("Privacidad")
            SettingsToggleRow(icon = Icons.Outlined.VisibilityOff, title = "Perfil publico", subtitle = "Visible para todos los usuarios", checked = profilePublic, onCheckedChange = { profilePublic = it })
            SettingsArrowRow(icon = Icons.Outlined.Security, title = "Seguridad de la cuenta", subtitle = "Contrasena y verificacion", onClick = {})
            SettingsArrowRow(icon = Icons.Outlined.Download, title = "Descargar mis datos", subtitle = "Exportar informacion de mi cuenta", onClick = {})

            SectionLabel("Cuenta")
            SettingsArrowRow(icon = Icons.Outlined.Help, title = "Centro de ayuda", subtitle = "Preguntas frecuentes y soporte", onClick = {})
            SettingsArrowRow(icon = Icons.Outlined.Info, title = "Acerca de Jovi", subtitle = "Version 1.0.0", onClick = {})

            Spacer(Modifier.height(24.dp))
            Surface(
                onClick = onLogout,
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                shape = RoundedCornerShape(14.dp),
                color = SurfaceColor,
            ) {
                Row(modifier = Modifier.padding(16.dp), horizontalArrangement = Arrangement.spacedBy(14.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Outlined.Logout, contentDescription = null, tint = MaterialTheme.colorScheme.error)
                    Text("Cerrar sesion", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.error, fontWeight = FontWeight.SemiBold)
                }
            }
            Spacer(Modifier.height(32.dp))
        }
    }
}

@Composable
private fun SectionLabel(title: String) {
    Text(
        title.uppercase(),
        style = MaterialTheme.typography.labelSmall,
        color = TextSecondary,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp)
    )
}

@Composable
private fun SettingsToggleRow(icon: ImageVector, title: String, subtitle: String, checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.spacedBy(14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = null, tint = SecondaryColor, modifier = Modifier.size(22.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(title, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Medium)
            Text(subtitle, style = MaterialTheme.typography.bodySmall, color = TextSecondary)
        }
        Switch(checked = checked, onCheckedChange = onCheckedChange, colors = SwitchDefaults.colors(checkedThumbColor = SecondaryColor, checkedTrackColor = PrimaryColor))
    }
    HorizontalDivider(color = DividerColor, modifier = Modifier.padding(horizontal = 16.dp))
}

@Composable
private fun SettingsArrowRow(icon: ImageVector, title: String, subtitle: String, onClick: () -> Unit) {
    Surface(onClick = onClick, color = BackgroundColor) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 14.dp),
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(icon, contentDescription = null, tint = SecondaryColor, modifier = Modifier.size(22.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(title, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Medium)
                Text(subtitle, style = MaterialTheme.typography.bodySmall, color = TextSecondary)
            }
            Icon(Icons.Outlined.ChevronRight, contentDescription = null, tint = TertiaryColor)
        }
    }
    HorizontalDivider(color = DividerColor, modifier = Modifier.padding(horizontal = 16.dp))
}
