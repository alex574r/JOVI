package com.example.jovi.ui.screens.calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.outlined.Videocam
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.jovi.ui.components.*
import com.example.jovi.ui.theme.*

private data class Appointment(
    val timeRange: String,
    val company: String,
    val position: String,
    val person: String,
    val status: AppointmentStatus,
)

private enum class AppointmentStatus { PAST, NOW, SOON, UPCOMING }

private val appointments = listOf(
    Appointment("09:00 AM - 10:00 AM", "Apple Inc.", "Systems Engineer Position", "", AppointmentStatus.PAST),
    Appointment("10:00 AM - 11:00 AM", "Google", "Senior UI Designer Interview", "Sarah Jenkins (HR Lead)", AppointmentStatus.NOW),
    Appointment("02:30 PM - 03:30 PM", "Meta Platforms", "Product Designer - Reels Team", "", AppointmentStatus.SOON),
    Appointment("04:00 PM - 04:30 PM", "Netflix", "Initial Portfolio Review", "", AppointmentStatus.UPCOMING),
)

@Composable
fun MyAppointmentsScreen(
    onBack: () -> Unit,
    onJoinMeeting: () -> Unit,
) {
    val weekDays = listOf("D 12", "L 13", "M 14", "Mi 15", "J 16", "V 17", "S")
    var selectedDay by remember { mutableIntStateOf(3) }

    Scaffold(
        topBar = {
            JoviTopBar(
                title = "Mis Citas",
                onBack = onBack,
                actions = {
                    IconButton(onClick = {}) {
                        Icon(Icons.Default.CalendarMonth, contentDescription = null, tint = TextPrimary)
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {},
                containerColor = PrimaryColor,
                contentColor = SecondaryColor
            ) {
                Icon(Icons.Default.Add, contentDescription = "Agregar cita")
            }
        },
        containerColor = BackgroundColor,
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(0.dp)
        ) {
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(horizontal = 20.dp, vertical = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(weekDays.indices.toList()) { i ->
                    val parts = weekDays.getOrElse(i) { "" }.split(" ")
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(50))
                            .background(if (i == selectedDay) PrimaryColor else BackgroundColor)
                            .border(if (i != selectedDay) 1.dp else 0.dp, TertiaryColor, RoundedCornerShape(50))
                            .clickable { selectedDay = i }
                            .padding(horizontal = 14.dp, vertical = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            if (parts.size > 0) Text(parts[0], style = MaterialTheme.typography.labelSmall, color = if (i == selectedDay) SecondaryColor else TextSecondary)
                            if (parts.size > 1) Text(parts[1], style = MaterialTheme.typography.labelLarge, color = if (i == selectedDay) SecondaryColor else TextPrimary, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            Text(
                "Miércoles, 15 de Junio",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)
            )

            appointments.forEach { appt ->
                AppointmentRow(appt, onJoin = onJoinMeeting)
            }

            Spacer(Modifier.height(80.dp))
        }
    }
}

@Composable
private fun AppointmentRow(appt: Appointment, onJoin: () -> Unit) {
    val isActive = appt.status == AppointmentStatus.NOW
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.Top
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                modifier = Modifier
                    .size(12.dp)
                    .clip(CircleShape)
                    .background(if (isActive) PrimaryColor else TertiaryColor)
            )
            Box(
                modifier = Modifier
                    .width(2.dp)
                    .height(80.dp)
                    .background(TertiaryColor)
            )
        }

        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            color = if (isActive) BackgroundColor else SurfaceColor,
            border = if (isActive) androidx.compose.foundation.BorderStroke(1.5.dp, PrimaryColor) else null,
            shadowElevation = if (isActive) 4.dp else 0.dp
        ) {
            Column(
                modifier = Modifier.padding(14.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        "${appt.timeRange}${if (isActive) " • AHORA" else ""}",
                        style = MaterialTheme.typography.labelSmall,
                        color = if (isActive) PrimaryDark else TextSecondary,
                        fontWeight = if (isActive) FontWeight.Bold else FontWeight.Normal
                    )
                    if (appt.status == AppointmentStatus.PAST) {
                        Text("PASADO", style = MaterialTheme.typography.labelSmall, color = TextSecondary)
                    }
                }
                Text(appt.company, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Text(appt.position, style = MaterialTheme.typography.bodySmall, color = TextSecondary)
                if (appt.person.isNotEmpty()) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        ProfileAvatar(appt.person.take(2), size = 22.dp)
                        Text(appt.person, style = MaterialTheme.typography.bodySmall)
                    }
                }
                if (isActive) {
                    JoviPrimaryButton(
                        text = "Unirse",
                        onClick = onJoin,
                        leadingIcon = Icons.Outlined.Videocam,
                    )
                } else if (appt.status == AppointmentStatus.SOON) {
                    JoviSecondaryButton(
                        text = "Unirse pronto",
                        onClick = {},
                        leadingIcon = Icons.Outlined.Videocam,
                    )
                }
            }
        }
    }
}
