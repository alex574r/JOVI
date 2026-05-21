package com.example.jovi.ui.screens.verification

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Face
import androidx.compose.material.icons.outlined.Fingerprint
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Shield
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.jovi.ui.components.*
import com.example.jovi.ui.theme.*

@Composable
fun BiometricVerificationScreen(
    onSkip: () -> Unit,
    onVerify: () -> Unit,
) {
    val progress by animateFloatAsState(
        targetValue = 0.65f,
        animationSpec = tween(1200),
        label = "scan_progress"
    )

    Scaffold(
        topBar = {
            JoviTopBar(
                title = "Identidad",
                onBack = {},
                actions = {
                    TextButton(onClick = onSkip) {
                        Text("Saltar", color = TextSecondary)
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
                .padding(horizontal = 28.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp),
        ) {
            Spacer(Modifier.height(16.dp))

            Icon(
                Icons.Outlined.Shield,
                contentDescription = null,
                tint = PrimaryDark,
                modifier = Modifier.size(48.dp)
            )

            Text(
                "Verificación\nBiométrica",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            Text(
                "Posiciona tu rostro frente a la cámara para asegurar tu perfil.",
                style = MaterialTheme.typography.bodyMedium,
                color = TextSecondary,
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(8.dp))

            Box(
                modifier = Modifier.size(200.dp),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(200.dp)
                        .clip(CircleShape)
                        .border(4.dp, Brush.sweepGradient(listOf(PrimaryColor, PrimaryLight, PrimaryColor)), CircleShape)
                )
                Box(
                    modifier = Modifier
                        .size(180.dp)
                        .clip(CircleShape)
                        .background(PrimaryLight),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Outlined.Face, contentDescription = null, tint = PrimaryDark, modifier = Modifier.size(64.dp))
                }
                Surface(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .offset(y = 10.dp),
                    shape = RoundedCornerShape(50),
                    color = PrimaryColor
                ) {
                    Text(
                        "ANALIZANDO",
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 4.dp),
                        style = MaterialTheme.typography.labelSmall,
                        color = SecondaryColor,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(Modifier.height(16.dp))

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Progreso del escaneo", style = MaterialTheme.typography.labelMedium)
                    Text("${(progress * 100).toInt()}%", style = MaterialTheme.typography.labelMedium, color = PrimaryDark)
                }
                LinearProgressIndicator(
                    progress = { progress },
                    modifier = Modifier.fillMaxWidth().height(6.dp).clip(RoundedCornerShape(50)),
                    color = PrimaryColor,
                    trackColor = TertiaryColor,
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Icon(Icons.Outlined.Face, contentDescription = null, tint = TextSecondary, modifier = Modifier.size(14.dp))
                Text(
                    "Mantén el dispositivo a la altura de tus ojos",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondary
                )
            }

            Spacer(Modifier.height(8.dp))

            JoviPrimaryButton(
                text = "Verificar Ahora",
                onClick = onVerify,
                leadingIcon = Icons.Outlined.Face,
            )

            JoviSecondaryButton(
                text = "Usar huella digital",
                onClick = onVerify,
                leadingIcon = Icons.Outlined.Fingerprint,
            )

            Spacer(Modifier.height(8.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Icon(Icons.Outlined.Lock, contentDescription = null, tint = TextSecondary, modifier = Modifier.size(12.dp))
                Text(
                    "ENCRIPTACIÓN DE GRADO MILITAR AES-256",
                    style = MaterialTheme.typography.labelSmall,
                    color = TextSecondary
                )
            }
        }
    }
}
