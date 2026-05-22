package com.example.jovi.ui.screens.recruiter

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.jovi.ui.components.ProfileAvatar
import com.example.jovi.ui.theme.*
import com.example.jovi.viewmodel.RecruiterViewModel

@Composable
fun RecruiterDashboardContent(
    viewModel: RecruiterViewModel,
    onAnalytics: () -> Unit,
    onPublishVacancy: () -> Unit,
    onViewApplicants: () -> Unit,
    onOpenChat: (String) -> Unit,
) {
    val stats by viewModel.stats.collectAsState()
    val candidates by viewModel.candidates.collectAsState()

    val vacancies = listOf(
        Triple("Mobile Developer Junior", "Activa", 12),
        Triple("Practicante UX/UI", "Activa", 8),
        Triple("Data Analyst Intern", "En pausa", 3),
    )

    LazyColumn(
        modifier = Modifier.fillMaxSize().background(BackgroundColor),
        contentPadding = PaddingValues(bottom = 24.dp),
    ) {
        item {
            // Header
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Brush.linearGradient(listOf(PrimaryDark, PrimaryDark.copy(0.85f))))
                    .statusBarsPadding()
                    .padding(horizontal = 20.dp, vertical = 16.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Column {
                            Text("Hola, Innovatech Corp", style = MaterialTheme.typography.labelMedium, color = BackgroundColor.copy(0.7f))
                            Text("Panel de Reclutador", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold, color = SecondaryColor)
                        }
                        Box(
                            modifier = Modifier.size(44.dp).clip(CircleShape).background(SecondaryColor),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("IC", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold, color = PrimaryDark)
                        }
                    }
                }
            }
        }

        item {
            // Stats grid
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text("Resumen", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 4.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    DashboardStatCard(Icons.Outlined.People, "Postulantes", "${stats.totalApplicants}", "+5 hoy", modifier = Modifier.weight(1f))
                    DashboardStatCard(Icons.Outlined.Favorite, "Matches", "${stats.totalMatches}", "+2 esta semana", modifier = Modifier.weight(1f))
                }
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    DashboardStatCard(Icons.Outlined.Work, "Vacantes activas", "${stats.activeVacancies}", "de 5 publicadas", modifier = Modifier.weight(1f))
                    DashboardStatCard(Icons.Outlined.Visibility, "Vistas de perfil", "${stats.profileViews}", "+34 esta semana", modifier = Modifier.weight(1f))
                }
            }
        }

        item {
            // Quick actions
            Column(modifier = Modifier.padding(horizontal = 16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text("Acciones rápidas", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    QuickActionButton(icon = Icons.Outlined.PostAdd, label = "Publicar\nvacante", onClick = onPublishVacancy, modifier = Modifier.weight(1f))
                    QuickActionButton(icon = Icons.Outlined.People, label = "Ver\ncandidatos", onClick = onViewApplicants, modifier = Modifier.weight(1f))
                    QuickActionButton(icon = Icons.Outlined.BarChart, label = "Analíticas", onClick = onAnalytics, modifier = Modifier.weight(1f))
                }
            }
        }

        item {
            // Active vacancies
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text("Mis vacantes", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    TextButton(onClick = onPublishVacancy) { Text("+ Nueva", color = PrimaryDark, style = MaterialTheme.typography.labelMedium) }
                }
                vacancies.forEach { (title, status, count) ->
                    VacancyMiniCard(title = title, status = status, applicantCount = count)
                }
            }
        }

        item {
            // Recent candidates
            Column(modifier = Modifier.padding(horizontal = 16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text("Candidatos recientes", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    TextButton(onClick = onViewApplicants) { Text("Ver todos", color = PrimaryDark, style = MaterialTheme.typography.labelMedium) }
                }
                candidates.take(3).forEach { candidate ->
                    RecentCandidateRow(
                        initials = candidate.avatarInitials,
                        name = candidate.displayName,
                        role = candidate.university.ifEmpty { "Desarrollador" },
                        onMessage = { onOpenChat(candidate.displayName) },
                    )
                }
                if (candidates.isEmpty()) {
                    // fallback demo data
                    listOf(
                        Triple("CM", "Carlos Mendoza", "UNAM"),
                        Triple("AG", "Ana Garcia", "Tec de Monterrey"),
                        Triple("LT", "Luis Torres", "IPN"),
                    ).forEach { (initials, name, uni) ->
                        RecentCandidateRow(initials = initials, name = name, role = uni, onMessage = { onOpenChat(name) })
                    }
                }
            }
        }
    }
}

@Composable
private fun DashboardStatCard(
    icon: ImageVector,
    label: String,
    value: String,
    subtitle: String,
    modifier: Modifier = Modifier,
) {
    Surface(modifier = modifier, shape = RoundedCornerShape(16.dp), color = SurfaceColor) {
        Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Icon(icon, contentDescription = null, tint = PrimaryDark, modifier = Modifier.size(20.dp))
            Text(value, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.ExtraBold, color = PrimaryDark)
            Text(label, style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Medium, color = TextPrimary)
            Text(subtitle, style = MaterialTheme.typography.labelSmall, color = TextSecondary)
        }
    }
}

@Composable
private fun QuickActionButton(icon: ImageVector, label: String, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Surface(
        onClick = onClick,
        modifier = modifier,
        shape = RoundedCornerShape(14.dp),
        color = PrimaryLight,
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Icon(icon, contentDescription = null, tint = PrimaryDark, modifier = Modifier.size(24.dp))
            Text(label, style = MaterialTheme.typography.labelSmall, color = PrimaryDark, textAlign = androidx.compose.ui.text.style.TextAlign.Center)
        }
    }
}

@Composable
private fun VacancyMiniCard(title: String, status: String, applicantCount: Int) {
    Surface(shape = RoundedCornerShape(12.dp), color = BackgroundColor, border = androidx.compose.foundation.BorderStroke(1.dp, DividerColor)) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(title, style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.SemiBold)
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp), verticalAlignment = Alignment.CenterVertically) {
                    Box(modifier = Modifier.size(7.dp).clip(CircleShape).background(if (status == "Activa") StatusAccepted else TextSecondary))
                    Text(status, style = MaterialTheme.typography.labelSmall, color = TextSecondary)
                }
            }
            Surface(shape = RoundedCornerShape(50), color = PrimaryLight) {
                Text("$applicantCount candidatos", modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp), style = MaterialTheme.typography.labelSmall, color = PrimaryDark)
            }
        }
    }
}

@Composable
private fun RecentCandidateRow(initials: String, name: String, role: String, onMessage: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        ProfileAvatar(initials, size = 40.dp)
        Column(modifier = Modifier.weight(1f)) {
            Text(name, style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Medium)
            Text(role, style = MaterialTheme.typography.bodySmall, color = TextSecondary)
        }
        IconButton(onClick = onMessage, modifier = Modifier.size(36.dp)) {
            Icon(Icons.Outlined.MailOutline, contentDescription = "Mensaje", tint = PrimaryDark, modifier = Modifier.size(18.dp))
        }
    }
}
