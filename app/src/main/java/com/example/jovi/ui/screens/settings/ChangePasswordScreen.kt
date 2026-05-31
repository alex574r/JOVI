package com.example.jovi.ui.screens.settings

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.jovi.ui.components.*
import com.example.jovi.ui.theme.*
import com.example.jovi.viewmodel.AuthViewModel

@Composable
fun ChangePasswordScreen(
    authViewModel: AuthViewModel,
    onBack: () -> Unit,
) {
    var current by remember { mutableStateOf("") }
    var newPass by remember { mutableStateOf("") }
    var confirm by remember { mutableStateOf("") }
    var error by remember { mutableStateOf("") }
    var success by remember { mutableStateOf(false) }
    val user by authViewModel.currentUser.collectAsState()

    Scaffold(
        topBar = { JoviTopBar(title = "Cambiar contraseña", onBack = onBack) },
        containerColor = BackgroundColor,
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Spacer(Modifier.height(8.dp))
            if (error.isNotEmpty()) {
                Text(error, color = ErrorColor, style = MaterialTheme.typography.bodySmall)
            }
            if (success) {
                Text("¡Contraseña actualizada correctamente!", color = PrimaryDark, style = MaterialTheme.typography.bodySmall)
            }
            JoviTextField(value = current, onValueChange = { current = it; error = "" }, placeholder = "Contraseña actual", leadingIcon = Icons.Outlined.Lock, isPassword = true)
            JoviTextField(value = newPass, onValueChange = { newPass = it; error = "" }, placeholder = "Nueva contraseña", leadingIcon = Icons.Outlined.Lock, isPassword = true)
            JoviTextField(value = confirm, onValueChange = { confirm = it; error = "" }, placeholder = "Confirmar nueva contraseña", leadingIcon = Icons.Outlined.Lock, isPassword = true)
            Spacer(Modifier.height(8.dp))
            JoviPrimaryButton(text = "Guardar cambios", onClick = {
                val u = user ?: return@JoviPrimaryButton
                when {
                    current != u.password -> error = "La contraseña actual es incorrecta"
                    newPass.length < 6 -> error = "La nueva contraseña debe tener al menos 6 caracteres"
                    newPass != confirm -> error = "Las contraseñas no coinciden"
                    else -> {
                        authViewModel.updatePassword(newPass)
                        success = true
                        current = ""; newPass = ""; confirm = ""
                    }
                }
            })
        }
    }
}
