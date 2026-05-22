package com.example.jovi.ui.screens.profile

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.jovi.ui.components.JoviTopBar
import com.example.jovi.ui.components.ProfileAvatar
import com.example.jovi.ui.theme.*

private data class SavedPost(val id: Int, val type: String, val author: String, val initials: String, val content: String, val timeAgo: String)

@Composable
fun SavedPostsScreen(onBack: () -> Unit, onOpenPost: (Int) -> Unit = {}) {
    var selectedFilter by remember { mutableIntStateOf(0) }
    val filters = listOf("Todos", "Threads", "Reels")

    val allSaved = listOf(
        SavedPost(1, "Thread", "Stanford University", "SU", "Abrimos convocatoria para el programa de investigacion de verano 2025. Plazas limitadas para estudiantes de ingenieria.", "hace 1d"),
        SavedPost(2, "Reel", "Ana Garcia", "AG", "Redisene este landing page en 2 horas. Antes vs despues.", "hace 2d"),
        SavedPost(3, "Thread", "Innovatech Corp", "IC", "Estamos buscando 3 practicantes de desarrollo mobile. Stack: Kotlin + Compose.", "hace 3d"),
        SavedPost(4, "Reel", "Carlos Mendoza", "CM", "Tutorial: SwipeCard en Compose en 50 lineas", "hace 4d"),
    )

    val filtered = when (selectedFilter) {
        1 -> allSaved.filter { it.type == "Thread" }
        2 -> allSaved.filter { it.type == "Reel" }
        else -> allSaved
    }

    Scaffold(
        containerColor = BackgroundColor,
        topBar = { JoviTopBar(title = "Publicaciones Guardadas", onBack = onBack) }
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding)) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                filters.forEachIndexed { index, label ->
                    val selected = selectedFilter == index
                    FilterChip(
                        selected = selected,
                        onClick = { selectedFilter = index },
                        label = { Text(label, style = MaterialTheme.typography.labelMedium) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = PrimaryDark,
                            selectedLabelColor = BackgroundColor,
                        ),
                        border = FilterChipDefaults.filterChipBorder(
                            enabled = true,
                            selected = selected,
                            borderColor = TertiaryColor,
                            selectedBorderColor = PrimaryDark,
                        )
                    )
                }
            }

            AnimatedContent(
                targetState = filtered,
                transitionSpec = { fadeIn(tween(200)).togetherWith(fadeOut(tween(200))) },
                label = "saved_filter"
            ) { posts ->
                if (posts.isEmpty()) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(Icons.Outlined.BookmarkBorder, contentDescription = null, tint = TertiaryColor, modifier = Modifier.size(56.dp))
                            Spacer(Modifier.height(12.dp))
                            Text("Sin guardados", style = MaterialTheme.typography.titleSmall, color = TextSecondary)
                        }
                    }
                } else {
                    LazyColumn(contentPadding = PaddingValues(bottom = 24.dp)) {
                        items(posts, key = { it.id }) { post ->
                            SavedPostRow(post, onClick = { onOpenPost(post.id) })
                            HorizontalDivider(color = DividerColor)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SavedPostRow(post: SavedPost, onClick: () -> Unit) {
    Surface(onClick = onClick, color = BackgroundColor) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 14.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            ProfileAvatar(post.initials, size = 42.dp)
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                    Text(post.author, style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.SemiBold)
                    Text(post.timeAgo, style = MaterialTheme.typography.labelSmall, color = TextSecondary)
                }
                Text(post.content, style = MaterialTheme.typography.bodySmall, color = TextSecondary, maxLines = 2)
            }
            Box(
                modifier = Modifier.background(PrimaryLight, RoundedCornerShape(6.dp)).padding(4.dp),
            ) {
                Icon(
                    if (post.type == "Reel") Icons.Outlined.PlayCircleOutline else Icons.Outlined.Article,
                    contentDescription = null, tint = PrimaryDark, modifier = Modifier.size(14.dp)
                )
            }
        }
    }
}
