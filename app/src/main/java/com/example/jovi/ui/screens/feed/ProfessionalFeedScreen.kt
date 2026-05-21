package com.example.jovi.ui.screens.feed

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.jovi.ui.components.*
import com.example.jovi.ui.theme.*

private data class Thread(val author: String, val role: String, val time: String, val content: String, val likes: Int, val comments: Int)
private data class Reel(val author: String, val authorRole: String, val hasVideo: Boolean)

private val threads = listOf(
    Thread("TechNova Inc.", "Empresa • hace 3h", "3h", "Estamos buscando activamente a alguien que ame Figma tanto como el café. ¡Revisa el enlace en nuestro perfil para postular!", 28, 4),
    Thread("Sarah Jenkins", "Diseñadora • hace 1h", "1h", "¡El mejor programa de mentoría con el que he trabajado! Aprendí muchísimo. ¡Aplica ahora!", 12, 2),
)

@Composable
fun ProfessionalFeedScreen(
    onCreateContent: () -> Unit,
    onApply: () -> Unit,
) {
    Scaffold(
        containerColor = BackgroundColor,
        floatingActionButton = {
            FloatingActionButton(
                onClick = onCreateContent,
                containerColor = PrimaryColor,
                contentColor = SecondaryColor,
                shape = CircleShape
            ) {
                Icon(Icons.Default.Add, contentDescription = "Crear contenido")
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {
            item {
                // Reel card header
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(220.dp)
                        .background(SecondaryColor)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(
                                        SecondaryColor.copy(alpha = 0f),
                                        SecondaryColor.copy(alpha = 0.85f)
                                    )
                                )
                            )
                    )
                    Row(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Surface(shape = RoundedCornerShape(50), color = BackgroundColor.copy(0.2f)) {
                            Text("1", modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp), style = MaterialTheme.typography.labelSmall, color = BackgroundColor)
                        }
                    }
                    Column(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(16.dp)
                    ) {
                        Text("TechNova Inc.", style = MaterialTheme.typography.labelMedium, color = BackgroundColor, fontWeight = FontWeight.Bold)
                        Text(
                            "#hiring #uxdesign #workwithus — Únete a nuestro equipo...",
                            style = MaterialTheme.typography.bodySmall,
                            color = BackgroundColor.copy(0.8f),
                            maxLines = 2
                        )
                    }
                    Column(
                        modifier = Modifier
                            .align(Alignment.CenterEnd)
                            .padding(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        ReelAction(icon = Icons.Outlined.FavoriteBorder, count = "4.2K")
                        ReelAction(icon = Icons.Outlined.ChatBubbleOutline, count = "")
                        ReelAction(icon = Icons.Outlined.Share, count = "")
                    }
                }
            }

            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Threads", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                        Text("${threads.size}", style = MaterialTheme.typography.bodySmall, color = TextSecondary)
                        TextButton(onClick = {}) {
                            Text("Ver todo", color = PrimaryDark)
                        }
                    }
                }
            }

            items(threads) { thread ->
                ThreadCard(thread, onApply = onApply)
            }
        }
    }
}

@Composable
private fun ThreadCard(thread: Thread, onApply: () -> Unit) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp),
        shape = RoundedCornerShape(16.dp),
        color = SurfaceColor
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                ProfileAvatar(thread.author.take(2), size = 36.dp)
                Column(modifier = Modifier.weight(1f)) {
                    Text(thread.author, style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold)
                    Text(thread.role, style = MaterialTheme.typography.labelSmall, color = TextSecondary)
                }
            }
            Text(thread.content, style = MaterialTheme.typography.bodyMedium, color = TextPrimary)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        Icon(Icons.Outlined.FavoriteBorder, contentDescription = null, modifier = Modifier.size(14.dp), tint = TextSecondary)
                        Text("${thread.likes}", style = MaterialTheme.typography.labelSmall, color = TextSecondary)
                    }
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        Icon(Icons.Outlined.ChatBubbleOutline, contentDescription = null, modifier = Modifier.size(14.dp), tint = TextSecondary)
                        Text("${thread.comments} Responder", style = MaterialTheme.typography.labelSmall, color = TextSecondary)
                    }
                }
                if (thread.author != "Sarah Jenkins") {
                    Surface(onClick = onApply, shape = RoundedCornerShape(50), color = PrimaryColor) {
                        Text(
                            "Aplicar →",
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            style = MaterialTheme.typography.labelMedium,
                            color = SecondaryColor
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ReelAction(icon: androidx.compose.ui.graphics.vector.ImageVector, count: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(2.dp)) {
        Icon(icon, contentDescription = null, tint = BackgroundColor, modifier = Modifier.size(24.dp))
        if (count.isNotEmpty()) Text(count, style = MaterialTheme.typography.labelSmall, color = BackgroundColor)
    }
}
