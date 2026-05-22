package com.example.jovi.ui.screens.profile

import androidx.compose.foundation.background
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.unit.dp
import com.example.jovi.ui.components.*
import com.example.jovi.ui.theme.*

@Composable
fun EditProfileScreen(onBack: () -> Unit, onSave: () -> Unit) {
    var displayName by remember { mutableStateOf("Carlos Mendoza") }
    var username by remember { mutableStateOf("carlos_dev") }
    var bio by remember { mutableStateOf("Software developer apasionado por la tecnologia.") }
    var university by remember { mutableStateOf("UNAM") }
    var location by remember { mutableStateOf("CDMX") }
    var email by remember { mutableStateOf("carlos@example.com") }
    var phone by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            JoviTopBar(
                title = "Editar perfil",
                onBack = onBack,
                actions = {
                    TextButton(onClick = onSave) {
                        Text("Guardar", color = PrimaryDark, fontWeight = FontWeight.Bold)
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
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Spacer(Modifier.height(8.dp))

            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Box(
                        modifier = Modifier.size(88.dp).clip(CircleShape).background(SecondaryColor),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("CM", color = PrimaryColor, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                    }
                    TextButton(onClick = {}) {
                        Text("Cambiar foto", color = PrimaryDark, style = MaterialTheme.typography.labelMedium)
                    }
                }
            }

            SectionHeader(title = "Informacion publica")

            JoviTextField(value = displayName, onValueChange = { displayName = it }, placeholder = "Nombre completo", leadingIcon = Icons.Outlined.Person)
            JoviTextField(value = username, onValueChange = { username = it }, placeholder = "Nombre de usuario", leadingIcon = Icons.Outlined.AlternateEmail)
            JoviTextField(value = bio, onValueChange = { bio = it }, placeholder = "Biografia", leadingIcon = Icons.Outlined.Edit)

            SectionHeader(title = "Informacion academica / laboral")

            JoviTextField(value = university, onValueChange = { university = it }, placeholder = "Universidad / Empresa", leadingIcon = Icons.Outlined.School)
            JoviTextField(value = location, onValueChange = { location = it }, placeholder = "Ubicacion", leadingIcon = Icons.Outlined.LocationOn)

            SectionHeader(title = "Contacto privado")

            JoviTextField(value = email, onValueChange = { email = it }, placeholder = "Correo electronico", leadingIcon = Icons.Outlined.Email, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email))
            JoviTextField(value = phone, onValueChange = { phone = it }, placeholder = "Telefono (opcional)", leadingIcon = Icons.Outlined.Phone, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone))

            Spacer(Modifier.height(8.dp))
            JoviPrimaryButton(text = "Guardar cambios", onClick = onSave)
            Spacer(Modifier.height(32.dp))
        }
    }
}
