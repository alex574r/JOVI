package com.example.jovi.ui.screens.academic

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
import com.example.jovi.ui.components.*
import com.example.jovi.ui.theme.*

private data class Applicant(
    val name: String,
    val university: String,
    val gpa: String,
    val status: String,
)

private val applicants = listOf(
    Applicant("Sarah Jenkins", "New York University", "3.8", "Pendiente"),
    Applicant("Michael Chen", "UCLA", "3.6", "Entrevistado"),
    Applicant("Jessica Alva", "Boston University", "3.9", "Aceptado"),
    Applicant("David Kim", "Cornell University", "3.2", "Rechazado"),
)

@Composable
fun ServiceApplicantsScreen(
    vacancyTitle: String = "Social Service: Community Health",
    totalApplicants: Int = 24,
    onBack: () -> Unit,
    onViewProfile: () -> Unit,
    onChat: () -> Unit,
) {
    var selectedFilter by remember { mutableStateOf("Todos") }
    val filters = listOf("Todos", "Pendiente", "Entrevistado", "Aceptado")

    Scaffold(
        topBar = { JoviTopBar(title = "Postulantes", onBack = onBack) },
        containerColor = BackgroundColor,
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp)
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(SecondaryColor)
                        .padding(16.dp),
                    contentAlignment = Alignment.BottomStart
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Surface(shape = RoundedCornerShape(50), color = StatusAccepted.copy(0.3f)) {
                            Text("Activo • Publicado hace 5 días", modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp), style = MaterialTheme.typography.labelSmall, color = StatusAccepted)
                        }
                        Text(vacancyTitle, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = BackgroundColor)
                        Text("$totalApplicants Postulantes en total", style = MaterialTheme.typography.bodySmall, color = BackgroundColor.copy(0.7f))
                    }
                }
            }

            item {
                ScrollableTabRow(
                    selectedTabIndex = filters.indexOf(selectedFilter),
                    containerColor = BackgroundColor,
                    contentColor = SecondaryColor,
                    edgePadding = 16.dp,
                    divider = {},
                ) {
                    filters.forEachIndexed { index, filter ->
                        Tab(
                            selected = selectedFilter == filter,
                            onClick = { selectedFilter = filter },
                            text = { Text(filter, style = MaterialTheme.typography.labelLarge) }
                        )
                    }
                }
                Spacer(Modifier.height(8.dp))
            }

            items(applicants.filter { selectedFilter == "Todos" || it.status == selectedFilter }) { applicant ->
                ApplicantCard(applicant, onViewProfile = onViewProfile, onChat = onChat)
            }

            item {
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    Text(
                        "Desliza las tarjetas para más acciones",
                        style = MaterialTheme.typography.labelSmall,
                        color = TextSecondary,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun ApplicantCard(applicant: Applicant, onViewProfile: () -> Unit, onChat: () -> Unit) {
    var expanded by remember { mutableStateOf(false) }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp),
        shape = RoundedCornerShape(14.dp),
        color = BackgroundColor,
        border = androidx.compose.foundation.BorderStroke(1.dp, DividerColor)
    ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                ProfileAvatar(applicant.name.take(2), size = 46.dp)
                Column(modifier = Modifier.weight(1f)) {
                    Text(applicant.name, style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold)
                    Text(applicant.university, style = MaterialTheme.typography.bodySmall, color = TextSecondary)
                    Text("GPA: ${applicant.gpa}", style = MaterialTheme.typography.labelSmall, color = PrimaryDark, fontWeight = FontWeight.SemiBold)
                }
                IconButton(onClick = onViewProfile) {
                    Icon(Icons.Outlined.Visibility, contentDescription = "Ver perfil", tint = TextSecondary)
                }
                IconButton(onClick = onChat) {
                    Icon(Icons.Outlined.ChatBubble, contentDescription = "Chat", tint = PrimaryDark)
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Estado", style = MaterialTheme.typography.bodySmall, color = TextSecondary)
                var statusExpanded by remember { mutableStateOf(false) }
                Box {
                    Surface(
                        onClick = { statusExpanded = true },
                        shape = RoundedCornerShape(50),
                        color = when (applicant.status) {
                            "Pendiente" -> StatusPending.copy(0.15f)
                            "Entrevistado" -> StatusInterviewed.copy(0.15f)
                            "Aceptado" -> StatusAccepted.copy(0.15f)
                            else -> StatusRejected.copy(0.15f)
                        }
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            StatusBadge(applicant.status)
                            Icon(Icons.Default.ArrowDropDown, contentDescription = null, tint = TextSecondary, modifier = Modifier.size(16.dp))
                        }
                    }
                }
            }
        }
    }
}
