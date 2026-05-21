package com.example.jovi.ui.screens.academic

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.unit.dp
import com.example.jovi.ui.components.*
import com.example.jovi.ui.theme.*

@Composable
fun PublishVacancyScreen(
    onBack: () -> Unit,
    onPublish: () -> Unit,
) {
    var vacancyType by remember { mutableStateOf("Internship") }
    var projectTitle by remember { mutableStateOf("") }
    var vacancies by remember { mutableStateOf("") }
    var stipend by remember { mutableStateOf("") }
    var careerSearch by remember { mutableStateOf("") }
    val selectedCareers = remember { mutableStateListOf("Biology", "Laboratory") }
    val types = listOf("Servicio", "Internship", "Beca")

    Scaffold(
        topBar = { JoviTopBar(title = "Nueva Vacante", onBack = onBack) },
        containerColor = BackgroundColor,
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            ProgressStepBar(current = 1, total = 2)

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("PASO 1 DE 2", style = MaterialTheme.typography.labelSmall, color = PrimaryDark, fontWeight = FontWeight.Bold)
                Text("50% Completado", style = MaterialTheme.typography.labelSmall, color = TextSecondary)
            }

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Tipo de Vacante", style = MaterialTheme.typography.labelLarge)
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    types.forEach { type ->
                        Surface(
                            onClick = { vacancyType = type },
                            shape = RoundedCornerShape(50),
                            color = if (type == vacancyType) SecondaryColor else BackgroundColor,
                            border = androidx.compose.foundation.BorderStroke(
                                1.5.dp,
                                if (type == vacancyType) SecondaryColor else TertiaryColor
                            )
                        ) {
                            Text(
                                type,
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                                style = MaterialTheme.typography.labelMedium,
                                color = if (type == vacancyType) BackgroundColor else TextPrimary
                            )
                        }
                    }
                }
            }

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Título del Proyecto", style = MaterialTheme.typography.labelLarge)
                JoviTextField(
                    value = projectTitle,
                    onValueChange = { projectTitle = it },
                    placeholder = "e.g. Research Assistant en Biología",
                    leadingIcon = Icons.Outlined.TextFields,
                )
            }

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Vacantes", style = MaterialTheme.typography.labelLarge)
                    OutlinedTextField(
                        value = vacancies,
                        onValueChange = { vacancies = it },
                        placeholder = { Text("0", color = TextSecondary) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = PrimaryColor, unfocusedBorderColor = TertiaryColor),
                        leadingIcon = { Icon(Icons.Outlined.People, contentDescription = null, tint = TextSecondary) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                    )
                }
                Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Estipendio (Mensual)", style = MaterialTheme.typography.labelLarge)
                    OutlinedTextField(
                        value = stipend,
                        onValueChange = { stipend = it },
                        placeholder = { Text("0.00", color = TextSecondary) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = PrimaryColor, unfocusedBorderColor = TertiaryColor),
                        leadingIcon = { Icon(Icons.Outlined.AttachMoney, contentDescription = null, tint = TextSecondary) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        singleLine = true,
                    )
                }
            }

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Text("Carreras Objetivo", style = MaterialTheme.typography.labelLarge)
                    TextButton(onClick = {}) { Text("Agregar Nueva", color = PrimaryDark) }
                }
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, TertiaryColor)
                ) {
                    Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            selectedCareers.forEach { career ->
                                SkillChip(label = career, onRemove = { selectedCareers.remove(career) })
                            }
                        }
                        OutlinedTextField(
                            value = careerSearch,
                            onValueChange = { careerSearch = it },
                            placeholder = { Text("Escribir para buscar...", color = TextSecondary) },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(10.dp),
                            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = TertiaryColor, unfocusedBorderColor = TertiaryColor),
                            singleLine = true,
                        )
                    }
                }
                Text(
                    "Selecciona áreas de estudio o carreras relevantes para conectar mejor con candidatos.",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondary
                )
            }

            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                color = PrimaryLight
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Outlined.School, contentDescription = null, tint = PrimaryDark, modifier = Modifier.size(28.dp))
                    Column {
                        Text("PRO TIP", style = MaterialTheme.typography.labelSmall, color = PrimaryDark, fontWeight = FontWeight.Bold)
                        Text("Las descripciones detalladas atraen mejores candidatos.", style = MaterialTheme.typography.bodySmall, color = TextSecondary)
                    }
                }
            }

            Spacer(Modifier.height(8.dp))

            JoviPrimaryButton(
                text = "Publicar Vacante",
                onClick = onPublish,
                trailingIcon = Icons.Default.ChevronRight,
            )

            Spacer(Modifier.height(16.dp))
        }
    }
}
