package com.example.jovi.ui.screens.verification

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Shield
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.jovi.ui.components.*
import com.example.jovi.ui.theme.*

@Composable
fun ProfileVerifiedSuccessScreen(
    onBack: () -> Unit,
    onGoToProfile: () -> Unit,
) {
    Scaffold(
        topBar = { JoviTopBar(title = "Verificación", onBack = onBack) },
        containerColor = BackgroundColor,
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(PrimaryLight),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.Shield,
                    contentDescription = null,
                    tint = PrimaryDark,
                    modifier = Modifier.size(44.dp)
                )
            }

            Spacer(Modifier.height(32.dp))

            Box(contentAlignment = Alignment.BottomEnd) {
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .background(PrimaryLight),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Outlined.Person,
                        contentDescription = null,
                        tint = PrimaryDark,
                        modifier = Modifier.size(60.dp)
                    )
                }
                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .clip(CircleShape)
                        .background(PrimaryColor),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Outlined.Shield,
                        contentDescription = null,
                        tint = SecondaryColor,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            Spacer(Modifier.height(28.dp))

            Text(
                "¡Perfil Verificado!",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(12.dp))

            Text(
                "Ahora eres un candidato de confianza para las empresas",
                style = MaterialTheme.typography.bodyLarge,
                color = TextSecondary,
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(20.dp))

            Surface(
                shape = androidx.compose.foundation.shape.RoundedCornerShape(50),
                color = PrimaryLight
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 7.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(Icons.Outlined.Shield, contentDescription = null, tint = PrimaryDark, modifier = Modifier.size(14.dp))
                    Text("Identidad confirmada", style = MaterialTheme.typography.labelMedium, color = PrimaryDark)
                }
            }

            Spacer(Modifier.height(56.dp))

            JoviPrimaryButton(text = "Ir a mi perfil", onClick = onGoToProfile)
        }
    }
}
