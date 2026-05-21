package com.example.jovi.ui.screens.match

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Send
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
fun MatchCelebrationScreen(
    companyName: String = "Innovatech Corp",
    programName: String = "Prácticas Profesionales",
    onSendMessage: () -> Unit,
    onKeepSearching: () -> Unit,
    onDismiss: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundColor)
    ) {
        IconButton(
            onClick = onDismiss,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp)
        ) {
            Icon(Icons.Default.Close, contentDescription = "Cerrar", tint = TextSecondary)
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            // Overlapping avatars with heart
            Box(
                modifier = Modifier.height(80.dp).width(160.dp),
                contentAlignment = Alignment.Center
            ) {
                ProfileAvatar(
                    initials = "TU",
                    size = 72.dp,
                    bgColor = PrimaryLight,
                    modifier = Modifier.offset(x = (-30).dp)
                )
                ProfileAvatar(
                    initials = companyName.take(2),
                    size = 72.dp,
                    bgColor = SurfaceColor,
                    modifier = Modifier.offset(x = 30.dp)
                )
                Box(
                    modifier = Modifier
                        .size(30.dp)
                        .clip(CircleShape)
                        .background(PrimaryColor)
                        .align(Alignment.Center),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Favorite, contentDescription = null, tint = BackgroundColor, modifier = Modifier.size(16.dp))
                }
            }

            Spacer(Modifier.height(36.dp))

            Text(
                "¡Es un Match Académico!",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(16.dp))

            Text(
                "La empresa ",
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                color = TextPrimary
            )
            Text(
                "$companyName está interesada en tu perfil para su programa de $programName.",
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                color = TextPrimary
            )

            Spacer(Modifier.height(48.dp))

            JoviPrimaryButton(
                text = "Enviar Mensaje",
                onClick = onSendMessage,
                leadingIcon = Icons.Default.Send,
            )

            Spacer(Modifier.height(12.dp))

            JoviSecondaryButton(
                text = "Seguir Buscando",
                onClick = onKeepSearching,
            )
        }

        // Decorative dots
        repeat(6) { i ->
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(PrimaryLight)
                    .align(Alignment.TopStart)
                    .offset(x = (20 + i * 40).dp, y = (80 + (i % 3) * 30).dp)
            )
        }
    }
}
