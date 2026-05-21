package com.example.jovi.ui.screens.contract

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Download
import androidx.compose.material.icons.outlined.Draw
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.jovi.ui.components.*
import com.example.jovi.ui.theme.*

private val contractSections = listOf(
    "1. ROLES Y RESPONSABILIDADES" to "El practicante desempeñará las funciones establecidas en la descripción de puesto de acuerdo con un código de conducta profesional. Las responsabilidades incluyen participación activa en reuniones de equipo, entrega puntual de tareas asignadas y alineación con los flujos de trabajo de la empresa.",
    "2. COMPENSACIÓN Y HORARIO" to "Las prácticas son un puesto de tiempo completo (40 horas/semana). La compensación se establece según la tarifa acordada por hora, pagadera quincenal. El tiempo extra debe ser preaprobado por el supervisor directo.",
    "3. CONFIDENCIALIDAD" to "El practicante acepta mantener la confidencialidad de toda la información propietaria, secretos comerciales y datos internos de la empresa durante y después del período de prácticas. La divulgación no autorizada puede resultar en acción legal.",
    "4. PROPIEDAD INTELECTUAL" to "Cualquier trabajo, invención o creación producida durante el período de prácticas es propiedad exclusiva de la empresa.",
)

@Composable
fun DigitalContractScreen(
    contractTitle: String = "Acuerdo de Prácticas",
    onBack: () -> Unit,
    onSign: () -> Unit,
) {
    Scaffold(
        topBar = {
            JoviTopBar(
                title = contractTitle,
                onBack = onBack,
                actions = {
                    IconButton(onClick = {}) {
                        Icon(Icons.Outlined.Download, contentDescription = "Descargar", tint = TextPrimary)
                    }
                }
            )
        },
        containerColor = BackgroundColor,
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Text(
                "Revisar y Firmar",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                "Por favor, lee el siguiente acuerdo de prácticas detenidamente. Tu firma a continuación confirma tu aceptación de estos términos.",
                style = MaterialTheme.typography.bodyMedium,
                color = TextSecondary
            )

            contractSections.forEach { (title, body) ->
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(title, style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold, color = TextPrimary)
                    Text(body, style = MaterialTheme.typography.bodyMedium, color = TextSecondary)
                }
                Divider(color = DividerColor)
            }

            Spacer(Modifier.height(8.dp))

            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                color = PrimaryLight,
                border = androidx.compose.foundation.BorderStroke(1.dp, PrimaryColor)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(Icons.Outlined.Draw, contentDescription = null, tint = PrimaryDark, modifier = Modifier.size(28.dp))
                    Spacer(Modifier.height(6.dp))
                    Text("Toca para firmar aquí", style = MaterialTheme.typography.bodyMedium, color = PrimaryDark)
                }
            }

            JoviPrimaryButton(
                text = "Confirmar y Enviar Firma",
                onClick = onSign,
                leadingIcon = Icons.Outlined.Draw,
            )

            Spacer(Modifier.height(16.dp))
        }
    }
}
