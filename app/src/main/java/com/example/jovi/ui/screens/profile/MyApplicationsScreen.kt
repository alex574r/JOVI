package com.example.jovi.ui.screens.profile

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.jovi.ui.components.JoviTopBar
import com.example.jovi.ui.components.ProfileAvatar
import com.example.jovi.ui.theme.*

private enum class AppStatus { PENDING, REVIEW, INTERVIEW, ACCEPTED, REJECTED }
private data class MyApplication(
    val id: Int,
    val jobTitle: String,
    val company: String,
    val companyInitials: String,
    val status: AppStatus,
    val appliedDate: String,
    val nextStep: String? = null,
)

@Composable
fun MyApplicationsScreen(onBack: () -> Unit, onViewDetail: (Int) -> Unit = {}) {
    var selectedFilter by remember { mutableIntStateOf(0) }
    val filters = listOf("Todas", "Activas", "Finalizadas")

    val applications = listOf(
        MyApplication(1, "Desarrollador Mobile Junior", "Innovatech Corp", "IC", AppStatus.INTERVIEW, "20 May 2025", "Entrevista técnica el 25 May"),
        MyApplication(2, "Practicante UX", "Stanford University", "SU", AppStatus.REVIEW, "18 May 2025", "En revisión de CV"),
        MyApplication(3, "Data Analyst Intern", "TechCorp", "TC", AppStatus.PENDING, "15 May 2025"),
        MyApplication(4, "Backend Developer", "StartupXYZ", "SX", AppStatus.ACCEPTED, "1 May 2025", "Inicio: 1 Jun 2025"),
        MyApplication(5, "Frontend Intern", "DesignLab", "DL", AppStatus.REJECTED, "28 Apr 2025"),
    )

    val filtered = when (selectedFilter) {
        1 -> applications.filter { it.status in listOf(AppStatus.PENDING, AppStatus.REVIEW, AppStatus.INTERVIEW) }
        2 -> applications.filter { it.status in listOf(AppStatus.ACCEPTED, AppStatus.REJECTED) }
        else -> applications
    }

    Scaffold(
        containerColor = BackgroundColor,
        topBar = {
            JoviTopBar(
                title = "Mis Solicitudes",
                onBack = onBack,
                actions = {
                    IconButton(onClick = {}) {
                        Icon(Icons.Outlined.FilterList, contentDescription = "Filtrar", tint = TextPrimary)
                    }
                }
            )
        }
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

            LazyColumn(contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                items(filtered, key = { it.id }) { app ->
                    ApplicationCard(app = app, onClick = { onViewDetail(app.id) })
                }
            }
        }
    }
}

@Composable
private fun ApplicationCard(app: MyApplication, onClick: () -> Unit) {
    val (statusLabel, statusColor, statusBg) = when (app.status) {
        AppStatus.PENDING -> Triple("Enviada", PrimaryDark, PrimaryLight)
        AppStatus.REVIEW -> Triple("En Revisión", Color(0xFF1565C0), Color(0xFFE3F2FD))
        AppStatus.INTERVIEW -> Triple("Entrevista", Color(0xFF6A1B9A), Color(0xFFF3E5F5))
        AppStatus.ACCEPTED -> Triple("Aceptado", StatusAccepted, StatusAccepted.copy(0.12f))
        AppStatus.REJECTED -> Triple("Rechazado", StatusRejected, StatusRejected.copy(0.12f))
    }

    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        color = BackgroundColor,
        shadowElevation = 1.dp,
        border = androidx.compose.foundation.BorderStroke(1.dp, DividerColor),
    ) {
        Column(modifier = Modifier.fillMaxWidth().padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp), verticalAlignment = Alignment.CenterVertically) {
                ProfileAvatar(app.companyInitials, size = 44.dp)
                Column(modifier = Modifier.weight(1f)) {
                    Text(app.jobTitle, style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold)
                    Text(app.company, style = MaterialTheme.typography.bodySmall, color = TextSecondary)
                }
                Surface(shape = RoundedCornerShape(50), color = statusBg) {
                    Text(
                        statusLabel,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                        style = MaterialTheme.typography.labelSmall,
                        color = statusColor,
                        fontWeight = FontWeight.SemiBold,
                    )
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Outlined.CalendarToday, contentDescription = null, tint = TextSecondary, modifier = Modifier.size(13.dp))
                    Text("Aplicado: ${app.appliedDate}", style = MaterialTheme.typography.labelSmall, color = TextSecondary)
                }
                Icon(Icons.Outlined.ChevronRight, contentDescription = null, tint = TextSecondary, modifier = Modifier.size(18.dp))
            }

            if (app.nextStep != null) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(PrimaryLight, RoundedCornerShape(8.dp))
                        .padding(horizontal = 10.dp, vertical = 6.dp),
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(Icons.Outlined.Info, contentDescription = null, tint = PrimaryDark, modifier = Modifier.size(14.dp))
                    Text(app.nextStep, style = MaterialTheme.typography.labelSmall, color = PrimaryDark)
                }
            }
        }
    }
}
