package com.example.jovi.ui.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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

private data class Experience(val title: String, val company: String, val period: String)
private data class Education(val degree: String, val institution: String, val year: String)

@Composable
fun PublicProfileScreen(
    onBack: () -> Unit,
    onShare: () -> Unit,
    onSendMatchRequest: () -> Unit,
    onAddExperience: () -> Unit,
) {
    val experience = listOf(
        Experience("Lead Product Designer", "EcoScale", "2021 - Presente"),
        Experience("Senior UI/UX Designer", "Brightly Creative", "2018 - 2021"),
    )
    val education = listOf(
        Education("B.A. Diseño Gráfico", "Rhode Island School of Design", "2017"),
    )
    val skills = listOf("UI Design", "React", "Figma", "Project Management", "Prototyping", "User Research")

    Scaffold(
        topBar = {
            JoviTopBar(
                title = "Perfil Público",
                onBack = onBack,
                actions = {
                    IconButton(onClick = onShare) {
                        Icon(Icons.Outlined.Share, contentDescription = "Compartir", tint = TextPrimary)
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
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .background(PrimaryLight),
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
            ) {
                Box(modifier = Modifier.offset(y = (-32).dp)) {
                    ProfileAvatar(
                        initials = "SJ",
                        size = 72.dp,
                        modifier = Modifier
                            .clip(CircleShape)
                    )
                }

                Column(
                    modifier = Modifier.offset(y = (-20).dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text("Sarah Jenkins", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
                        VerifiedBadge()
                    }
                    Text("Senior Product Designer", style = MaterialTheme.typography.bodyLarge, color = TextSecondary)
                    Text(
                        "Creando experiencias digitales intuitivas por más de 8 años. Apasionada por la tecnología sostenible y sistemas de diseño centrados en humanos.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextSecondary
                    )

                    Spacer(Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        StatCard(icon = Icons.Default.Whatshot, label = "RACHA", value = "15 Días", modifier = Modifier.weight(1f))
                        StatCard(icon = Icons.Default.Star, label = "RATING", value = "4.9", modifier = Modifier.weight(1f))
                        StatCard(icon = Icons.Default.Favorite, label = "MATCH", value = "128", modifier = Modifier.weight(1f))
                    }

                    Spacer(Modifier.height(16.dp))

                    SectionHeader(title = "Experiencia", icon = Icons.Outlined.Work, action = "Agregar", onActionClick = onAddExperience)

                    experience.forEach { exp ->
                        ExperienceItem(exp)
                    }

                    Spacer(Modifier.height(12.dp))

                    SectionHeader(title = "Educación", icon = Icons.Outlined.School)

                    education.forEach { edu ->
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier.size(36.dp).clip(RoundedCornerShape(8.dp)).background(SurfaceColor),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Outlined.School, contentDescription = null, tint = PrimaryDark, modifier = Modifier.size(18.dp))
                            }
                            Column {
                                Text(edu.degree, style = MaterialTheme.typography.labelLarge)
                                Text("${edu.institution} • ${edu.year}", style = MaterialTheme.typography.bodySmall, color = TextSecondary)
                            }
                        }
                    }

                    Spacer(Modifier.height(12.dp))

                    SectionHeader(title = "Habilidades", icon = Icons.Default.Bolt)

                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        skills.forEach { SkillChip(it) }
                    }

                    Spacer(Modifier.height(20.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        JoviPrimaryButton(
                            text = "Enviar Match",
                            onClick = onSendMatchRequest,
                            leadingIcon = Icons.Default.Send,
                            modifier = Modifier.weight(1f)
                        )
                        Surface(
                            onClick = {},
                            shape = RoundedCornerShape(50),
                            color = SurfaceColor,
                            border = androidx.compose.foundation.BorderStroke(1.dp, TertiaryColor),
                            modifier = Modifier.size(52.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(Icons.Default.Bookmark, contentDescription = null, tint = SecondaryColor)
                            }
                        }
                    }

                    Spacer(Modifier.height(24.dp))
                }
            }
        }
    }
}

@Composable
private fun StatCard(icon: androidx.compose.ui.graphics.vector.ImageVector, label: String, value: String, modifier: Modifier = Modifier) {
    Surface(modifier = modifier, shape = RoundedCornerShape(12.dp), color = SurfaceColor) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Icon(icon, contentDescription = null, tint = PrimaryDark, modifier = Modifier.size(16.dp))
            Text(value, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Text(label, style = MaterialTheme.typography.labelSmall, color = TextSecondary)
        }
    }
}

@Composable
private fun ExperienceItem(exp: Experience) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier.size(36.dp).clip(RoundedCornerShape(8.dp)).background(SurfaceColor),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Outlined.Work, contentDescription = null, tint = PrimaryDark, modifier = Modifier.size(18.dp))
        }
        Column {
            Text(exp.title, style = MaterialTheme.typography.labelLarge)
            Text("${exp.company} • ${exp.period}", style = MaterialTheme.typography.bodySmall, color = TextSecondary)
        }
    }
}
