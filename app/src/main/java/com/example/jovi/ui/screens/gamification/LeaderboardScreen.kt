package com.example.jovi.ui.screens.gamification

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Whatshot
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.jovi.ui.components.*
import com.example.jovi.ui.theme.*

private data class LeaderboardEntry(
    val rank: Int,
    val name: String,
    val university: String,
    val streak: Int,
    val isMe: Boolean = false,
)

private val leaderboard = listOf(
    LeaderboardEntry(1, "Sarah Jen...", "—", 212),
    LeaderboardEntry(2, "Alex Chen", "—", 184),
    LeaderboardEntry(3, "Marco Ro...", "—", 156),
    LeaderboardEntry(4, "Elena Gilbert", "Stanford University", 142),
    LeaderboardEntry(5, "David Okafor", "MIT", 128),
    LeaderboardEntry(6, "Sofia Martinez", "UCL London", 115),
    LeaderboardEntry(7, "James Wilson", "Unassigned", 98),
)

@Composable
fun LeaderboardScreen(
    onBack: () -> Unit,
    currentUserRank: Int = 42,
    currentUserStreak: Int = 45,
) {
    Scaffold(
        topBar = {
            JoviTopBar(
                title = "Streak Leaderboard",
                onBack = onBack,
                actions = {
                    IconButton(onClick = {}) {
                        Icon(Icons.Outlined.Share, contentDescription = "Compartir", tint = TextPrimary)
                    }
                }
            )
        },
        bottomBar = {
            Surface(shadowElevation = 8.dp, color = PrimaryColor) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(SecondaryColor.copy(0.3f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("$currentUserRank", style = MaterialTheme.typography.labelLarge, color = BackgroundColor, fontWeight = FontWeight.Bold)
                    }
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Tú (Daniel)", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold, color = SecondaryColor)
                        Text("¡Sigue así! Estás en el top 5%", style = MaterialTheme.typography.labelSmall, color = SecondaryColor.copy(0.8f))
                    }
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        Icon(Icons.Default.Whatshot, contentDescription = null, tint = SecondaryColor, modifier = Modifier.size(16.dp))
                        Text("$currentUserStreak", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold, color = SecondaryColor)
                    }
                }
            }
        },
        containerColor = BackgroundColor,
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {
            item {
                // Podium - top 3
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.Bottom
                ) {
                    PodiumItem(leaderboard[1], podiumHeight = 100.dp, modifier = Modifier.weight(1f))
                    PodiumItem(leaderboard[0], podiumHeight = 130.dp, modifier = Modifier.weight(1f), isFirst = true)
                    PodiumItem(leaderboard[2], podiumHeight = 80.dp, modifier = Modifier.weight(1f))
                }
            }

            item {
                Text(
                    "TOP PERFORMERS",
                    style = MaterialTheme.typography.labelSmall,
                    color = TextSecondary,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp)
                )
            }

            itemsIndexed(leaderboard.drop(3)) { index, entry ->
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 4.dp),
                    shape = RoundedCornerShape(14.dp),
                    color = SurfaceColor
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            "${index + 4}",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = TextSecondary,
                            modifier = Modifier.width(24.dp)
                        )
                        ProfileAvatar(entry.name.take(2), size = 40.dp)
                        Column(modifier = Modifier.weight(1f)) {
                            Text(entry.name, style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold)
                            if (entry.university.isNotEmpty() && entry.university != "—") {
                                Text(entry.university, style = MaterialTheme.typography.labelSmall, color = TextSecondary)
                            }
                        }
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            Icon(Icons.Default.Whatshot, contentDescription = null, tint = PrimaryDark, modifier = Modifier.size(14.dp))
                            Text("${entry.streak}", style = MaterialTheme.typography.labelLarge, color = PrimaryDark)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun PodiumItem(
    entry: LeaderboardEntry,
    podiumHeight: androidx.compose.ui.unit.Dp,
    modifier: Modifier = Modifier,
    isFirst: Boolean = false,
) {
    val medalColor = when (entry.rank) {
        1 -> PrimaryColor
        2 -> Color(0xFFB0BEC5)
        else -> Color(0xFFCD7F32)
    }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Box(contentAlignment = Alignment.BottomEnd) {
            ProfileAvatar(
                initials = entry.name.take(2),
                size = if (isFirst) 64.dp else 52.dp,
                bgColor = PrimaryLight
            )
            Box(
                modifier = Modifier
                    .size(20.dp)
                    .clip(CircleShape)
                    .background(medalColor),
                contentAlignment = Alignment.Center
            ) {
                Text("${entry.rank}", style = MaterialTheme.typography.labelSmall, color = if (entry.rank == 1) SecondaryColor else BackgroundColor, fontWeight = FontWeight.Bold)
            }
        }
        Text(entry.name, style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.SemiBold)
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(2.dp)) {
            Icon(Icons.Default.Whatshot, contentDescription = null, tint = PrimaryDark, modifier = Modifier.size(12.dp))
            Text("${entry.streak}", style = MaterialTheme.typography.labelSmall, color = PrimaryDark)
        }
    }
}
