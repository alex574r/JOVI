package com.example.jovi.ui.screens.discovery

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.jovi.ui.components.*
import com.example.jovi.ui.theme.*

private data class JobCard(
    val title: String,
    val company: String,
    val location: String,
    val modality: String,
    val salaryRange: String,
    val level: String,
    val tags: List<String>,
    val description: String,
)

private val sampleJobs = listOf(
    JobCard(
        "Senior UX Designer", "Spotify", "Estocolmo, SE", "Presencial",
        "\$90k - \$120k", "Senior Level", listOf("Design System"),
        "Buscamos una mente creativa para liderar nuestro equipo de design system. Serás responsable de definir el lenguaje visual e interacción..."
    ),
    JobCard(
        "Product Manager", "Google", "CDMX, MX", "Remoto",
        "\$80k - \$100k", "Mid Level", listOf("Agile", "Roadmap"),
        "Únete a nuestro equipo de producto para definir la estrategia y visión de nuestros principales productos..."
    ),
    JobCard(
        "Android Developer", "Meta", "Remote", "Híbrido",
        "\$95k - \$130k", "Senior Level", listOf("Kotlin", "Compose"),
        "Desarrolla aplicaciones Android de alta calidad para millones de usuarios alrededor del mundo..."
    ),
)

@Composable
fun JobDiscoveryScreen(
    onMatch: () -> Unit,
    onVacancyDetail: () -> Unit = {},
    currentUserName: String = "Sarah Jenkins",
) {
    var currentIndex by remember { mutableIntStateOf(0) }
    val job = sampleJobs.getOrNull(currentIndex % sampleJobs.size) ?: sampleJobs.first()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundColor)
    ) {
        Spacer(Modifier.height(16.dp))

        // Header — igual que InternshipDiscoveryScreen
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(14.dp),
                modifier = Modifier.weight(1f)
            ) {
                ProfileAvatar(initials = currentUserName.take(2), size = 44.dp)
                Text(currentUserName, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            }
            Icon(Icons.Default.Tune, contentDescription = "Filtros", tint = SecondaryColor)
        }

        Spacer(Modifier.height(20.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(horizontal = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            // Tarjeta de fondo
            Surface(
                modifier = Modifier
                    .fillMaxWidth(0.92f)
                    .fillMaxHeight(0.85f)
                    .offset(y = (-16).dp),
                shape = RoundedCornerShape(24.dp),
                color = SurfaceColor,
                shadowElevation = 2.dp
            ) {}

            // Tarjeta principal swipeable
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

                        // Banner superior (igual que InternshipDiscovery)
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(150.dp)
                                .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                                .background(PrimaryLight),
                            contentAlignment = Alignment.Center
                        ) {
                            ProfileAvatar(job.company.take(2), size = 56.dp)
                            // Etiqueta tipo de empleo
                            Row(
                                modifier = Modifier
                                    .align(Alignment.TopStart)
                                    .padding(12.dp)
                            ) {
                                Surface(shape = RoundedCornerShape(50), color = PrimaryColor) {
                                    Text(
                                        "Empleo",
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                        style = MaterialTheme.typography.labelSmall,
                                        color = SecondaryColor,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }

                        // Contenido scrollable
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .verticalScroll(rememberScrollState())
                                .padding(20.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.Top
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(job.title, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
                                    Text(job.company, style = MaterialTheme.typography.bodyLarge, color = PrimaryDark)
                                }
                                IconButton(onClick = onVacancyDetail, modifier = Modifier.size(24.dp)) {
                                    Icon(Icons.Outlined.Info, contentDescription = "Ver detalle", tint = TextSecondary)
                                }
                            }

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(Icons.Outlined.LocationOn, contentDescription = null, tint = TextSecondary, modifier = Modifier.size(14.dp))
                                Text("${job.location} • ${job.modality}", style = MaterialTheme.typography.bodySmall, color = TextSecondary)
                            }

                            FlowRow(horizontalArrangement = Arrangement.spacedBy(6.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                InfoChip(text = job.salaryRange)
                                InfoChip(text = job.level)
                                job.tags.forEach { InfoChip(text = it) }
                            }

                            Text(job.description, style = MaterialTheme.typography.bodyMedium, color = TextSecondary)
                        }

                        // Botones siempre visibles al fondo
                        Box(modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp)) {
                            SwipeActionButtons(
                                onDislike = { currentIndex++ },
                                onUndo = { if (currentIndex > 0) currentIndex-- },
                                onSave = {},
                                onLike = { currentIndex++; onMatch() },
                            )
                        }
                    }
                }
            }
        }

        Spacer(Modifier.height(8.dp))
    }
}
