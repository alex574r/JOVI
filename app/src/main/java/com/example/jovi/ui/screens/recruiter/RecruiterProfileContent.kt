package com.example.jovi.ui.screens.recruiter

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.jovi.ui.components.ProfileAvatar
import com.example.jovi.ui.components.VerifiedBadge
import com.example.jovi.ui.theme.*
import com.example.jovi.viewmodel.RecruiterViewModel

@Composable
fun RecruiterProfileContent(
    viewModel: RecruiterViewModel,
    onSettings: () -> Unit,
    onLogout: () -> Unit,
    onAnalytics: () -> Unit,
    onPublishVacancy: () -> Unit,
) {
    val stats by viewModel.stats.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundColor)
            .verticalScroll(rememberScrollState())
    ) {
        // Cover + avatar
        Box(
            modifier = Modifier.fillMaxWidth().height(140.dp).background(PrimaryLight)
        )
        Column(modifier = Modifier.padding(horizontal = 20.dp)) {
            Box(modifier = Modifier.offset(y = (-36).dp)) {
                ProfileAvatar("IC", size = 80.dp)
            }
            Column(modifier = Modifier.offset(y = (-24).dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text("Innovatech Corp", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
                    VerifiedBadge()
                }
                Text("Empresa de tecnología • CDMX", style = MaterialTheme.typography.bodyMedium, color = TextSecondary)
                Text(
                    "Empresa de tecnología buscando talento joven. Prácticas con impacto real desde el primer día.",
                    style = MaterialTheme.typography.bodySmall, color = TextSecondary
                )
            }
        }

        // Stats
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            RecruiterProfileStat("Postulantes", "${stats.totalApplicants}", modifier = Modifier.weight(1f))
            RecruiterProfileStat("Matches", "${stats.totalMatches}", modifier = Modifier.weight(1f))
            RecruiterProfileStat("Vistas", "${stats.profileViews}", modifier = Modifier.weight(1f))
        }

        Spacer(Modifier.height(16.dp))
        HorizontalDivider(thickness = 8.dp, color = SurfaceColor)
        Spacer(Modifier.height(8.dp))

        // Company actions
        Column(modifier = Modifier.padding(horizontal = 16.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text("Gestión", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold, color = TextSecondary, modifier = Modifier.padding(vertical = 6.dp))
            ProfileMenuRow(Icons.Outlined.PostAdd, "Publicar nueva vacante", onClick = onPublishVacancy)
            ProfileMenuRow(Icons.Outlined.BarChart, "Ver analíticas", onClick = onAnalytics)
            ProfileMenuRow(Icons.Outlined.Business, "Editar perfil de empresa", onClick = {})
        }

        Spacer(Modifier.height(4.dp))
        HorizontalDivider(color = DividerColor)
        Spacer(Modifier.height(4.dp))

        Column(modifier = Modifier.padding(horizontal = 16.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text("Cuenta", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold, color = TextSecondary, modifier = Modifier.padding(vertical = 6.dp))
            ProfileMenuRow(Icons.Outlined.Settings, "Configuración", onClick = onSettings)
            ProfileMenuRow(Icons.Outlined.HelpOutline, "Ayuda y soporte", onClick = {})
        }

        Spacer(Modifier.height(16.dp))

        TextButton(
            onClick = onLogout,
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            colors = ButtonDefaults.textButtonColors(contentColor = StatusRejected),
        ) {
            Icon(Icons.Outlined.Logout, contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(Modifier.width(8.dp))
            Text("Cerrar sesión", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Medium)
        }
        Spacer(Modifier.height(32.dp))
    }
}

@Composable
private fun RecruiterProfileStat(label: String, value: String, modifier: Modifier = Modifier) {
    Surface(modifier = modifier, shape = RoundedCornerShape(12.dp), color = SurfaceColor) {
        Column(modifier = Modifier.padding(12.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(value, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.ExtraBold, color = PrimaryDark)
            Text(label, style = MaterialTheme.typography.labelSmall, color = TextSecondary)
        }
    }
}

@Composable
private fun ProfileMenuRow(icon: ImageVector, label: String, onClick: () -> Unit) {
    Surface(onClick = onClick, color = BackgroundColor) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(vertical = 14.dp),
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(icon, contentDescription = null, tint = TextPrimary, modifier = Modifier.size(20.dp))
            Text(label, style = MaterialTheme.typography.bodyMedium, modifier = Modifier.weight(1f))
            Icon(Icons.Outlined.ChevronRight, contentDescription = null, tint = TextSecondary, modifier = Modifier.size(18.dp))
        }
    }
}
