package com.example.jovi.ui.screens.discovery

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BookmarkBorder
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

data class VacancyDetail(
    val title: String = "Desarrollador Mobile Junior",
    val company: String = "Innovatech Corp",
    val companyInitials: String = "IC",
    val location: String = "Ciudad de México (Híbrido)",
    val type: String = "Prácticas",
    val duration: String = "6 meses",
    val salary: String = "8,000 – 12,000 MXN/mes",
    val stack: List<String> = listOf("Kotlin", "Jetpack Compose", "Room", "Firebase"),
    val description: String = "Buscamos un desarrollador mobile apasionado para unirse a nuestro equipo de producto. Trabajarás en la app principal de Innovatech que tiene más de 50,000 usuarios activos. Tendrás ownership real de funcionalidades desde el día 1.",
    val requirements: List<String> = listOf(
        "Estudiante de Ingeniería en Sistemas o carrera afín",
        "Conocimientos básicos de Kotlin o Java",
        "Familiaridad con Android Studio",
        "Disponibilidad para trabajar 6 horas al día",
    ),
    val benefits: List<String> = listOf(
        "Beca competitiva",
        "Modalidad híbrida (3 días presencial)",
        "Mentoring con senior developers",
        "Carta de recomendación al finalizar",
        "Posibilidad de contratación formal",
    ),
    val applicantCount: Int = 48,
    val postedAgo: String = "hace 2 días",
)

@Composable
fun VacancyDetailScreen(
    vacancy: VacancyDetail = VacancyDetail(),
    onBack: () -> Unit,
    onApply: () -> Unit,
) {
    var isSaved by remember { mutableStateOf(false) }

    Scaffold(
        containerColor = BackgroundColor,
        topBar = {
            JoviTopBar(
                title = "Detalle de Vacante",
                onBack = onBack,
                actions = {
                    IconButton(onClick = { isSaved = !isSaved }) {
                        Icon(
                            if (isSaved) Icons.Filled.BookmarkBorder else Icons.Outlined.BookmarkBorder,
                            contentDescription = "Guardar",
                            tint = if (isSaved) PrimaryDark else TextPrimary,
                        )
                    }
                }
            )
        },
        bottomBar = {
            Surface(shadowElevation = 4.dp, color = BackgroundColor) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .navigationBarsPadding()
                        .padding(horizontal = 20.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    OutlinedButton(
                        onClick = {},
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(50),
                        border = androidx.compose.foundation.BorderStroke(1.5.dp, SecondaryColor),
                    ) {
                        Text("Contactar", color = SecondaryColor)
                    }
                    JoviPrimaryButton(
                        text = "Aplicar Ahora",
                        onClick = onApply,
                        modifier = Modifier.weight(1f),
                    )
                }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(0.dp)
        ) {
            // Header card
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(PrimaryLight)
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(14.dp), verticalAlignment = Alignment.CenterVertically) {
                    ProfileAvatar(vacancy.companyInitials, size = 60.dp)
                    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                        Text(vacancy.title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = PrimaryDark)
                        Text(vacancy.company, style = MaterialTheme.typography.bodyMedium, color = PrimaryDark.copy(0.7f))
                    }
                }
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    InfoChip(Icons.Outlined.LocationOn, vacancy.location)
                    InfoChip(Icons.Outlined.Work, vacancy.type)
                }
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    InfoChip(Icons.Outlined.Schedule, vacancy.duration)
                    InfoChip(Icons.Outlined.People, "${vacancy.applicantCount} solicitantes")
                }
            }

            Spacer(Modifier.height(4.dp))

            // Salary & stack
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(BackgroundColor)
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                Surface(shape = RoundedCornerShape(14.dp), color = SecondaryColor.copy(0.12f)) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                    ) {
                        Icon(Icons.Outlined.Payments, contentDescription = null, tint = SecondaryColor, modifier = Modifier.size(22.dp))
                        Text(vacancy.salary, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold, color = SecondaryColor)
                    }
                }

                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    SectionHeader(title = "Stack tecnológico", icon = Icons.Outlined.Code)
                    androidx.compose.foundation.layout.FlowRow(horizontalArrangement = Arrangement.spacedBy(6.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        vacancy.stack.forEach { tech ->
                            Surface(shape = RoundedCornerShape(50), color = SurfaceColor) {
                                Text(tech, modifier = Modifier.padding(horizontal = 12.dp, vertical = 5.dp), style = MaterialTheme.typography.labelMedium, color = PrimaryDark)
                            }
                        }
                    }
                }
            }

            HorizontalDivider(thickness = 8.dp, color = SurfaceColor)

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    SectionHeader(title = "Descripción", icon = Icons.Outlined.Description)
                    Text(vacancy.description, style = MaterialTheme.typography.bodyMedium, color = TextSecondary, lineHeight = MaterialTheme.typography.bodyMedium.lineHeight)
                }

                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    SectionHeader(title = "Requisitos", icon = Icons.Outlined.CheckCircle)
                    vacancy.requirements.forEach { req ->
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.Top) {
                            Box(
                                modifier = Modifier.padding(top = 5.dp).size(6.dp).clip(androidx.compose.foundation.shape.CircleShape).background(PrimaryDark)
                            )
                            Text(req, style = MaterialTheme.typography.bodyMedium, color = TextSecondary)
                        }
                    }
                }

                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    SectionHeader(title = "Beneficios", icon = Icons.Outlined.Stars)
                    vacancy.benefits.forEach { ben ->
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Outlined.Check, contentDescription = null, tint = PrimaryDark, modifier = Modifier.size(16.dp))
                            Text(ben, style = MaterialTheme.typography.bodyMedium, color = TextSecondary)
                        }
                    }
                }
                Spacer(Modifier.height(8.dp))
            }
        }
    }
}

@Composable
private fun InfoChip(icon: androidx.compose.ui.graphics.vector.ImageVector, label: String) {
    Surface(shape = RoundedCornerShape(50), color = BackgroundColor.copy(0.7f)) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Icon(icon, contentDescription = null, tint = PrimaryDark, modifier = Modifier.size(13.dp))
            Text(label, style = MaterialTheme.typography.labelSmall, color = PrimaryDark)
        }
    }
}
