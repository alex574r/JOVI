package com.example.jovi.ui.screens.recruiter

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.jovi.ui.components.ProfileAvatar
import com.example.jovi.ui.theme.*

private data class RecruiterConversation(
    val id: Int,
    val candidateName: String,
    val initials: String,
    val lastMessage: String,
    val timeAgo: String,
    val unread: Boolean,
    val stage: String,
)

@Composable
fun RecruiterMessagesContent(onOpenChat: (Long) -> Unit) {
    val conversations = listOf(
        RecruiterConversation(1, "Carlos Mendoza", "CM", "Perfecto, nos vemos el martes a las 10am", "15m", false, "Entrevista"),
        RecruiterConversation(2, "Ana Garcia", "AG", "Muchas gracias por la oportunidad!", "1h", true, "Seleccionada"),
        RecruiterConversation(3, "Luis Torres", "LT", "Me interesa la posicion de data analyst", "2h", true, "Aplicante"),
        RecruiterConversation(4, "Sofia Ramirez", "SR", "Adjunto mi portafolio actualizado", "1d", false, "Revisión"),
        RecruiterConversation(5, "Miguel Lopez", "ML", "Confirmo disponibilidad para la entrevista", "2d", false, "Entrevista"),
    )

    Column(modifier = Modifier.fillMaxSize().background(BackgroundColor)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(BackgroundColor)
                .statusBarsPadding()
                .padding(horizontal = 20.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text("Mensajes", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold, color = PrimaryDark)
            IconButton(onClick = {}) {
                Icon(Icons.Outlined.Edit, contentDescription = "Nuevo", tint = TextPrimary)
            }
        }

        LazyColumn(contentPadding = PaddingValues(bottom = 24.dp)) {
            items(conversations.size) { index ->
                val conv = conversations[index]
                RecruiterConversationRow(conv = conv, onClick = { onOpenChat(conv.id.toLong()) })
                HorizontalDivider(color = DividerColor, modifier = Modifier.padding(start = 76.dp))
            }
        }
    }
}

@Composable
private fun RecruiterConversationRow(conv: RecruiterConversation, onClick: () -> Unit) {
    Surface(onClick = onClick, color = if (conv.unread) PrimaryLight.copy(0.35f) else BackgroundColor) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box {
                ProfileAvatar(conv.initials, size = 50.dp)
                if (conv.unread) {
                    Box(modifier = Modifier.align(Alignment.TopEnd).size(12.dp).clip(CircleShape).background(PrimaryDark))
                }
            }
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(3.dp)) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(conv.candidateName, style = MaterialTheme.typography.labelLarge, fontWeight = if (conv.unread) FontWeight.Bold else FontWeight.Normal)
                    Text(conv.timeAgo, style = MaterialTheme.typography.labelSmall, color = if (conv.unread) PrimaryDark else TextSecondary)
                }
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Text(conv.lastMessage, style = MaterialTheme.typography.bodySmall, color = TextSecondary, maxLines = 1, overflow = TextOverflow.Ellipsis, modifier = Modifier.weight(1f))
                    Surface(shape = RoundedCornerShape(50), color = SurfaceColor, modifier = Modifier.padding(start = 8.dp)) {
                        Text(conv.stage, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp), style = MaterialTheme.typography.labelSmall, color = PrimaryDark)
                    }
                }
            }
        }
    }
}
