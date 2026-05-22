package com.example.jovi.ui.screens.chat

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.jovi.data.db.entity.ConversationEntity
import com.example.jovi.ui.components.JoviTopBar
import com.example.jovi.ui.components.ProfileAvatar
import com.example.jovi.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun ChatListScreen(
    conversations: List<ConversationEntity>,
    currentUserId: Long = 1L,
    onOpenChat: (String) -> Unit,
    onBack: () -> Unit,
) {
    Scaffold(
        containerColor = BackgroundColor,
        topBar = {
            JoviTopBar(
                title = "Mensajes",
                onBack = onBack,
                actions = {
                    IconButton(onClick = {}) {
                        Icon(Icons.Outlined.Edit, contentDescription = "Nuevo mensaje", tint = TextPrimary)
                    }
                }
            )
        }
    ) { padding ->
        if (conversations.isEmpty()) {
            EmptyMessages(modifier = Modifier.fillMaxSize().padding(padding))
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentPadding = PaddingValues(bottom = 16.dp),
            ) {
                items(conversations, key = { it.id }) { conv ->
                    val otherName = if (conv.userId1 == currentUserId) conv.user2Name else conv.user1Name
                    val otherInitials = if (conv.userId1 == currentUserId) conv.user2Initials else conv.user1Initials
                    ConversationRow(
                        conversation = conv,
                        otherName = otherName,
                        otherInitials = otherInitials,
                        isUnread = conv.unreadCount > 0,
                        onClick = { onOpenChat(otherName) }
                    )
                    HorizontalDivider(color = DividerColor, modifier = Modifier.padding(start = 74.dp))
                }
            }
        }
    }
}

@Composable
private fun ConversationRow(
    conversation: ConversationEntity,
    otherName: String,
    otherInitials: String,
    isUnread: Boolean,
    onClick: () -> Unit,
) {
    Surface(
        onClick = onClick,
        color = if (isUnread) PrimaryLight.copy(0.4f) else BackgroundColor,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box {
                ProfileAvatar(otherInitials, size = 50.dp)
                if (isUnread) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .size(14.dp)
                            .clip(CircleShape)
                            .background(PrimaryDark)
                    )
                }
            }

            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(3.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        otherName,
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = if (isUnread) FontWeight.Bold else FontWeight.Normal,
                    )
                    Text(
                        formatTime(conversation.lastMessageAt),
                        style = MaterialTheme.typography.labelSmall,
                        color = if (isUnread) PrimaryDark else TextSecondary,
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        conversation.lastMessage,
                        style = MaterialTheme.typography.bodySmall,
                        color = if (isUnread) TextPrimary else TextSecondary,
                        fontWeight = if (isUnread) FontWeight.Medium else FontWeight.Normal,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f),
                    )
                    if (conversation.unreadCount > 0) {
                        Box(
                            modifier = Modifier
                                .padding(start = 8.dp)
                                .size(20.dp)
                                .clip(CircleShape)
                                .background(PrimaryDark),
                            contentAlignment = Alignment.Center,
                        ) {
                            Text(
                                "${conversation.unreadCount}",
                                style = MaterialTheme.typography.labelSmall,
                                color = BackgroundColor,
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun EmptyMessages(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Icon(
            Icons.Outlined.ChatBubbleOutline,
            contentDescription = null,
            tint = TertiaryColor,
            modifier = Modifier.size(64.dp),
        )
        Spacer(Modifier.height(16.dp))
        Text("Sin mensajes aún", style = MaterialTheme.typography.titleMedium, color = TextSecondary)
        Text("Cuando hagas match podrás chatear aquí", style = MaterialTheme.typography.bodySmall, color = TextSecondary)
    }
}

private fun formatTime(millis: Long): String {
    val now = System.currentTimeMillis()
    val diff = now - millis
    return when {
        diff < 60_000 -> "Ahora"
        diff < 3_600_000 -> "${diff / 60_000}m"
        diff < 86_400_000 -> "${diff / 3_600_000}h"
        else -> SimpleDateFormat("dd/MM", Locale.getDefault()).format(Date(millis))
    }
}
