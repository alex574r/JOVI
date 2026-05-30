package com.example.jovi.ui.screens.settings

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
fun SettingsScreen(
    onBack: () -> Unit,
    onLogout: () -> Unit,
    onChangePassword: () -> Unit = {},
    onNotificationPrefs: () -> Unit = {},
    onPrivacy: () -> Unit = {},
    onHelp: () -> Unit = {},
) {
    var notificationsEnabled by remember { mutableStateOf(true) }
    var profilePublic by remember { mutableStateOf(true) }
    var darkMode by remember { mutableStateOf(false) }

    Scaffold(
        topBar = { JoviTopBar(title = "Ajustes", onBack = onBack) },
        containerColor = BackgroundColor,
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState()),
        ) {
            SectionLabel("Perfil y cuenta")
            SettingsArrowRow(icon = Icons.Outlined.Lock, title = "Cambiar contraseña", subtitle = "Actualiza tu contraseña de acceso", onClick = onChangePassword)
            SettingsArrowRow(icon = Icons.Outlined.Verified, title = "Verificacion biometrica", subtitle = "Facial o huella dactilar", onClick = {})

            SectionLabel("Preferencias")
            SettingsToggleRow(icon = Icons.Outlined.Notifications, title = "Notificaciones push", subtitle = "Recibir alertas en tiempo real", checked = notificationsEnabled, onCheckedChange = { notificationsEnabled = it })
            SettingsArrowRow(icon = Icons.Outlined.NotificationsNone, title = "Preferencias de notificacion", subtitle = "Que tipo de alertas recibir", onClick = onNotificationPrefs)
            SettingsToggleRow(icon = Icons.Outlined.DarkMode, title = "Modo oscuro", subtitle = "Proximamente disponible", checked = darkMode, onCheckedChange = { darkMode = it })

            SectionLabel("Privacidad y seguridad")
            SettingsToggleRow(icon = Icons.Outlined.VisibilityOff, title = "Perfil publico", subtitle = "Visible para todos los usuarios", checked = profilePublic, onCheckedChange = { profilePublic = it })
            SettingsArrowRow(icon = Icons.Outlined.Security, title = "Privacidad", subtitle = "Controla quien ve tu informacion", onClick = onPrivacy)
            SettingsArrowRow(icon = Icons.Outlined.Download, title = "Descargar mis datos", subtitle = "Exportar informacion de mi cuenta", onClick = {})

            SectionLabel("Soporte")
            SettingsArrowRow(icon = Icons.Outlined.HelpOutline, title = "Centro de ayuda", subtitle = "Preguntas frecuentes y soporte", onClick = onHelp)
            SettingsArrowRow(icon = Icons.Outlined.Info, title = "Acerca de Jovi", subtitle = "Version 1.0.1 (build 3)", onClick = {})
            SettingsArrowRow(icon = Icons.Outlined.Description, title = "Terminos y condiciones", subtitle = "Politica de uso", onClick = {})

            Spacer(Modifier.height(24.dp))
            Surface(
                onClick = onLogout,
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                shape = RoundedCornerShape(14.dp),
                color = StatusRejected.copy(0.08f),
            ) {
                Row(modifier = Modifier.padding(16.dp), horizontalArrangement = Arrangement.spacedBy(14.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Outlined.Logout, contentDescription = null, tint = StatusRejected)
                    Text("Cerrar sesion", style = MaterialTheme.typography.bodyMedium, color = StatusRejected, fontWeight = FontWeight.SemiBold)
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
        modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp),
    )
}

@Composable
private fun SettingsToggleRow(icon: ImageVector, title: String, subtitle: String, checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.spacedBy(14.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(icon, contentDescription = null, tint = PrimaryDark, modifier = Modifier.size(22.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(title, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Medium)
            Text(subtitle, style = MaterialTheme.typography.bodySmall, color = TextSecondary)
        }
        Switch(checked = checked, onCheckedChange = onCheckedChange, colors = SwitchDefaults.colors(checkedThumbColor = BackgroundColor, checkedTrackColor = PrimaryDark))
    }
    HorizontalDivider(color = DividerColor, modifier = Modifier.padding(horizontal = 16.dp))
}

@Composable
private fun SettingsArrowRow(icon: ImageVector, title: String, subtitle: String, onClick: () -> Unit) {
    Surface(onClick = onClick, color = BackgroundColor) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 14.dp),
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(icon, contentDescription = null, tint = PrimaryDark, modifier = Modifier.size(22.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(title, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Medium)
                Text(subtitle, style = MaterialTheme.typography.bodySmall, color = TextSecondary)
            }
            Icon(Icons.Outlined.ChevronRight, contentDescription = null, tint = TertiaryColor)
        }
    }
    HorizontalDivider(color = DividerColor, modifier = Modifier.padding(horizontal = 16.dp))
}
