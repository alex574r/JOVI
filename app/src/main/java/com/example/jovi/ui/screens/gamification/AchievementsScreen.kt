package com.example.jovi.ui.screens.gamification

import androidx.compose.animation.core.*
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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.jovi.ui.components.JoviTopBar
import com.example.jovi.ui.theme.*

private data class Achievement(
    val id: Int,
    val title: String,
    val description: String,
    val icon: ImageVector,
    val xp: Int,
    val unlocked: Boolean,
    val progress: Float = 1f,
    val category: String,
)

@Composable
fun AchievementsScreen(onBack: () -> Unit) {
    val achievements = listOf(
        Achievement(1, "Primer Match", "Consiguiste tu primer match", Icons.Outlined.Favorite, 50, true, category = "Social"),
        Achievement(2, "Racha de 7 dias", "Mantuviste actividad 7 dias seguidos", Icons.Outlined.LocalFireDepartment, 100, true, category = "Constancia"),
        Achievement(3, "Perfil Verificado", "Completaste la verificacion biometrica", Icons.Outlined.Verified, 150, true, category = "Perfil"),
        Achievement(4, "Primer Post", "Publicaste tu primer contenido", Icons.Outlined.Edit, 75, true, category = "Contenido"),
        Achievement(5, "Racha de 30 dias", "Mantuviste actividad 30 dias seguidos", Icons.Outlined.Bolt, 300, false, progress = 0.47f, category = "Constancia"),
        Achievement(6, "100 Likes", "Tus posts recibieron 100 likes", Icons.Outlined.FavoriteBorder, 200, false, progress = 0.63f, category = "Contenido"),
        Achievement(7, "Top 10 Leaderboard", "Entraste al top 10 del ranking", Icons.Outlined.Leaderboard, 500, false, progress = 0.2f, category = "Ranking"),
        Achievement(8, "Entrevista exitosa", "Completaste tu primera entrevista", Icons.Outlined.VideoCall, 250, false, progress = 0f, category = "Proceso"),
        Achievement(9, "Contratacion", "Firmaste tu primer contrato", Icons.Outlined.Assignment, 1000, false, progress = 0f, category = "Proceso"),
    )

    val unlockedXp = achievements.filter { it.unlocked }.sumOf { it.xp }
    val totalXp = achievements.sumOf { it.xp }
    val categories = achievements.map { it.category }.distinct()
    var selectedCategory by remember { mutableStateOf("Todos") }

    val displayed = if (selectedCategory == "Todos") achievements
    else achievements.filter { it.category == selectedCategory }

    Scaffold(
        containerColor = BackgroundColor,
        topBar = { JoviTopBar(title = "Logros", onBack = onBack) }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding),
            contentPadding = PaddingValues(bottom = 24.dp),
        ) {
            item {
                Surface(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    shape = RoundedCornerShape(20.dp),
                    color = PrimaryDark,
                ) {
                    Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text("Puntos XP Totales", style = MaterialTheme.typography.labelMedium, color = BackgroundColor.copy(0.7f))
                        Text("$unlockedXp XP", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.ExtraBold, color = SecondaryColor)
                        LinearProgressIndicator(
                            progress = { unlockedXp.toFloat() / totalXp.toFloat() },
                            modifier = Modifier.fillMaxWidth().height(6.dp).clip(RoundedCornerShape(50)),
                            color = SecondaryColor,
                            trackColor = BackgroundColor.copy(0.2f),
                        )
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("${achievements.count { it.unlocked }} / ${achievements.size} desbloqueados", style = MaterialTheme.typography.labelSmall, color = BackgroundColor.copy(0.7f))
                            Text("$totalXp XP total", style = MaterialTheme.typography.labelSmall, color = BackgroundColor.copy(0.7f))
                        }
                    }
                }
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf("Todos") + categories.take(4)
                }
                androidx.compose.foundation.lazy.LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    val allFilters = listOf("Todos") + categories
                    items(allFilters.size) { i ->
                        val cat = allFilters[i]
                        val selected = selectedCategory == cat
                        FilterChip(
                            selected = selected,
                            onClick = { selectedCategory = cat },
                            label = { Text(cat, style = MaterialTheme.typography.labelMedium) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = PrimaryDark,
                                selectedLabelColor = BackgroundColor,
                            ),
                            border = FilterChipDefaults.filterChipBorder(
                                enabled = true, selected = selected,
                                borderColor = TertiaryColor, selectedBorderColor = PrimaryDark,
                            )
                        )
                    }
                }
                Spacer(Modifier.height(8.dp))
            }

            items(displayed, key = { it.id }) { achievement ->
                AchievementRow(achievement)
                HorizontalDivider(color = DividerColor, modifier = Modifier.padding(start = 76.dp))
            }
        }
    }
}

@Composable
private fun AchievementRow(achievement: Achievement) {
    val pulse by rememberInfiniteTransition(label = "pulse").animateFloat(
        initialValue = 1f, targetValue = 1.08f,
        animationSpec = infiniteRepeatable(tween(900, easing = EaseInOut), RepeatMode.Reverse),
        label = "badge_pulse"
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(if (achievement.unlocked) BackgroundColor else BackgroundColor)
            .padding(horizontal = 16.dp, vertical = 14.dp),
        horizontalArrangement = Arrangement.spacedBy(14.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .scale(if (achievement.unlocked) pulse else 1f)
                .size(50.dp)
                .clip(CircleShape)
                .background(if (achievement.unlocked) PrimaryDark else SurfaceColor),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                achievement.icon,
                contentDescription = null,
                tint = if (achievement.unlocked) SecondaryColor else TertiaryColor,
                modifier = Modifier.size(24.dp).alpha(if (achievement.unlocked) 1f else 0.4f),
            )
        }

        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(3.dp)) {
            Text(
                achievement.title,
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.SemiBold,
                color = if (achievement.unlocked) TextPrimary else TextSecondary,
            )
            Text(achievement.description, style = MaterialTheme.typography.bodySmall, color = TextSecondary)
            if (!achievement.unlocked && achievement.progress > 0f) {
                Spacer(Modifier.height(4.dp))
                LinearProgressIndicator(
                    progress = { achievement.progress },
                    modifier = Modifier.fillMaxWidth().height(4.dp).clip(RoundedCornerShape(50)),
                    color = PrimaryDark,
                    trackColor = TertiaryColor,
                )
                Text(
                    "${(achievement.progress * 100).toInt()}%",
                    style = MaterialTheme.typography.labelSmall,
                    color = TextSecondary,
                )
            }
        }

        Surface(
            shape = RoundedCornerShape(50),
            color = if (achievement.unlocked) SecondaryColor.copy(0.15f) else SurfaceColor,
        ) {
            Text(
                "+${achievement.xp} XP",
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                style = MaterialTheme.typography.labelSmall,
                color = if (achievement.unlocked) SecondaryColor else TextSecondary,
                fontWeight = FontWeight.Bold,
            )
        }
    }
}
