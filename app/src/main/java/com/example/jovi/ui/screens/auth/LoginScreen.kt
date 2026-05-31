package com.example.jovi.ui.screens.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.jovi.data.db.entity.AccountType
import com.example.jovi.ui.components.*
import com.example.jovi.ui.theme.*
import com.example.jovi.viewmodel.AuthState
import com.example.jovi.viewmodel.AuthViewModel

@Composable
fun LoginScreen(
    authViewModel: AuthViewModel,
    onLoginSuccess: () -> Unit,
    onLoginAsRecruiter: () -> Unit,
    onRegister: () -> Unit,
    onGuest: () -> Unit,
    onForgotPassword: () -> Unit,
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val authState by authViewModel.authState.collectAsState()

    LaunchedEffect(authState) {
        when (val s = authState) {
            is AuthState.Success -> {
                if (s.user.accountType == AccountType.RECRUITER) onLoginAsRecruiter()
                else onLoginSuccess()
            }
            else -> {}
        }
    }

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
        Spacer(Modifier.height(24.dp))

        Surface(modifier = Modifier.size(140.dp), shape = CircleShape, color = PrimaryLight) {
            Box(contentAlignment = Alignment.Center) {
                Icon(Icons.Outlined.Person, contentDescription = null, modifier = Modifier.size(64.dp), tint = SecondaryColor)
            }
        }
        Spacer(Modifier.height(8.dp))
        Text(
            "carlos@example.com / carlos123",
            style = MaterialTheme.typography.labelSmall,
            color = TextSecondary,
            textAlign = TextAlign.Center
        )
        Text(
            "ana@example.com / ana123  ·  hr@innovatech.com / innovatech123",
            style = MaterialTheme.typography.labelSmall,
            color = TextSecondary,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(24.dp))

        if (authState is AuthState.Error) {
            Surface(
                shape = RoundedCornerShape(10.dp),
                color = ErrorColor.copy(0.1f),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    (authState as AuthState.Error).message,
                    modifier = Modifier.padding(12.dp),
                    color = ErrorColor,
                    style = MaterialTheme.typography.bodySmall,
                )
            }
            Spacer(Modifier.height(12.dp))
        }

        JoviTextField(
            value = email,
            onValueChange = { email = it; authViewModel.clearError() },
            placeholder = "Correo electrónico",
            leadingIcon = Icons.Outlined.Email,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
        )
        Spacer(Modifier.height(14.dp))
        JoviTextField(
            value = password,
            onValueChange = { password = it; authViewModel.clearError() },
            placeholder = "Contraseña",
            leadingIcon = Icons.Outlined.Lock,
            isPassword = true
        )
        Spacer(Modifier.height(8.dp))
        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterEnd) {
            TextButton(onClick = onForgotPassword) {
                Text("¿Olvidaste la contraseña?", color = TextSecondary, style = MaterialTheme.typography.bodySmall)
            }
        }
        Spacer(Modifier.height(20.dp))

        if (authState is AuthState.Loading) {
            CircularProgressIndicator(color = PrimaryDark, modifier = Modifier.size(36.dp))
        } else {
            JoviPrimaryButton(text = "Ingresar", onClick = { authViewModel.login(email, password) })
        }

        Spacer(Modifier.height(16.dp))
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
            HorizontalDivider(modifier = Modifier.weight(1f), color = TertiaryColor)
            Text("  ó  ", color = TextSecondary, style = MaterialTheme.typography.bodyMedium)
            HorizontalDivider(modifier = Modifier.weight(1f), color = TertiaryColor)
        }
        Spacer(Modifier.height(16.dp))
        JoviSecondaryButton(text = "Crear una cuenta", onClick = onRegister, leadingIcon = Icons.Outlined.PersonAdd)
        Spacer(Modifier.height(12.dp))
        JoviPrimaryButton(text = "Continuar como Invitado", onClick = onGuest, leadingIcon = Icons.Outlined.Person)
        Spacer(Modifier.height(32.dp))
    }
}
