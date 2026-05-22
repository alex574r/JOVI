package com.example.jovi.ui.screens.notifications

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.jovi.data.db.entity.NotificationEntity
import com.example.jovi.ui.components.*
import com.example.jovi.ui.theme.*

private data class NotifDisplay(
    val id: Long,
    val title: String,
    val body: String,
    val initials: String,
    val timeAgo: String,
    val isRead: Boolean,
)

private fun formatNotifTime(timestamp: Long): String {
    val diff = System.currentTimeMillis() - timestamp
    return when {
        diff < 60_000L -> "ahora"
        diff < 3_600_000L -> "hace ${diff / 60_000}m"
        diff < 86_400_000L -> "hace ${diff / 3_600_000}h"
        diff < 172_800_000L -> "ayer"
        else -> "hace ${diff / 86_400_000}d"
    }
}

private fun NotificationEntity.toDisplay() = NotifDisplay(
    id = id,
    title = title,
    body = body,
    initials = senderInitials.ifBlank { title.take(2).uppercase() },
    timeAgo = formatNotifTime(timestamp),
    isRead = isRead,
)

private val mockNotifications = listOf(
    NotifDisplay(1L, "Nuevo Match", "Innovatech Corp hizo match contigo para Mobile Developer", "IC", "hace 5m", false),
    NotifDisplay(2L, "Le gusto tu publicacion", "Ana Garcia dio like a tu post sobre Kotlin Multiplatform", "AG", "hace 1h", false),
    NotifDisplay(3L, "Nuevo mensaje", "Tienes un mensaje de Innovatech Corp", "IC", "hace 2h", true),
    NotifDisplay(4L, "Nuevo seguidor", "Luis Torres ahora te sigue", "LT", "hace 3h", true),
    NotifDisplay(5L, "Actualizacion de solicitud", "Stanford University reviso tu aplicacion", "SU", "ayer", true),
    NotifDisplay(6L, "Recordatorio", "Tienes una entrevista manana a las 10:00 AM con Google", "G", "hace 1d", true),
)

@Composable
fun NotificationsScreen(
    notifications: List<NotificationEntity> = emptyList(),
    onMarkRead: (Long) -> Unit = {},
    onMarkAllRead: () -> Unit = {},
    onBack: () -> Unit,
) {
    val displayItems = remember(notifications) {
        if (notifications.isEmpty()) mockNotifications else notifications.map { it.toDisplay() }
    }

    Scaffold(
        topBar = {
            JoviTopBar(
                title = "Notificaciones",
                onBack = onBack,
                actions = {
                    TextButton(onClick = onMarkAllRead) {
                        Text("Leer todo", color = PrimaryDark, style = MaterialTheme.typography.labelMedium)
                    }
                },
            )
        },
        containerColor = BackgroundColor,
    ) { padding ->
        if (displayItems.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    Icon(Icons.Outlined.Notifications, contentDescription = null, tint = TertiaryColor, modifier = Modifier.size(64.dp))
                    Text("Sin notificaciones", style = MaterialTheme.typography.titleMedium, color = TextSecondary)
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentPadding = PaddingValues(vertical = 8.dp),
            ) {
                items(displayItems, key = { it.id }) { notif ->
                    NotificationRow(notif = notif, onTap = { if (!notif.isRead) onMarkRead(notif.id) })
                    HorizontalDivider(color = DividerColor, modifier = Modifier.padding(horizontal = 16.dp))
                }
            }
        }
    }
}

@Composable
private fun NotificationRow(notif: NotifDisplay, onTap: () -> Unit) {
    val bgColor = if (!notif.isRead) PrimaryLight else BackgroundColor
    Surface(onClick = onTap, color = bgColor) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            verticalAlignment = Alignment.Top,
        ) {
            Box(
                modifier = Modifier.size(44.dp).clip(CircleShape).background(SecondaryColor),
                contentAlignment = Alignment.Center,
            ) {
                Text(notif.initials, color = PrimaryColor, style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold)
            }
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(
                        notif.title,
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = if (!notif.isRead) FontWeight.Bold else FontWeight.Normal,
                    )
                    Text(notif.timeAgo, style = MaterialTheme.typography.labelSmall, color = TextSecondary)
                }
                Text(notif.body, style = MaterialTheme.typography.bodySmall, color = TextSecondary)
            }
            if (!notif.isRead) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(PrimaryDark)
                        .align(Alignment.CenterVertically),
                )
            }
        }
    }
}
