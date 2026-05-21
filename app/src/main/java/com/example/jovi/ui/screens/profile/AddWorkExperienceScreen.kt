package com.example.jovi.ui.screens.profile

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.jovi.ui.components.*
import com.example.jovi.ui.theme.*

@Composable
fun AddWorkExperienceScreen(
    onBack: () -> Unit,
    onSave: () -> Unit,
) {
    var jobTitle by remember { mutableStateOf("") }
    var companyName by remember { mutableStateOf("") }
    var employmentType by remember { mutableStateOf("") }
    var isCurrentJob by remember { mutableStateOf(true) }
    var startDate by remember { mutableStateOf("") }
    var endDate by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    val charCount = description.length

    Scaffold(
        topBar = { JoviTopBar(title = "Agregar Experiencia", onBack = onBack) },
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
            Text("Puesto de Trabajo", style = MaterialTheme.typography.labelLarge)
            JoviTextField(
                value = jobTitle,
                onValueChange = { jobTitle = it },
                placeholder = "e.g. Product Designer",
            )

            Text("Nombre de la Empresa", style = MaterialTheme.typography.labelLarge)
            JoviTextField(
                value = companyName,
                onValueChange = { companyName = it },
                placeholder = "e.g. Acme Corp",
                leadingIcon = Icons.Outlined.Business,
            )

            Text("Tipo de Empleo", style = MaterialTheme.typography.labelLarge)
            ExposedDropdownMenuBox(
                expanded = false,
                onExpandedChange = {}
            ) {
                OutlinedTextField(
                    value = employmentType.ifEmpty { "Seleccionar tipo" },
                    onValueChange = {},
                    readOnly = true,
                    modifier = Modifier.fillMaxWidth().menuAnchor(),
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = TertiaryColor,
                        focusedBorderColor = PrimaryColor,
                    ),
                    trailingIcon = { Icon(Icons.Outlined.ExpandMore, contentDescription = null) }
                )
            }

            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                color = SurfaceColor
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("Trabajo aquí actualmente", style = MaterialTheme.typography.labelLarge)
                        Text("Activa si este es tu puesto actual", style = MaterialTheme.typography.bodySmall, color = TextSecondary)
                    }
                    Switch(
                        checked = isCurrentJob,
                        onCheckedChange = { isCurrentJob = it },
                        colors = SwitchDefaults.colors(checkedThumbColor = BackgroundColor, checkedTrackColor = PrimaryColor)
                    )
                }
            }

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text("Fecha de Inicio", style = MaterialTheme.typography.labelLarge)
                    OutlinedTextField(
                        value = startDate,
                        onValueChange = { startDate = it },
                        placeholder = { Text("mm/dd/yyyy", color = TextSecondary) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        colors = OutlinedTextFieldDefaults.colors(unfocusedBorderColor = TertiaryColor, focusedBorderColor = PrimaryColor),
                        trailingIcon = { Icon(Icons.Outlined.CalendarMonth, contentDescription = null, tint = TextSecondary) },
                        singleLine = true,
                    )
                }
                Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text("Fecha de Fin", style = MaterialTheme.typography.labelLarge, color = if (isCurrentJob) TextSecondary else TextPrimary)
                    OutlinedTextField(
                        value = if (isCurrentJob) "Presente" else endDate,
                        onValueChange = { endDate = it },
                        placeholder = { Text("mm/dd/yyyy", color = TextSecondary) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        colors = OutlinedTextFieldDefaults.colors(unfocusedBorderColor = TertiaryColor, focusedBorderColor = PrimaryColor),
                        enabled = !isCurrentJob,
                        singleLine = true,
                    )
                }
            }

            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text("Descripción", style = MaterialTheme.typography.labelLarge)
                JoviTextField(
                    value = description,
                    onValueChange = { if (it.length <= 2000) description = it },
                    placeholder = "Describe tus responsabilidades y logros clave...",
                    singleLine = false,
                    minLines = 5,
                )
                Text("$charCount/2000", style = MaterialTheme.typography.labelSmall, color = TextSecondary, modifier = Modifier.align(Alignment.End))
            }

            Spacer(Modifier.height(8.dp))

            JoviPrimaryButton(
                text = "Guardar Experiencia",
                onClick = onSave,
                leadingIcon = Icons.Outlined.Check,
            )

            Spacer(Modifier.height(16.dp))
        }
    }
}
