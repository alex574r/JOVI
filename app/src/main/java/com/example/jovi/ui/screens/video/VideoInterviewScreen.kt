package com.example.jovi.ui.screens.video

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.jovi.ui.components.*
import com.example.jovi.ui.theme.*

@Composable
fun VideoInterviewScreen(
    contactName: String = "Sarah Jenkins",
    callDuration: String = "14:22",
    onEndCall: () -> Unit,
) {
    var isMuted by remember { mutableStateOf(false) }
    var isCameraOff by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1A1A2E))
    ) {
        // Main video area (contact)
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            ProfileAvatar(
                initials = contactName.take(2),
                size = 120.dp,
                bgColor = PrimaryLight.copy(alpha = 0.3f),
                textColor = BackgroundColor
            )
        }

        // Top info
        Column(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(20.dp)
        ) {
            Text(contactName, style = MaterialTheme.typography.titleLarge, color = BackgroundColor, fontWeight = FontWeight.Bold)
            Text(callDuration, style = MaterialTheme.typography.bodyMedium, color = BackgroundColor.copy(0.7f))
        }

        // Self preview (PiP)
        Surface(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(20.dp)
                .size(width = 90.dp, height = 120.dp),
            shape = RoundedCornerShape(12.dp),
            color = Color(0xFF2C2C3E)
        ) {
            Box(contentAlignment = Alignment.Center) {
                ProfileAvatar(initials = "TU", size = 40.dp, bgColor = PrimaryLight.copy(0.3f), textColor = BackgroundColor)
            }
        }

        // CV floating badge
        Surface(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(end = 16.dp),
            shape = RoundedCornerShape(8.dp),
            color = PrimaryColor
        ) {
            Column(
                modifier = Modifier.padding(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(Icons.Outlined.Description, contentDescription = null, tint = SecondaryColor, modifier = Modifier.size(20.dp))
                Text("CV", style = MaterialTheme.typography.labelSmall, color = SecondaryColor, fontWeight = FontWeight.Bold)
            }
        }

        // Bottom controls
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter),
            color = Color(0xFF1A1A2E).copy(alpha = 0.9f)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 20.dp, horizontal = 24.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                VideoControlButton(
                    icon = if (isMuted) Icons.Filled.MicOff else Icons.Outlined.Mic,
                    label = "Silenciar",
                    onClick = { isMuted = !isMuted },
                    isActive = isMuted
                )
                VideoControlButton(
                    icon = if (isCameraOff) Icons.Filled.VideocamOff else Icons.Outlined.Videocam,
                    label = "Cámara",
                    onClick = { isCameraOff = !isCameraOff },
                    isActive = isCameraOff
                )
                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .clip(CircleShape)
                        .background(ErrorColor),
                    contentAlignment = Alignment.Center
                ) {
                    IconButton(onClick = onEndCall) {
                        Icon(Icons.Default.CallEnd, contentDescription = "Colgar", tint = BackgroundColor, modifier = Modifier.size(28.dp))
                    }
                }
                VideoControlButton(icon = Icons.Outlined.ScreenShare, label = "Compartir", onClick = {})
                VideoControlButton(icon = Icons.Default.MoreHoriz, label = "Más", onClick = {})
            }
        }
    }
}

@Composable
private fun VideoControlButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    onClick: () -> Unit,
    isActive: Boolean = false,
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Surface(
            onClick = onClick,
            shape = CircleShape,
            color = if (isActive) BackgroundColor.copy(0.3f) else BackgroundColor.copy(0.15f),
            modifier = Modifier.size(48.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(icon, contentDescription = label, tint = BackgroundColor, modifier = Modifier.size(22.dp))
            }
        }
        Text(label, style = MaterialTheme.typography.labelSmall, color = BackgroundColor.copy(0.7f))
    }
}
