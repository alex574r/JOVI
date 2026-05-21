package com.example.jovi.ui.screens.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CloudUpload
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.jovi.ui.components.*
import com.example.jovi.ui.theme.*

@Composable
fun RegistrationProfessionalInfoScreen(
    onBack: () -> Unit,
    onComplete: () -> Unit,
) {
    var skillSearch by remember { mutableStateOf("") }
    var experience by remember { mutableStateOf("") }
    val selectedSkills = remember { mutableStateListOf("UX Design", "Prototyping", "Wireframing") }

    Scaffold(
        topBar = { JoviTopBar(title = "Información Profesional", onBack = onBack) },
        containerColor = BackgroundColor,
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            ProgressStepBar(current = 2, total = 3)

            Spacer(Modifier.height(4.dp))

            Text(
                "Cuéntanos sobre tu trabajo",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold
            )
            Text(
                "Agrega tus habilidades y experiencia para ser conectado con las oportunidades perfectas.",
                style = MaterialTheme.typography.bodyMedium,
                color = TextSecondary
            )

            Spacer(Modifier.height(4.dp))

            Text("Habilidades", style = MaterialTheme.typography.labelLarge)
            JoviTextField(
                value = skillSearch,
                onValueChange = { skillSearch = it },
                placeholder = "Buscar habilidades (e.g. Figma, Python)",
                trailingIcon = Icons.Outlined.Search,
            )

            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                selectedSkills.forEach { skill ->
                    SkillChip(label = skill, onRemove = { selectedSkills.remove(skill) })
                }
                Surface(
                    onClick = {},
                    shape = RoundedCornerShape(50),
                    color = BackgroundColor,
                    border = androidx.compose.foundation.BorderStroke(1.dp, TertiaryColor),
                    modifier = Modifier.padding(2.dp)
                ) {
                    Text(
                        "+ Agregar más",
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        style = MaterialTheme.typography.labelMedium,
                        color = TextSecondary
                    )
                }
            }

            Text("Conocimientos / Experiencia", style = MaterialTheme.typography.labelLarge)
            JoviTextField(
                value = experience,
                onValueChange = { experience = it },
                placeholder = "Describe tus logros clave, responsabilidades y áreas de expertise...",
                singleLine = false,
                minLines = 4,
            )

            Text("CV / Currículum", style = MaterialTheme.typography.labelLarge)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .border(1.5.dp, PrimaryColor, RoundedCornerShape(14.dp))
                    .background(PrimaryLight)
                    .padding(vertical = 28.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Icon(Icons.Outlined.CloudUpload, contentDescription = null, tint = PrimaryDark, modifier = Modifier.size(32.dp))
                    Text(
                        "Clic para subir o arrastra y suelta",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextPrimary
                    )
                    Text("PDF, DOCX (MÁX. 5MB)", style = MaterialTheme.typography.bodySmall, color = TextSecondary)
                }
            }

            Spacer(Modifier.height(8.dp))

            JoviPrimaryButton(
                text = "Completar Registro",
                onClick = onComplete,
                trailingIcon = Icons.Outlined.CloudUpload,
            )

            Spacer(Modifier.height(16.dp))
        }
    }
}
