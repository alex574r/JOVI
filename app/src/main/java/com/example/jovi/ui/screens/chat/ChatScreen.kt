package com.example.jovi.ui.screens.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.jovi.data.db.entity.MessageEntity
import com.example.jovi.data.db.entity.MessageStatus
import com.example.jovi.data.db.entity.MessageType
import com.example.jovi.ui.components.*
import com.example.jovi.ui.theme.*
import com.example.jovi.viewmodel.AuthViewModel
import com.example.jovi.viewmodel.ChatViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    conversationId: Long,
    contactName: String,
    contactInitials: String,
    chatViewModel: ChatViewModel,
    authViewModel: AuthViewModel,
    onBack: () -> Unit,
    onScheduleInterview: () -> Unit = {},
    onVideoCall: () -> Unit = {},
) {
    val currentUser by authViewModel.currentUser.collectAsState()
    val messages by chatViewModel.messages.collectAsState()
    var messageText by remember { mutableStateOf("") }
    var showDocDialog by remember { mutableStateOf(false) }
    val listState = rememberLazyListState()

    LaunchedEffect(conversationId) {
        chatViewModel.loadMessages(conversationId)
        chatViewModel.markAsRead(conversationId)
    }

    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) listState.animateScrollToItem(messages.size - 1)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        ProfileAvatar(initials = contactInitials, size = 38.dp)
                        Column {
                            Text(contactName, style = MaterialTheme.typography.titleMedium)
                            Text("EN LÍNEA", style = MaterialTheme.typography.labelSmall, color = PrimaryDark)
                        }
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBackIosNew, contentDescription = "Atrás", tint = TextPrimary)
                    }
                },
                actions = {
                    IconButton(onClick = onVideoCall) {
                        Icon(Icons.Outlined.Call, contentDescription = "Llamar", tint = TextPrimary)
                    }
                    IconButton(onClick = {}) {
                        Icon(Icons.Default.MoreVert, contentDescription = "Más", tint = TextPrimary)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = BackgroundColor)
            )
        },
        bottomBar = {
            Surface(shadowElevation = 4.dp, color = BackgroundColor) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    IconButton(onClick = { showDocDialog = true }) {
                        Icon(Icons.Outlined.AttachFile, contentDescription = "Adjuntar", tint = TextSecondary)
                    }
                    OutlinedTextField(
                        value = messageText,
                        onValueChange = { messageText = it },
                        placeholder = { Text("Mensaje...", color = TextSecondary) },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(50),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = TertiaryColor,
                            unfocusedBorderColor = TertiaryColor,
                        ),
                        singleLine = true,
                    )
                    Box(
                        modifier = Modifier.size(40.dp).clip(CircleShape).background(PrimaryColor),
                        contentAlignment = Alignment.Center
                    ) {
                        IconButton(onClick = {
                            val u = currentUser ?: return@IconButton
                            if (messageText.isNotBlank()) {
                                chatViewModel.sendMessage(conversationId, u.id, u.displayName, messageText)
                                messageText = ""
                            }
                        }) {
                            Icon(Icons.Default.Send, contentDescription = "Enviar", tint = SecondaryColor, modifier = Modifier.size(18.dp))
                        }
                    }
                }
            }
        },
        containerColor = BackgroundColor,
    ) { padding ->
        LazyColumn(
            state = listState,
            modifier = Modifier.fillMaxSize().padding(padding).padding(horizontal = 16.dp),
            contentPadding = PaddingValues(vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    Surface(shape = RoundedCornerShape(50), color = SurfaceColor) {
                        Text("HOY", modifier = Modifier.padding(horizontal = 14.dp, vertical = 4.dp), style = MaterialTheme.typography.labelSmall, color = TextSecondary)
                    }
                }
            }
            items(messages, key = { it.id }) { msg ->
                val isMe = msg.senderId == (currentUser?.id ?: -1L)
                when (msg.type) {
                    MessageType.FILE -> FileMessageBubble(msg, isMe, contactInitials, currentUser?.avatarInitials ?: "?")
                    else -> TextMessageBubble(msg, isMe, contactInitials, currentUser?.avatarInitials ?: "?")
                }
            }
        }
    }

    if (showDocDialog) {
        DocumentPickerDialog(
            onDismiss = { showDocDialog = false },
            onSend = { fileName, fileSize ->
                val u = currentUser ?: return@DocumentPickerDialog
                chatViewModel.sendDocument(conversationId, u.id, u.displayName, fileName, fileSize)
                showDocDialog = false
            }
        )
    }
}

