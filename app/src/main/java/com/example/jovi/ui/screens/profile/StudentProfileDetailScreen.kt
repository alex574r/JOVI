package com.example.jovi.ui.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.jovi.ui.components.*
import com.example.jovi.ui.theme.*

@Composable
fun StudentProfileDetailScreen(
    onBack: () -> Unit,
    onReject: () -> Unit,
    onAcceptForInterview: () -> Unit,
) {
    val skills = listOf("UI/UX Design", "Figma", "User Research", "Tailwind CSS", "Prototyping", "Accessibility", "React")

    Scaffold(
        topBar = {
            JoviTopBar(
                title = "Perfil del Estudiante",
                onBack = onBack,
                actions = {
                    IconButton(onClick = {}) {
                        Icon(Icons.Default.MoreVert, contentDescription = null, tint = TextPrimary)
                    }
                }
            )
        },
        bottomBar = {
            Surface(shadowElevation = 4.dp, color = BackgroundColor) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    JoviSecondaryButton(
                        text = "Rechazar",
                        onClick = onReject,
                        leadingIcon = Icons.Default.Close,
                        modifier = Modifier.weight(1f)
                    )
                    JoviPrimaryButton(
                        text = "Aceptar para Entrevista",
                        onClick = onAcceptForInterview,
                        leadingIcon = Icons.Default.CheckCircle,
                        modifier = Modifier.weight(2f)
                    )
                }
            }
        },
        containerColor = BackgroundColor,
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                Box(contentAlignment = Alignment.BottomEnd) {
                    ProfileAvatar(initials = "AR", size = 80.dp)
                    Surface(shape = androidx.compose.foundation.shape.CircleShape, color = StatusInterviewed) {
                        Icon(Icons.Outlined.School, contentDescription = null, tint = BackgroundColor, modifier = Modifier.padding(4.dp).size(14.dp))
                    }
                }
                Spacer(Modifier.height(10.dp))
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text("Alex Rivera", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                    VerifiedBadge()
                }
                Text("Estudiante Verificado • Stanford University", style = MaterialTheme.typography.bodySmall, color = PrimaryDark)
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Surface(modifier = Modifier.weight(1f), shape = RoundedCornerShape(12.dp), color = SurfaceColor) {
                    Column(modifier = Modifier.padding(12.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Outlined.School, contentDescription = null, tint = PrimaryDark, modifier = Modifier.size(18.dp))
                        Text("GPA: 3.9/4.0", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold)
                    }
                }
                Surface(modifier = Modifier.weight(1f), shape = RoundedCornerShape(12.dp), color = SurfaceColor) {
                    Column(modifier = Modifier.padding(12.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Outlined.Assessment, contentDescription = null, tint = PrimaryDark, modifier = Modifier.size(18.dp))
                        Text("85% Créditos", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold)
                    }
                }
            }

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                SectionHeader(title = "Objetivos Profesionales")
                Surface(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp), color = SurfaceColor) {
                    Text(
                        "Buscando prácticas de verano en UI/UX para aplicar mis habilidades en investigación de usuarios y prototipado. Apasionado por crear experiencias digitales accesibles para el impacto social y el desarrollo de sistemas de diseño escalables.",
                        modifier = Modifier.padding(14.dp),
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextSecondary
                    )
                }
            }

            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                SectionHeader(title = "Habilidades y Expertise")
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    skills.forEach { SkillChip(it) }
                }
            }

            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                SectionHeader(title = "Documentos")
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    color = SurfaceColor
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Surface(shape = RoundedCornerShape(8.dp), color = ErrorColor.copy(0.1f)) {
                            Icon(Icons.Outlined.PictureAsPdf, contentDescription = null, tint = ErrorColor, modifier = Modifier.padding(8.dp).size(22.dp))
                        }
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Alex_Rivera_CV_2024.pdf", style = MaterialTheme.typography.labelLarge)
                            Text("Actualizado hace 2 días • 1.2 MB", style = MaterialTheme.typography.labelSmall, color = TextSecondary)
                        }
                        Icon(Icons.Outlined.Visibility, contentDescription = null, tint = TextSecondary)
                    }
                }
            }
        }
    }
}
