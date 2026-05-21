package com.example.jovi.ui.screens.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.jovi.ui.components.*
import com.example.jovi.ui.theme.*

@Composable
fun RegistrationPersonalInfoScreen(
    onBack: () -> Unit,
    onNext: () -> Unit,
) {
    var firstName by remember { mutableStateOf("") }
    var fatherSurname by remember { mutableStateOf("") }
    var motherSurname by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }

    Scaffold(
        topBar = { JoviTopBar(title = "Información Personal", onBack = onBack) },
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
            ProgressStepBar(current = 1, total = 3)

            Spacer(Modifier.height(4.dp))

            Text(
                "Cuéntanos sobre ti",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold
            )
            Text(
                "Necesitamos tus datos para conectarte con las mejores oportunidades en tu área.",
                style = MaterialTheme.typography.bodyMedium,
                color = TextSecondary
            )

            Spacer(Modifier.height(4.dp))

            Text("Nombre", style = MaterialTheme.typography.labelLarge)
            JoviTextField(
                value = firstName,
                onValueChange = { firstName = it },
                placeholder = "Ingresa tu nombre",
                leadingIcon = Icons.Outlined.Person,
            )

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text("Apellido Paterno", style = MaterialTheme.typography.labelLarge)
                    OutlinedTextField(
                        value = fatherSurname,
                        onValueChange = { fatherSurname = it },
                        placeholder = { Text("Apellido", color = TextSecondary) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = androidx.compose.foundation.shape.RoundedCornerShape(14.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = PrimaryColor,
                            unfocusedBorderColor = TertiaryColor,
                        ),
                        singleLine = true,
                    )
                }
                Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text("Apellido Materno", style = MaterialTheme.typography.labelLarge)
                    OutlinedTextField(
                        value = motherSurname,
                        onValueChange = { motherSurname = it },
                        placeholder = { Text("Apellido", color = TextSecondary) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = androidx.compose.foundation.shape.RoundedCornerShape(14.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = PrimaryColor,
                            unfocusedBorderColor = TertiaryColor,
                        ),
                        singleLine = true,
                    )
                }
            }

            Text("Número de Teléfono", style = MaterialTheme.typography.labelLarge)
            JoviTextField(
                value = phone,
                onValueChange = { phone = it },
                placeholder = "(555) 000-0000",
                leadingIcon = Icons.Outlined.Phone,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            )

            Text("Correo Electrónico", style = MaterialTheme.typography.labelLarge)
            JoviTextField(
                value = email,
                onValueChange = { email = it },
                placeholder = "nombre@ejemplo.com",
                leadingIcon = Icons.Outlined.Email,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            )

            Text("Ubicación", style = MaterialTheme.typography.labelLarge)
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = androidx.compose.foundation.shape.RoundedCornerShape(14.dp),
                color = SurfaceColor,
                border = androidx.compose.foundation.BorderStroke(1.dp, TertiaryColor)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Outlined.LocationOn, contentDescription = null, tint = PrimaryDark, modifier = Modifier.size(28.dp))
                        Text("Usar ubicación actual", color = PrimaryDark, style = MaterialTheme.typography.labelMedium)
                    }
                }
            }

            Spacer(Modifier.height(8.dp))

            JoviPrimaryButton(
                text = "Siguiente Paso",
                onClick = onNext,
                trailingIcon = Icons.Outlined.Person,
            )

            Spacer(Modifier.height(16.dp))
        }
    }
}
