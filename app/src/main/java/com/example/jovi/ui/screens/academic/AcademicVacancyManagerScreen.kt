package com.example.jovi.ui.screens.academic

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.jovi.ui.components.*
import com.example.jovi.ui.theme.*

private data class VacancyListing(
    val category: String,
    val title: String,
    val postedAgo: String,
    val applicantCount: Int,
    val isActive: Boolean,
    val closingIn: String? = null,
)

private val listings = listOf(
    VacancyListing("Servicio Profesional", "Research Assistant - Biology Dept.", "hace 2d", 12, true),
    VacancyListing("Beca", "PhD Grant: Renewable Energy", "cierra", 24, true, "5d"),
    VacancyListing("Servicio Profesional", "Junior Lab Assistant", "hace 1sem", 0, false),
)

@Composable
fun AcademicVacancyManagerScreen(
    onNotifications: () -> Unit,
    onPublishVacancy: () -> Unit,
    onReviewApplicants: (String) -> Unit,
    onMessages: () -> Unit,
    onProfile: () -> Unit,
    onSettings: () -> Unit,
) {
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("Todas", "Profesional", "Becas")

    Scaffold(
        containerColor = BackgroundColor,
        bottomBar = {
            NavigationBar(containerColor = BackgroundColor, tonalElevation = 4.dp) {
                listOf(
                    Triple(Icons.Default.Dashboard, Icons.Outlined.Dashboard, "Dashboard"),
                    Triple(Icons.Default.Chat, Icons.Outlined.ChatBubbleOutline, "Mensajes"),
                    Triple(Icons.Default.Person, Icons.Outlined.Person, "Perfil"),
                    Triple(Icons.Default.Settings, Icons.Outlined.Settings, "Config."),
                ).forEachIndexed { index, (filled, outline, label) ->
                    NavigationBarItem(
                        selected = index == 0,
                        onClick = {
                            when (index) {
                                1 -> onMessages()
                                2 -> onProfile()
                            }
                        },
                        icon = { Icon(if (index == 0) filled else outline, contentDescription = label) },
                        label = { Text(label, style = MaterialTheme.typography.labelSmall) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = PrimaryDark,
                            selectedTextColor = PrimaryDark,
                            indicatorColor = PrimaryLight,
                        )
                    )
                }
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onPublishVacancy,
                containerColor = PrimaryColor,
                contentColor = SecondaryColor,
                shape = CircleShape
            ) {
                Icon(Icons.Default.Add, contentDescription = "Nueva vacante")
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(CircleShape)
                                .background(SecondaryColor),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Outlined.School, contentDescription = null, tint = PrimaryColor, modifier = Modifier.size(22.dp))
                        }
                        Column {
                            Text("Bienvenido de vuelta,", style = MaterialTheme.typography.bodySmall, color = TextSecondary)
                            Text("Stanford Univ.", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                        }
                    }
                    IconButton(onClick = onNotifications) {
                        Icon(Icons.Default.Notifications, contentDescription = "Notificaciones", tint = SecondaryColor)
                    }
                }
            }

            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Surface(modifier = Modifier.weight(1f), shape = RoundedCornerShape(16.dp), color = PrimaryLight) {
                        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Icon(Icons.Outlined.WorkOutline, contentDescription = null, tint = PrimaryDark, modifier = Modifier.size(22.dp))
                            Text("VACANTES", style = MaterialTheme.typography.labelSmall, color = TextSecondary, fontWeight = FontWeight.Bold)
                            Text("12", style = MaterialTheme.typography.headlineLarge, fontWeight = FontWeight.Bold)
                            Text("+2 esta semana", style = MaterialTheme.typography.labelSmall, color = PrimaryDark)
                        }
                    }
                    Surface(modifier = Modifier.weight(1f), shape = RoundedCornerShape(16.dp), color = PrimaryLight) {
                        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Icon(Icons.Outlined.People, contentDescription = null, tint = PrimaryDark, modifier = Modifier.size(22.dp))
                            Text("POSTULANTES", style = MaterialTheme.typography.labelSmall, color = TextSecondary, fontWeight = FontWeight.Bold)
                            Text("486", style = MaterialTheme.typography.headlineLarge, fontWeight = FontWeight.Bold)
                            Text("12 por revisar", style = MaterialTheme.typography.labelSmall, color = PrimaryDark)
                        }
                    }
                }
                Spacer(Modifier.height(16.dp))
            }

            item {
                ScrollableTabRow(
                    selectedTabIndex = selectedTab,
                    containerColor = BackgroundColor,
                    contentColor = SecondaryColor,
                    edgePadding = 20.dp,
                    divider = {},
                    indicator = { tabPositions ->
                        TabRowDefaults.SecondaryIndicator(
                            modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                            color = PrimaryColor,
                            height = 3.dp
                        )
                    }
                ) {
                    tabs.forEachIndexed { index, tab ->
                        Tab(
                            selected = selectedTab == index,
                            onClick = { selectedTab = index },
                            text = { Text(tab, style = MaterialTheme.typography.labelLarge) }
                        )
                    }
                }
                Spacer(Modifier.height(12.dp))
            }

            item {
                Text(
                    "LISTADOS ACTIVOS",
                    style = MaterialTheme.typography.labelSmall,
                    color = TextSecondary,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 4.dp)
                )
            }

            items(listings) { listing ->
                VacancyCard(listing, onReview = { onReviewApplicants(listing.title) })
            }
        }
    }
}

