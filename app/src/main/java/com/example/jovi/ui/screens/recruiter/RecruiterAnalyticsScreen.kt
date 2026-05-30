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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.jovi.ui.components.JoviTopBar
import com.example.jovi.ui.theme.*

@Composable
fun RecruiterAnalyticsScreen(onBack: () -> Unit) {
    val weekDays = listOf("L", "M", "X", "J", "V", "S", "D")
    val applications = listOf(4, 7, 3, 9, 12, 5, 8)
    val maxVal = applications.max().toFloat()

    val conversionSteps = listOf(
        Triple("Vistas de vacante", 234, Color(0xFF4CAF50)),
        Triple("Aplicaciones", 48, Color(0xFF2196F3)),
        Triple("Entrevistas", 12, Color(0xFF9C27B0)),
        Triple("Ofertas enviadas", 4, Color(0xFFFF9800)),
        Triple("Contratados", 2, Color(0xFFF44336)),
    )

    Scaffold(
        containerColor = BackgroundColor,
        topBar = { JoviTopBar(title = "Analíticas", onBack = onBack) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            // Period selector
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                listOf("7 días", "30 días", "3 meses").forEachIndexed { index, label ->
                    var selectedPeriod by remember { mutableIntStateOf(0) }
                    val selected = index == 0
                    FilterChip(
                        selected = selected,
                        onClick = {},
                        label = { Text(label, style = MaterialTheme.typography.labelMedium) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = PrimaryDark,
                            selectedLabelColor = BackgroundColor,
                        ),
                        border = FilterChipDefaults.filterChipBorder(
                            enabled = true, selected = selected,
                            borderColor = TertiaryColor, selectedBorderColor = PrimaryDark,
                        )
                    )
                }
            }

            // Summary KPIs
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                KpiCard("Postulantes", "48", "+12%", isPositive = true, modifier = Modifier.weight(1f))
                KpiCard("Matches", "12", "+4%", isPositive = true, modifier = Modifier.weight(1f))
                KpiCard("Rechazos", "36", "-2%", isPositive = false, modifier = Modifier.weight(1f))
            }

            // Bar chart — applications by day
            Surface(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                shape = RoundedCornerShape(16.dp),
                color = SurfaceColor,
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text("Aplicaciones por día", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold)
                    Row(
                        modifier = Modifier.fillMaxWidth().height(120.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.Bottom,
                    ) {
                        applications.forEachIndexed { i, count ->
                            Column(
                                modifier = Modifier.weight(1f),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(4.dp, alignment = Alignment.Bottom),
                            ) {
                                Text("$count", style = MaterialTheme.typography.labelSmall, color = PrimaryDark)
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .fillMaxHeight(count / maxVal)
                                        .clip(RoundedCornerShape(topStart = 6.dp, topEnd = 6.dp))
                                        .background(PrimaryDark.copy(0.8f + 0.2f * (count / maxVal)))
                                )
                            }
                        }
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        weekDays.forEach { day ->
                            Text(day, style = MaterialTheme.typography.labelSmall, color = TextSecondary, modifier = Modifier.weight(1f), textAlign = androidx.compose.ui.text.style.TextAlign.Center)
                        }
                    }
                }
            }

            // Conversion funnel
            Surface(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                shape = RoundedCornerShape(16.dp),
                color = SurfaceColor,
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text("Embudo de conversión", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold)
                    conversionSteps.forEachIndexed { index, (label, count, color) ->
                        val ratio = count.toFloat() / conversionSteps[0].second.toFloat()
                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Text(label, style = MaterialTheme.typography.bodySmall, color = TextSecondary)
                                Text("$count", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold, color = color)
                            }
                            LinearProgressIndicator(
                                progress = { ratio },
                                modifier = Modifier.fillMaxWidth().height(6.dp).clip(RoundedCornerShape(50)),
                                color = color,
                                trackColor = TertiaryColor.copy(0.3f),
                            )
                        }
                    }
                }
            }

            // Top vacancies
            Surface(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                shape = RoundedCornerShape(16.dp),
                color = SurfaceColor,
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text("Vacantes más populares", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold)
                    listOf(
                        Triple("Mobile Developer Junior", 12, 0.75f),
                        Triple("Practicante UX/UI", 8, 0.5f),
                        Triple("Data Analyst Intern", 3, 0.19f),
                    ).forEach { (title, count, ratio) ->
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                Text(title, style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Medium)
                                LinearProgressIndicator(
                                    progress = { ratio },
                                    modifier = Modifier.fillMaxWidth().height(5.dp).clip(RoundedCornerShape(50)),
                                    color = PrimaryDark,
                                    trackColor = TertiaryColor.copy(0.3f),
                                )
                            }
                            Text("$count", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold, color = PrimaryDark)
                        }
                    }
                }
            }

            Spacer(Modifier.height(8.dp))
        }
    }
}

@Composable
private fun KpiCard(label: String, value: String, trend: String, isPositive: Boolean, modifier: Modifier = Modifier) {
    Surface(modifier = modifier, shape = RoundedCornerShape(14.dp), color = if (isPositive) PrimaryLight else StatusRejected.copy(0.08f)) {
        Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(value, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.ExtraBold, color = if (isPositive) PrimaryDark else StatusRejected)
            Text(label, style = MaterialTheme.typography.labelSmall, color = TextSecondary)
            Text(trend, style = MaterialTheme.typography.labelSmall, color = if (isPositive) StatusAccepted else StatusRejected, fontWeight = FontWeight.Bold)
        }
    }
}
