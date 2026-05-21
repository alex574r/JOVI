package com.example.jovi.ui.screens.discovery

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Tune
import androidx.compose.material.icons.outlined.Visibility
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

private data class CandidateCard(
    val name: String,
    val age: Int,
    val title: String,
    val experience: String,
    val skills: List<String>,
)

private val sampleCandidates = listOf(
    CandidateCard("Sarah Jenkins", 28, "Senior Product Designer", "6 años exp.", listOf("Figma", "Prototyping", "User Research", "Agile")),
    CandidateCard("Alex Rivera", 24, "UI/UX Designer", "3 años exp.", listOf("Sketch", "Adobe XD", "CSS", "React")),
    CandidateCard("Marco López", 30, "Product Manager", "7 años exp.", listOf("Roadmapping", "Scrum", "Analytics")),
)

@Composable
fun CandidateDiscoveryScreen(
    onMatch: () -> Unit,
    onViewProfile: () -> Unit,
) {
    var currentIndex by remember { mutableIntStateOf(0) }
    val candidate = sampleCandidates.getOrNull(currentIndex % sampleCandidates.size) ?: sampleCandidates.first()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundColor)
    ) {
        Spacer(Modifier.height(16.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Outlined.Tune, contentDescription = null, tint = SecondaryColor)
            Text("Descubrir Talento", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Icon(Icons.Outlined.Notifications, contentDescription = null, tint = SecondaryColor)
        }

        Spacer(Modifier.height(20.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(horizontal = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth(0.92f)
                    .fillMaxHeight(0.85f)
                    .offset(y = (-16).dp),
                shape = RoundedCornerShape(24.dp),
                color = SurfaceColor,
                shadowElevation = 2.dp
            ) {}

            SwipeCard(
                onSwipeLeft = { currentIndex++ },
                onSwipeRight = { currentIndex++; onMatch() },
                modifier = Modifier.fillMaxWidth().fillMaxHeight(0.90f)
            ) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    shape = RoundedCornerShape(24.dp),
                    color = SecondaryColor,
                    shadowElevation = 8.dp
                ) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        // Gradient overlay at bottom
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .fillMaxHeight(0.5f)
                                .align(Alignment.BottomCenter)
                                .background(
                                    Brush.verticalGradient(
                                        colors = listOf(
                                            SecondaryColor.copy(alpha = 0f),
                                            SecondaryColor.copy(alpha = 0.9f),
                                            SecondaryColor
                                        )
                                    )
                                )
                        )

                        // Background avatar placeholder
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(RoundedCornerShape(24.dp))
                                .background(PrimaryLight.copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            ProfileAvatar(candidate.name.take(2), size = 120.dp, bgColor = PrimaryLight, textColor = SecondaryColor)
                        }

                        // Exp badge
                        Surface(
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(16.dp),
                            shape = RoundedCornerShape(50),
                            color = PrimaryColor
                        ) {
                            Text(
                                candidate.experience,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                style = MaterialTheme.typography.labelSmall,
                                color = SecondaryColor,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        // Bottom info
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .align(Alignment.BottomCenter)
                                .padding(20.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Text(
                                "${candidate.name}, ${candidate.age}",
                                style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.Bold,
                                color = BackgroundColor
                            )
                            Text(candidate.title, style = MaterialTheme.typography.bodyLarge, color = BackgroundColor.copy(alpha = 0.85f))

                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                Icon(Icons.Outlined.Tune, contentDescription = null, tint = PrimaryColor, modifier = Modifier.size(14.dp))
                                Text("HABILIDADES CLAVE", style = MaterialTheme.typography.labelSmall, color = PrimaryColor, fontWeight = FontWeight.Bold)
                            }

                            FlowRow(horizontalArrangement = Arrangement.spacedBy(6.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                candidate.skills.take(4).forEach { skill ->
                                    Surface(shape = RoundedCornerShape(50), color = BackgroundColor.copy(alpha = 0.15f)) {
                                        Text(
                                            skill,
                                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                            style = MaterialTheme.typography.labelSmall,
                                            color = BackgroundColor
                                        )
                                    }
                                }
                                if (candidate.skills.size > 4) {
                                    Surface(shape = RoundedCornerShape(50), color = BackgroundColor.copy(alpha = 0.15f)) {
                                        Text(
                                            "+${candidate.skills.size - 4}",
                                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                            style = MaterialTheme.typography.labelSmall,
                                            color = BackgroundColor
                                        )
                                    }
                                }
                            }

                            TextButton(onClick = onViewProfile) {
                                Icon(Icons.Outlined.Visibility, contentDescription = null, tint = BackgroundColor.copy(alpha = 0.7f), modifier = Modifier.size(14.dp))
                                Spacer(Modifier.width(4.dp))
                                Text("Ver Perfil", color = BackgroundColor.copy(alpha = 0.7f), style = MaterialTheme.typography.labelMedium)
                            }

                            SwipeActionButtons(
                                onDislike = { currentIndex++ },
                                onUndo = { if (currentIndex > 0) currentIndex-- },
                                onSave = {},
                                onLike = { currentIndex++; onMatch() }
                            )
                        }
                    }
                }
            }
        }
        Spacer(Modifier.height(8.dp))
    }
}