@Composable
private fun VacancyCard(listing: VacancyListing, onReview: () -> Unit) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp),
        shape = RoundedCornerShape(16.dp),
        color = BackgroundColor,
        border = androidx.compose.foundation.BorderStroke(1.dp, DividerColor),
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                    Surface(shape = RoundedCornerShape(50), color = PrimaryLight) {
                        Text(listing.category, modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp), style = MaterialTheme.typography.labelSmall, color = PrimaryDark)
                    }
                    Spacer(Modifier.height(4.dp))
                    Text(listing.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        Icon(Icons.Outlined.Schedule, contentDescription = null, tint = TextSecondary, modifier = Modifier.size(12.dp))
                        if (listing.closingIn != null) {
                            Icon(Icons.Default.Timer, contentDescription = null, tint = StatusPending, modifier = Modifier.size(12.dp))
                            Text("Cierra en ${listing.closingIn}", style = MaterialTheme.typography.labelSmall, color = StatusPending)
                        } else {
                            Text("Publicado ${listing.postedAgo}", style = MaterialTheme.typography.labelSmall, color = TextSecondary)
                        }
                    }
                }
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .clip(CircleShape)
                        .background(if (listing.isActive) StatusAccepted else TertiaryColor)
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (listing.applicantCount > 0) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        Row(horizontalArrangement = Arrangement.spacedBy((-8).dp)) {
                            repeat(minOf(3, listing.applicantCount)) {
                                ProfileAvatar("AP", size = 26.dp)
                            }
                        }
                        Text("${listing.applicantCount} Postulantes", style = MaterialTheme.typography.bodySmall, color = TextSecondary)
                    }
                } else {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        Icon(Icons.Outlined.HelpOutline, contentDescription = null, tint = TextSecondary, modifier = Modifier.size(16.dp))
                        Text("0 Postulantes", style = MaterialTheme.typography.bodySmall, color = TextSecondary)
                    }
                }

                Surface(
                    onClick = if (listing.applicantCount > 0) onReview else { {} },
                    shape = RoundedCornerShape(50),
                    color = if (listing.applicantCount > 0) PrimaryColor else SurfaceColor
                ) {
                    Text(
                        "Revisar →",
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 7.dp),
                        style = MaterialTheme.typography.labelMedium,
                        color = if (listing.applicantCount > 0) SecondaryColor else TextSecondary
                    )
                }
            }
        }
    }
}
