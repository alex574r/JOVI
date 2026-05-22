package com.example.jovi.ui.screens.auth

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.jovi.ui.components.*
import com.example.jovi.ui.theme.*

@Composable
fun ForgotPasswordScreen(onBack: () -> Unit) {
    var email by remember { mutableStateOf("") }
    var sent by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundColor)
            .padding(horizontal = 28.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(Modifier.height(16.dp))
        JoviTopBar(title = "Recuperar contraseña", onBack = onBack)
        Spacer(Modifier.height(48.dp))

        AnimatedContent(targetState = sent, label = "sent") { isSent ->
            if (!isSent) {
                Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(20.dp)) {
                    Box(
                        modifier = Modifier.size(88.dp).clip(CircleShape).background(PrimaryLight),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Outlined.Lock, contentDescription = null, tint = SecondaryColor, modifier = Modifier.size(44.dp))
                    }
                    Text("Ingresa tu correo y te enviaremos un enlace para restablecer tu contraseña.", style = MaterialTheme.typography.bodyMedium, color = TextSecondary, textAlign = TextAlign.Center)
                    JoviTextField(value = email, onValueChange = { email = it }, placeholder = "Correo electronico", leadingIcon = Icons.Outlined.Email, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email))
                    JoviPrimaryButton(text = "Enviar enlace", onClick = { if (email.isNotBlank()) sent = true })
                }
            } else {
                Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(20.dp)) {
                    Box(
                        modifier = Modifier.size(88.dp).clip(CircleShape).background(PrimaryLight),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Outlined.MarkEmailRead, contentDescription = null, tint = PrimaryDark, modifier = Modifier.size(44.dp))
                    }
                    Text("Enlace enviado", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                    Text("Revisa tu correo $email y sigue las instrucciones para restablecer tu contraseña.", style = MaterialTheme.typography.bodyMedium, color = TextSecondary, textAlign = TextAlign.Center)
                    JoviSecondaryButton(text = "Volver al inicio", onClick = onBack)
                }
            }
        }
    }
}
