package com.example.jovi.ui.screens.discovery

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Tune
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

private data class InternshipCard(
    val title: String,
    val company: String,
    val isUniversityVerified: Boolean,
    val postedAgo: String,
    val education: String,
    val stipend: String,
    val duration: String,
    val location: String,
    val description: String,
    val isTopMatch: Boolean,
)

private val sampleInternships = listOf(
    InternshipCard(
        "Junior Marketing Intern", "TechNova Solutions", true, "hace 2d",
        "Lic. Marketing", "\$2,000/mes", "3 Meses", "Remoto / Híbrido",
        "Únete a nuestro equipo dinámico para asistir en el desarrollo de estrategias de marketing, contenido...",
        true
    ),
    InternshipCard(
        "Data Science Intern", "Analytics Co.", true, "hace 5d",
        "Ing. Sistemas", "\$2,500/mes", "6 Meses", "Presencial",
        "Colabora con el equipo de datos en proyectos de machine learning y visualización de datos...",
        false
    ),
)

@Composable
fun InternshipDiscoveryScreen(
    onMatch: () -> Unit,
    currentUserName: String = "Sarah Jenkins",
) {
    var currentIndex by remember { mutableIntStateOf(0) }
    val internship = sampleInternships.getOrNull(currentIndex % sampleInternships.size) ?: sampleInternships.first()

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
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                ProfileAvatar(initials = currentUserName.take(2), size = 44.dp)
                Text(currentUserName, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            }
            Icon(Icons.Default.Tune, contentDescription = null, tint = SecondaryColor)
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
                    color = BackgroundColor,
                    shadowElevation = 8.dp
                ) {
                    Column(modifier = Modifier.fillMaxSize()) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(150.dp)
                                .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                                .background(PrimaryLight),
                            contentAlignment = Alignment.Center
                        ) {
                            ProfileAvatar(internship.company.take(2), size = 56.dp)
                            Row(
                                modifier = Modifier
                                    .align(Alignment.TopStart)
                                    .padding(12.dp),
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Surface(shape = RoundedCornerShape(50), color = PrimaryColor) {
                                    Text(
                                        "Prácticas",
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                        style = MaterialTheme.typography.labelSmall,
                                        color = SecondaryColor,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                                if (internship.isTopMatch) {
                                    Surface(shape = RoundedCornerShape(50), color = StatusPending) {
                                        Row(
                                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                                        ) {
                                            Icon(Icons.Default.Star, contentDescription = null, modifier = Modifier.size(12.dp), tint = SecondaryColor)
                                            Text("Top Match", style = MaterialTheme.typography.labelSmall, color = SecondaryColor)
                                        }
                                    }
                                }
                            }
                        }

                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(20.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Text(internship.title, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)

                            if (internship.isUniversityVerified) {
                                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                    Icon(Icons.Outlined.Verified, contentDescription = null, tint = PrimaryDark, modifier = Modifier.size(14.dp))
                                    Text("Verificado Universidad • ${internship.postedAgo}", style = MaterialTheme.typography.bodySmall, color = TextSecondary)
                                }
                            }

                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                InternInfoTile(icon = Icons.Outlined.School, label = "EDUCACIÓN", value = internship.education)
                                InternInfoTile(icon = Icons.Outlined.AttachMoney, label = "ESTIPENDIO", value = internship.stipend)
                            }
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                InternInfoTile(icon = Icons.Outlined.Schedule, label = "DURACIÓN", value = internship.duration)
                                InternInfoTile(icon = Icons.Outlined.LocationOn, label = "UBICACIÓN", value = internship.location)
                            }

                            Text(internship.description, style = MaterialTheme.typography.bodyMedium, color = TextSecondary, maxLines = 3)

                            Spacer(Modifier.weight(1f))

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

@Composable
private fun InternInfoTile(icon: androidx.compose.ui.graphics.vector.ImageVector, label: String, value: String) {
    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            Icon(icon, contentDescription = null, tint = PrimaryDark, modifier = Modifier.size(14.dp))
            Text(label, style = MaterialTheme.typography.labelSmall, color = TextSecondary, fontWeight = FontWeight.Bold)
        }
        Text(value, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold)
    }
}
