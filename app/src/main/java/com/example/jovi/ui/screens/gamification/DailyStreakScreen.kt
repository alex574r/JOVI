package com.example.jovi.ui.screens.gamification

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Help
import androidx.compose.material.icons.filled.RocketLaunch
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.filled.Whatshot
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.jovi.ui.components.*
import com.example.jovi.ui.theme.*

private val weekDays = listOf("L", "M", "X", "J", "V", "S", "D")
private val activeUntil = 4

private data class Reward(val title: String, val subtitle: String, val icon: androidx.compose.ui.graphics.vector.ImageVector)
private val rewards = listOf(
    Reward("Boost de Perfil", "ACTIVO", Icons.Default.RocketLaunch),
    Reward("Candidato Top", "ACTIVO", Icons.Default.Verified),
)

@Composable
fun DailyStreakScreen(
    streakDays: Int = 15,
    onBack: () -> Unit,
    onContinue: () -> Unit,
) {
    val animatedProgress by animateFloatAsState(
        targetValue = streakDays / 30f,
        animationSpec = tween(1000),
        label = "streak_progress"
    )

    Scaffold(
        topBar = {
            JoviTopBar(
                title = "Racha Diaria",
                onBack = onBack,
                actions = {
                    IconButton(onClick = {}) {
                        Icon(Icons.Default.Help, contentDescription = "Ayuda", tint = TextPrimary)
                    }
                }
            )
        },
        containerColor = BackgroundColor,
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Box(
                modifier = Modifier.size(180.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    progress = { animatedProgress },
                    modifier = Modifier.size(180.dp),
                    color = PrimaryColor,
                    trackColor = TertiaryColor,
                    strokeWidth = 10.dp,
                    strokeCap = StrokeCap.Round,
                )
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Default.Whatshot, contentDescription = null, tint = PrimaryDark, modifier = Modifier.size(36.dp))
                    Text("$streakDays", style = MaterialTheme.typography.displayLarge, fontWeight = FontWeight.Bold)
                    Text("Días", style = MaterialTheme.typography.bodyLarge, color = TextSecondary)
                }
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text("¡Mantén el ritmo!", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                Row {
                    Text("Mañana alcanzarás el nivel ", style = MaterialTheme.typography.bodyMedium, color = TextSecondary)
                    Text("Experto", style = MaterialTheme.typography.bodyMedium, color = PrimaryDark, fontWeight = FontWeight.Bold)
                }
            }

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text("Tu semana", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    weekDays.forEach { day ->
                        Text(day, style = MaterialTheme.typography.labelSmall, color = TextSecondary)
                    }
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    weekDays.forEachIndexed { index, _ ->
                        val isActive = index < activeUntil
                        Box(
                            modifier = Modifier
                                .size(38.dp)
                                .clip(CircleShape)
                                .background(if (isActive) PrimaryColor else TertiaryColor.copy(0.5f)),
                            contentAlignment = Alignment.Center
                        ) {
                            if (isActive) {
                                Icon(Icons.Default.Whatshot, contentDescription = null, tint = SecondaryColor, modifier = Modifier.size(18.dp))
                            }
                        }
                    }
                }
            }

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                SectionHeader(title = "Recompensas actuales", action = "Ver todas", onActionClick = {})
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    rewards.forEach { reward ->
                        Surface(
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(16.dp),
                            color = PrimaryLight
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(40.dp)
                                        .clip(CircleShape)
                                        .background(PrimaryColor),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(reward.icon, contentDescription = null, tint = SecondaryColor, modifier = Modifier.size(20.dp))
                                }
                                Text(reward.title, style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold)
                                Text(reward.subtitle, style = MaterialTheme.typography.labelSmall, color = PrimaryDark)
                            }
                        }
                    }
                }
            }

            JoviPrimaryButton(
                text = "Continuar swiping",
                onClick = onContinue,
                trailingIcon = Icons.Default.Whatshot,
            )

            Spacer(Modifier.height(8.dp))
        }
    }
}
