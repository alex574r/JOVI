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
import com.example.jovi.ui.components.*
import com.example.jovi.ui.theme.*

private sealed class ChatMessage {
    data class Text(val id: Int, val text: String, val isMe: Boolean, val time: String, val read: Boolean = false) : ChatMessage()
    data class ConfirmedInterview(val id: Int, val date: String, val time: String) : ChatMessage()
    data class FileAttachment(val id: Int, val fileName: String, val size: String, val time: String, val isMe: Boolean) : ChatMessage()
}

private val sampleMessages = listOf(
    ChatMessage.Text(1, "Hola Alex, revisamos tu portafolio y quedamos muy impresionados. ¡Confirmemos la entrevista para este jueves!", false, "10:24 AM"),
    ChatMessage.ConfirmedInterview(2, "Oct 24, 2023", "10:00 AM"),
    ChatMessage.Text(3, "¡Perfecto! Recibí la confirmación y el horario me viene genial. También adjunté mi CV más reciente.", true, "10:25 AM"),
    ChatMessage.Text(4, "Hola Alex, revisamos tu portafolio y quedamos muy impresionados con tus casos de estudio. ¿Podemos agendar una llamada rápida?", false, "10:24 AM"),
    ChatMessage.Text(5, "¡Hola! Muchas gracias por el feedback. Sí, definitivamente estoy disponible este jueves por la tarde.", true, "10:26 AM", true),
    ChatMessage.Text(6, "Aquí está mi CV actualizado para tu referencia.", true, "10:27 AM", true),
    ChatMessage.FileAttachment(7, "CV_Alex_Dev.pdf", "1.2 MB", "10:27 AM", true),
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    contactName: String = "Global Tech",
    onBack: () -> Unit,
    onScheduleInterview: () -> Unit,
    onVideoCall: () -> Unit,
) {
    var messageText by remember { mutableStateOf("") }
    var showScheduleSheet by remember { mutableStateOf(false) }
    val listState = rememberLazyListState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        ProfileAvatar(initials = contactName.take(2), size = 38.dp)
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
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    IconButton(onClick = {}) {
                        Icon(Icons.Outlined.AttachFile, contentDescription = null, tint = TextSecondary)
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
                    IconButton(onClick = {}) {
                        Icon(Icons.Outlined.EmojiEmotions, contentDescription = null, tint = TextSecondary)
                    }
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(PrimaryColor),
                        contentAlignment = Alignment.Center
                    ) {
                        IconButton(onClick = {}) {
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
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp),
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
            items(sampleMessages) { msg ->
                when (msg) {
                    is ChatMessage.Text -> ChatBubble(msg)
                    is ChatMessage.ConfirmedInterview -> InterviewCard(msg, onJoin = onVideoCall)
                    is ChatMessage.FileAttachment -> FileAttachmentBubble(msg)
                }
            }
        }
    }

    if (showScheduleSheet) {
        ScheduleInterviewBottomSheet(
            onDismiss = { showScheduleSheet = false },
            onConfirm = { showScheduleSheet = false; onScheduleInterview() }
        )
    }
}

@Composable
private fun ChatBubble(msg: ChatMessage.Text) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (msg.isMe) Arrangement.End else Arrangement.Start,
        verticalAlignment = Alignment.Bottom
    ) {
        if (!msg.isMe) {
            ProfileAvatar(initials = "GT", size = 28.dp, modifier = Modifier.padding(end = 6.dp))
        }
        Column(
            horizontalAlignment = if (msg.isMe) Alignment.End else Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Surface(
                shape = RoundedCornerShape(
                    topStart = 16.dp, topEnd = 16.dp,
                    bottomStart = if (msg.isMe) 16.dp else 4.dp,
                    bottomEnd = if (msg.isMe) 4.dp else 16.dp
                ),
                color = if (msg.isMe) PrimaryColor else SurfaceColor
            ) {
                Text(
                    msg.text,
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (msg.isMe) SecondaryColor else TextPrimary
                )
            }
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(msg.time, style = MaterialTheme.typography.labelSmall, color = TextSecondary)
                if (msg.isMe && msg.read) {
                    Icon(Icons.Default.DoneAll, contentDescription = null, modifier = Modifier.size(12.dp), tint = PrimaryDark)
                }
            }
        }
        if (msg.isMe) {
            ProfileAvatar(initials = "TU", size = 28.dp, modifier = Modifier.padding(start = 6.dp))
        }
    }
}

@Composable
private fun InterviewCard(msg: ChatMessage.ConfirmedInterview, onJoin: () -> Unit) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = BackgroundColor,
        border = androidx.compose.foundation.BorderStroke(1.5.dp, PrimaryColor)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(Icons.Outlined.CalendarMonth, contentDescription = null, tint = PrimaryDark, modifier = Modifier.size(24.dp))
            Text("ENTREVISTA CONFIRMADA", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold, color = TextSecondary)
            Text(msg.date, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = PrimaryDark)
            Text(msg.time, style = MaterialTheme.typography.bodyMedium, color = TextSecondary)
            JoviPrimaryButton(text = "Unirse a la Reunión", onClick = onJoin)
            TextButton(onClick = {}) {
                Text("Añadir al Calendario", color = PrimaryDark)
            }
        }
    }
}

@Composable
private fun FileAttachmentBubble(msg: ChatMessage.FileAttachment) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (msg.isMe) Arrangement.End else Arrangement.Start,
        verticalAlignment = Alignment.Bottom
    ) {
        if (!msg.isMe) ProfileAvatar(initials = "GT", size = 28.dp, modifier = Modifier.padding(end = 6.dp))
        Surface(
            shape = RoundedCornerShape(12.dp),
            color = if (msg.isMe) PrimaryColor else SurfaceColor
        ) {
            Row(
                modifier = Modifier.padding(12.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(shape = RoundedCornerShape(8.dp), color = ErrorColor.copy(0.15f)) {
                    Icon(Icons.Outlined.PictureAsPdf, contentDescription = null, tint = ErrorColor, modifier = Modifier.padding(8.dp).size(20.dp))
                }
                Column {
                    Text(msg.fileName, style = MaterialTheme.typography.labelMedium, color = if (msg.isMe) SecondaryColor else TextPrimary)
                    Text(msg.size, style = MaterialTheme.typography.labelSmall, color = if (msg.isMe) SecondaryColor.copy(0.7f) else TextSecondary)
                }
                Icon(Icons.Outlined.Download, contentDescription = null, tint = if (msg.isMe) SecondaryColor else TextSecondary)
            }
        }
    }
}
