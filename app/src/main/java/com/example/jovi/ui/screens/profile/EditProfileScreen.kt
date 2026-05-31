package com.example.jovi.ui.screens.profile

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.jovi.ui.components.*
import com.example.jovi.ui.theme.*
import com.example.jovi.viewmodel.AuthViewModel

@Composable
fun EditProfileScreen(
    authViewModel: AuthViewModel,
    onBack: () -> Unit,
    onSave: () -> Unit,
) {
    val user by authViewModel.currentUser.collectAsState()
    var displayName by remember(user?.id) { mutableStateOf(user?.displayName ?: "") }
    var bio by remember(user?.id) { mutableStateOf(user?.bio ?: "") }
    var university by remember(user?.id) { mutableStateOf(user?.university ?: "") }
    var location by remember(user?.id) { mutableStateOf(user?.location ?: "") }
    var headline by remember(user?.id) { mutableStateOf(user?.headline ?: "") }

    Scaffold(
        topBar = { JoviTopBar(title = "Editar perfil", onBack = onBack) },
        containerColor = BackgroundColor,
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Spacer(Modifier.height(8.dp))
            JoviTextField(value = displayName, onValueChange = { displayName = it }, placeholder = "Nombre completo", leadingIcon = Icons.Outlined.Person)
            JoviTextField(value = headline, onValueChange = { headline = it }, placeholder = "Título profesional (ej. Android Developer)", leadingIcon = Icons.Outlined.Work)
            JoviTextField(value = bio, onValueChange = { bio = it }, placeholder = "Sobre mí", leadingIcon = Icons.Outlined.Info)
            JoviTextField(value = university, onValueChange = { university = it }, placeholder = "Universidad", leadingIcon = Icons.Outlined.School)
            JoviTextField(value = location, onValueChange = { location = it }, placeholder = "Ciudad, País", leadingIcon = Icons.Outlined.LocationOn)
            Spacer(Modifier.height(8.dp))
            JoviPrimaryButton(text = "Guardar cambios", onClick = {
                val u = user ?: return@JoviPrimaryButton
                authViewModel.updateProfile(
                    u.copy(
                        displayName = displayName,
                        bio = bio,
                        university = university,
                        location = location,
                        headline = headline,
                    )
                )
                onSave()
            })
        }
    }
}
