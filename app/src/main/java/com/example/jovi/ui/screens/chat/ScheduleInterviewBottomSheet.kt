package com.example.jovi.ui.screens.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.jovi.ui.components.*
import com.example.jovi.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScheduleInterviewBottomSheet(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
) {
    var selectedDay by remember { mutableIntStateOf(14) }
    var selectedTime by remember { mutableStateOf("10:30 AM") }
    var note by remember { mutableStateOf("") }

    val days = listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18)
    val weekDays = listOf("DOM", "LUN", "MAR", "MIE", "JUE", "VIE", "SAB")
    val times = listOf("09:00 AM", "10:30 AM", "01:00 PM", "03:00 PM")

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        containerColor = BackgroundColor,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(bottom = 32.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onDismiss) {
                    Icon(Icons.Default.Close, contentDescription = "Cerrar")
                }
                Text("Agendar Entrevista", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Spacer(Modifier.width(48.dp))
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = {}) { Icon(Icons.Default.ChevronLeft, contentDescription = null) }
                Text("Noviembre 2023", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                IconButton(onClick = {}) { Icon(Icons.Default.ChevronRight, contentDescription = null) }
            }

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                weekDays.forEach { day ->
                    Text(day, style = MaterialTheme.typography.labelSmall, color = TextSecondary, modifier = Modifier.weight(1f), textAlign = TextAlign.Center)
                }
            }

            val rows = days.chunked(7)
            rows.forEach { row ->
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    row.forEach { day ->
                        Box(
                            modifier = Modifier
                                .size(38.dp)
                                .clip(CircleShape)
                                .background(if (day == selectedDay) PrimaryColor else if (day == 12 || day == 15) BackgroundColor else BackgroundColor)
                                .border(if (day == 12 || day == 15) 1.5.dp else 0.dp, if (day == 12 || day == 15) PrimaryColor else BackgroundColor, CircleShape)
                                .clickable { selectedDay = day },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                "$day",
                                style = MaterialTheme.typography.bodyMedium,
                                color = if (day == selectedDay) SecondaryColor else TextPrimary,
                                fontWeight = if (day == selectedDay) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    }
                    repeat(7 - row.size) { Spacer(Modifier.size(38.dp)) }
                }
            }

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Horarios Disponibles", style = MaterialTheme.typography.labelLarge)
                    Text("Zona: EST", style = MaterialTheme.typography.labelSmall, color = TextSecondary)
                }
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    times.forEach { time ->
                        Surface(
                            onClick = { selectedTime = time },
                            shape = RoundedCornerShape(50),
                            color = if (time == selectedTime) PrimaryColor else SurfaceColor,
                            border = if (time != selectedTime) androidx.compose.foundation.BorderStroke(1.dp, TertiaryColor) else null,
                        ) {
                            Text(
                                time,
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                                style = MaterialTheme.typography.labelMedium,
                                color = if (time == selectedTime) SecondaryColor else TextPrimary
                            )
                        }
                    }
                }
            }

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Nota o enlace de reunión", style = MaterialTheme.typography.labelLarge)
                JoviTextField(
                    value = note,
                    onValueChange = { note = it },
                    placeholder = "Agrega un mensaje para el reclutador o enlace de Zoom/Meet...",
                    singleLine = false,
                    minLines = 3,
                )
            }

            JoviPrimaryButton(
                text = "Confirmar Entrevista",
                onClick = onConfirm,
                trailingIcon = Icons.Default.ChevronRight,
            )

            Text(
                "Al confirmar, tú y el reclutador recibirán una invitación de calendario.",
                style = MaterialTheme.typography.bodySmall,
                color = TextSecondary,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
