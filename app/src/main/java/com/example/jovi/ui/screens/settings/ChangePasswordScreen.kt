package com.example.jovi.ui.screens.settings

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.jovi.ui.components.JoviPrimaryButton
import com.example.jovi.ui.components.JoviTextField
import com.example.jovi.ui.components.JoviTopBar
import com.example.jovi.ui.theme.*

@Composable
fun ChangePasswordScreen(onBack: () -> Unit) {
    var currentPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var success by remember { mutableStateOf(false) }

    Scaffold(
        containerColor = BackgroundColor,
        topBar = { JoviTopBar(title = "Cambiar Contraseña", onBack = onBack) }
    ) { padding ->
        AnimatedContent(
            targetState = success,
            transitionSpec = { fadeIn(tween(300)).togetherWith(fadeOut(tween(300))) },
            modifier = Modifier.fillMaxSize().padding(padding),
            label = "password_success"
        ) { done ->
            if (done) {
                Column(
                    modifier = Modifier.fillMaxSize().padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                ) {
                    Surface(shape = CircleShape, color = StatusAccepted.copy(0.15f), modifier = Modifier.size(80.dp)) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(Icons.Outlined.LockOpen, contentDescription = null, tint = StatusAccepted, modifier = Modifier.size(36.dp))
                        }
                    }
                    Spacer(Modifier.height(20.dp))
                    Text("Contraseña actualizada", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                    Text("Tu contraseña ha sido cambiada exitosamente.", style = MaterialTheme.typography.bodyMedium, color = TextSecondary)
                    Spacer(Modifier.height(28.dp))
                    JoviPrimaryButton(text = "Volver", onClick = onBack)
                }
            } else {
                Column(
                    modifier = Modifier.fillMaxSize().padding(24.dp).verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    Surface(shape = CircleShape, color = PrimaryLight, modifier = Modifier.size(64.dp).align(Alignment.CenterHorizontally)) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(Icons.Outlined.Lock, contentDescription = null, tint = PrimaryDark, modifier = Modifier.size(28.dp))
                        }
                    }
                    Spacer(Modifier.height(8.dp))
                    JoviTextField(value = currentPassword, onValueChange = { currentPassword = it }, placeholder = "Contraseña actual", leadingIcon = Icons.Outlined.Lock, isPassword = true)
                    JoviTextField(value = newPassword, onValueChange = { newPassword = it }, placeholder = "Nueva contraseña", leadingIcon = Icons.Outlined.LockOpen, isPassword = true)
                    JoviTextField(value = confirmPassword, onValueChange = { confirmPassword = it }, placeholder = "Confirmar nueva contraseña", leadingIcon = Icons.Outlined.Lock, isPassword = true)

                    if (newPassword.isNotEmpty() && confirmPassword.isNotEmpty() && newPassword != confirmPassword) {
                        Text("Las contraseñas no coinciden", style = MaterialTheme.typography.labelSmall, color = StatusRejected)
                    }

                    Spacer(Modifier.height(8.dp))
                    JoviPrimaryButton(
                        text = "Actualizar contraseña",
                        onClick = { if (newPassword == confirmPassword && newPassword.isNotEmpty()) success = true },
                    )
                }
            }
        }
    }
}
