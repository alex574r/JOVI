package com.example.jovi.ui.screens.auth

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.PersonAdd
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.jovi.ui.components.*
import com.example.jovi.ui.theme.*

@Composable
fun LoginScreen(
    onLogin: () -> Unit,
    onRegister: () -> Unit,
    onGuest: () -> Unit,
    onRecruiterLogin: () -> Unit,
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundColor)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 28.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(Modifier.height(56.dp))

        Text(
            "Iniciar sesión",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(32.dp))

        // Placeholder illustration area
        Surface(
            modifier = Modifier.size(180.dp),
            shape = androidx.compose.foundation.shape.CircleShape,
            color = PrimaryLight
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    Icons.Outlined.Person,
                    contentDescription = null,
                    modifier = Modifier.size(80.dp),
                    tint = SecondaryColor
                )
            }
        }

        Spacer(Modifier.height(40.dp))

        JoviTextField(
            value = email,
            onValueChange = { email = it },
            placeholder = "Correo electrónico",
            leadingIcon = Icons.Outlined.Email,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
        )

        Spacer(Modifier.height(14.dp))

        JoviTextField(
            value = password,
            onValueChange = { password = it },
            placeholder = "Contraseña",
            leadingIcon = Icons.Outlined.Lock,
            isPassword = true,
        )

        Spacer(Modifier.height(8.dp))

        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterEnd) {
            TextButton(onClick = {}) {
                Text("Olvidaste la contraseña?", color = TextSecondary, style = MaterialTheme.typography.bodySmall)
            }
        }

        Spacer(Modifier.height(20.dp))

        JoviPrimaryButton(text = "Ingresar", onClick = onLogin)

        Spacer(Modifier.height(20.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Divider(modifier = Modifier.weight(1f), color = TertiaryColor)
            Text("  ó  ", color = TextSecondary, style = MaterialTheme.typography.bodyMedium)
            Divider(modifier = Modifier.weight(1f), color = TertiaryColor)
        }

        Spacer(Modifier.height(20.dp))

        JoviSecondaryButton(
            text = "Crear una cuenta",
            onClick = onRegister,
            leadingIcon = Icons.Outlined.PersonAdd,
        )

        Spacer(Modifier.height(12.dp))

        JoviPrimaryButton(
            text = "Continuar como Invitado",
            onClick = onGuest,
            leadingIcon = Icons.Outlined.Person,
        )

        Spacer(Modifier.height(12.dp))

        TextButton(onClick = onRecruiterLogin) {
            Text("Acceder como Reclutador / Institución", color = SecondaryColor, style = MaterialTheme.typography.bodySmall)
        }

        Spacer(Modifier.height(32.dp))
    }
}