@Composable
private fun TextMessageBubble(msg: MessageEntity, isMe: Boolean, contactInitials: String, myInitials: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (isMe) Arrangement.End else Arrangement.Start,
        verticalAlignment = Alignment.Bottom
    ) {
        if (!isMe) ProfileAvatar(initials = contactInitials, size = 28.dp, modifier = Modifier.padding(end = 6.dp))
        Column(
            horizontalAlignment = if (isMe) Alignment.End else Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Surface(
                shape = RoundedCornerShape(
                    topStart = 16.dp, topEnd = 16.dp,
                    bottomStart = if (isMe) 16.dp else 4.dp,
                    bottomEnd = if (isMe) 4.dp else 16.dp
                ),
                color = if (isMe) PrimaryColor else SurfaceColor
            ) {
                Text(
                    msg.content,
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (isMe) SecondaryColor else TextPrimary
                )
            }
            Row(horizontalArrangement = Arrangement.spacedBy(4.dp), verticalAlignment = Alignment.CenterVertically) {
                Text(formatMsgTime(msg.timestamp), style = MaterialTheme.typography.labelSmall, color = TextSecondary)
                if (isMe) StatusIcon(msg.status)
            }
        }
        if (isMe) ProfileAvatar(initials = myInitials, size = 28.dp, modifier = Modifier.padding(start = 6.dp))
    }
}

@Composable
private fun FileMessageBubble(msg: MessageEntity, isMe: Boolean, contactInitials: String, myInitials: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (isMe) Arrangement.End else Arrangement.Start,
        verticalAlignment = Alignment.Bottom
    ) {
        if (!isMe) ProfileAvatar(initials = contactInitials, size = 28.dp, modifier = Modifier.padding(end = 6.dp))
        Surface(shape = RoundedCornerShape(12.dp), color = if (isMe) PrimaryColor else SurfaceColor) {
            Row(modifier = Modifier.padding(12.dp), horizontalArrangement = Arrangement.spacedBy(10.dp), verticalAlignment = Alignment.CenterVertically) {
                Surface(shape = RoundedCornerShape(8.dp), color = ErrorColor.copy(0.15f)) {
                    Icon(Icons.Outlined.PictureAsPdf, contentDescription = null, tint = ErrorColor, modifier = Modifier.padding(8.dp).size(20.dp))
                }
                Column {
                    Text(msg.fileName ?: msg.content, style = MaterialTheme.typography.labelMedium, color = if (isMe) SecondaryColor else TextPrimary)
                    Text(msg.fileSize ?: "", style = MaterialTheme.typography.labelSmall, color = if (isMe) SecondaryColor.copy(0.7f) else TextSecondary)
                }
                Icon(Icons.Outlined.Download, contentDescription = null, tint = if (isMe) SecondaryColor else TextSecondary)
            }
        }
        if (isMe) ProfileAvatar(initials = myInitials, size = 28.dp, modifier = Modifier.padding(start = 6.dp))
    }
}

@Composable
private fun StatusIcon(status: MessageStatus) {
    when (status) {
        MessageStatus.SENT -> Icon(Icons.Default.Done, contentDescription = null, modifier = Modifier.size(12.dp), tint = TextSecondary)
        MessageStatus.DELIVERED -> Icon(Icons.Default.DoneAll, contentDescription = null, modifier = Modifier.size(12.dp), tint = TextSecondary)
        MessageStatus.READ -> Icon(Icons.Default.DoneAll, contentDescription = null, modifier = Modifier.size(12.dp), tint = PrimaryDark)
    }
}

@Composable
private fun DocumentPickerDialog(onDismiss: () -> Unit, onSend: (String, String) -> Unit) {
    val docs = listOf(
        "CV_Actualizado.pdf" to "1.2 MB",
        "Portafolio_2025.pdf" to "3.4 MB",
        "Carta_Presentacion.docx" to "245 KB",
        "Transcripcion_Oficial.pdf" to "890 KB"
    )
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Seleccionar documento") },
        text = {
            Column {
                docs.forEach { (name, size) ->
                    Surface(onClick = { onSend(name, size) }, color = BackgroundColor) {
                        Row(modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp), horizontalArrangement = Arrangement.spacedBy(12.dp), verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Outlined.PictureAsPdf, contentDescription = null, tint = ErrorColor, modifier = Modifier.size(20.dp))
                            Column {
                                Text(name, style = MaterialTheme.typography.bodyMedium)
                                Text(size, style = MaterialTheme.typography.labelSmall, color = TextSecondary)
                            }
                        }
                    }
                    HorizontalDivider(color = DividerColor)
                }
            }
        },
        confirmButton = { TextButton(onClick = onDismiss) { Text("Cancelar") } }
    )
}

private fun formatMsgTime(millis: Long): String =
    SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date(millis))
