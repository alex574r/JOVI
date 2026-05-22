package com.example.jovi.ui.screens.notifications

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.jovi.ui.components.*
import com.example.jovi.ui.theme.*

private data class NotifItem(
    val title: String,
    val body: String,
    val initials: String,
    val type: String,
    val timeAgo: String,
    val isRead: Boolean,
)

private val mockNotifications = listOf(
    NotifItem("Nuevo Match", "Innovatech Corp hizo match contigo para Mobile Developer", "IC", "MATCH", "hace 5m", false),
    NotifItem("Le gusto tu publicacion", "Ana Garcia dio like a tu post sobre Kotlin Multiplatform", "AG", "LIKE", "hace 1h", false),
    NotifItem("Nuevo mensaje", "Tienes un mensaje de Innovatech Corp", "IC", "MESSAGE", "hace 2h", true),
    NotifItem("Nuevo seguidor", "Luis Torres ahora te sigue", "LT", "FOLLOW", "hace 3h", true),
    NotifItem("Actualizacion de solicitud", "Stanford University reviso tu aplicacion", "SU", "APPLICATION", "ayer", true),
    NotifItem("Recordatorio", "Tienes una entrevista manana a las 10:00 AM con Google", "G", "SYSTEM", "hace 1d", true),
)

@Composable
fun NotificationsScreen(onBack: () -> Unit) {
    Scaffold(
        topBar = {
            JoviTopBar(
                title = "Notificaciones",
                onBack = onBack,
                actions = {
                    TextButton(onClick = {}) {
                        Text("Leer todo", color = PrimaryDark, style = MaterialTheme.typography.labelMedium)
                    }
                }
            )
        },
        containerColor = BackgroundColor,
    ) { padding ->
        if (mockNotifications.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Icon(Icons.Outlined.Notifications, contentDescription = null, tint = TertiaryColor, modifier = Modifier.size(64.dp))
                    Text("Sin notificaciones", style = MaterialTheme.typography.titleMedium, color = TextSecondary)
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentPadding = PaddingValues(vertical = 8.dp)
            ) {
                items(mockNotifications) { notif ->
                    NotificationRow(notif)
                    HorizontalDivider(color = DividerColor, modifier = Modifier.padding(horizontal = 16.dp))
                }
            }
        }
    }
}

@Composable
private fun NotificationRow(notif: NotifItem) {
    val bgColor = if (!notif.isRead) PrimaryLight else BackgroundColor
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(bgColor)
            .padding(horizontal = 16.dp, vertical = 14.dp),
        horizontalArrangement = Arrangement.spacedBy(14.dp),
        verticalAlignment = Alignment.Top
    ) {
        Box(modifier = Modifier.size(44.dp).clip(CircleShape).background(SecondaryColor), contentAlignment = Alignment.Center) {
            Text(notif.initials, color = PrimaryColor, style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold)
        }
        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(notif.title, style = MaterialTheme.typography.labelLarge, fontWeight = if (!notif.isRead) FontWeight.Bold else FontWeight.Normal)
                Text(notif.timeAgo, style = MaterialTheme.typography.labelSmall, color = TextSecondary)
            }
            Text(notif.body, style = MaterialTheme.typography.bodySmall, color = TextSecondary)
        }
        if (!notif.isRead) {
            Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(PrimaryDark).align(Alignment.CenterVertically))
        }
    }
}
