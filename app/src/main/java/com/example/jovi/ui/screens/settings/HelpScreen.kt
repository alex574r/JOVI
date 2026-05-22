package com.example.jovi.ui.screens.settings

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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.jovi.ui.components.JoviTopBar
import com.example.jovi.ui.theme.*

@Composable
fun HelpScreen(onBack: () -> Unit) {
    val faqs = listOf(
        "Como funciona el sistema de matches?" to "Cuando tu perfil y una vacante coinciden mutuamente en interes, se genera un match. Ambas partes deben deslizar a la derecha (o dar like) para que ocurra el match.",
        "Como verifico mi perfil biometrico?" to "Ve a Configuracion > Verificacion biometrica. Podras usar reconocimiento facial o huella dactilar segun las capacidades de tu dispositivo.",
        "Puedo usar la app sin crear una cuenta?" to "Si, puedes explorar vacantes como invitado. Sin embargo, para aplicar, chatear y hacer match necesitaras crear una cuenta.",
        "Como funciona el sistema de rachas?" to "Cada dia que abres la app y realizas al menos una accion (aplicar, hacer swipe, publicar) suma un dia a tu racha. Mantenerla te sube en el leaderboard.",
        "Como elimino mi cuenta?" to "Ve a Ajustes > Privacidad > Eliminar cuenta. Esta accion es irreversible y eliminara todos tus datos.",
    )

    Scaffold(
        containerColor = BackgroundColor,
        topBar = { JoviTopBar(title = "Ayuda y Soporte", onBack = onBack) }
    ) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(0.dp),
        ) {
            // Contact options
            Row(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                ContactCard(Icons.Outlined.Email, "Email", "soporte@jovi.app", modifier = Modifier.weight(1f))
                ContactCard(Icons.Outlined.Chat, "Chat en vivo", "Disponible 9-18h", modifier = Modifier.weight(1f))
            }

            HorizontalDivider(thickness = 8.dp, color = SurfaceColor)
            Spacer(Modifier.height(8.dp))

            Text(
                "Preguntas frecuentes",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            )

            faqs.forEachIndexed { index, (question, answer) ->
                FaqItem(question = question, answer = answer)
                if (index < faqs.lastIndex) HorizontalDivider(color = DividerColor)
            }

            Spacer(Modifier.height(16.dp))

            Surface(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                shape = RoundedCornerShape(14.dp),
                color = PrimaryLight,
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text("No encontraste tu respuesta?", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold, color = PrimaryDark)
                    Text("Nuestro equipo responde en menos de 24 horas habiles.", style = MaterialTheme.typography.bodySmall, color = PrimaryDark.copy(0.7f))
                    TextButton(onClick = {}, contentPadding = PaddingValues(0.dp)) {
                        Text("Enviar ticket de soporte", color = PrimaryDark, fontWeight = FontWeight.Bold)
                    }
                }
            }

            Spacer(Modifier.height(16.dp))
        }
    }
}

@Composable
private fun ContactCard(icon: ImageVector, label: String, subtitle: String, modifier: Modifier = Modifier) {
    Surface(modifier = modifier, shape = RoundedCornerShape(14.dp), color = SurfaceColor) {
        Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Icon(icon, contentDescription = null, tint = PrimaryDark, modifier = Modifier.size(22.dp))
            Text(label, style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.SemiBold)
            Text(subtitle, style = MaterialTheme.typography.labelSmall, color = TextSecondary)
        }
    }
}

@Composable
private fun FaqItem(question: String, answer: String) {
    var expanded by remember { mutableStateOf(false) }
    Surface(onClick = { expanded = !expanded }, color = BackgroundColor) {
        Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 14.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.Top) {
                Text(question, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Medium, modifier = Modifier.weight(1f))
                Icon(
                    if (expanded) Icons.Outlined.KeyboardArrowUp else Icons.Outlined.KeyboardArrowDown,
                    contentDescription = null, tint = TextSecondary, modifier = Modifier.size(20.dp)
                )
            }
            if (expanded) {
                Spacer(Modifier.height(6.dp))
                Text(answer, style = MaterialTheme.typography.bodySmall, color = TextSecondary)
            }
        }
    }
}
