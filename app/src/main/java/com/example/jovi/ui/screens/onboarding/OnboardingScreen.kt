package com.example.jovi.ui.screens.onboarding

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.jovi.ui.theme.*

private data class OnboardingPage(
    val icon: ImageVector,
    val title: String,
    val subtitle: String,
)

private val pages = listOf(
    OnboardingPage(Icons.Outlined.Search, "Descubre Oportunidades", "Encuentra practicas, empleos y becas con solo deslizar. Tu proxima oportunidad esta a un swipe de distancia."),
    OnboardingPage(Icons.Outlined.People, "Conecta con Reclutadores", "Haz match con empresas e instituciones que buscan exactamente tu perfil. El proceso de contratacion nunca fue tan directo."),
    OnboardingPage(Icons.Outlined.TrendingUp, "Crece Profesionalmente", "Comparte tu experiencia, sigue a referentes de tu industria y construye tu reputacion profesional desde hoy."),
)

@Composable
fun OnboardingScreen(onFinish: () -> Unit) {
    var currentPage by remember { mutableIntStateOf(0) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundColor)
    ) {
        TextButton(
            onClick = onFinish,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp)
        ) {
            Text("Saltar", color = PrimaryDark)
        }

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            AnimatedContent(
                targetState = currentPage,
                transitionSpec = {
                    if (targetState > initialState) {
                        (slideInHorizontally { w -> w } + fadeIn()).togetherWith(slideOutHorizontally { w -> -w } + fadeOut())
                    } else {
                        (slideInHorizontally { w -> -w } + fadeIn()).togetherWith(slideOutHorizontally { w -> w } + fadeOut())
                    }
                },
                label = "page"
            ) { page ->
                val data = pages[page]
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(24.dp),
                    modifier = Modifier.padding(horizontal = 40.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(140.dp)
                            .clip(CircleShape)
                            .background(PrimaryLight),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(data.icon, contentDescription = null, tint = PrimaryDark, modifier = Modifier.size(64.dp))
                    }
                    Text(data.title, color = TextPrimary, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
                    Text(data.subtitle, color = TextSecondary, style = MaterialTheme.typography.bodyMedium, textAlign = TextAlign.Center, lineHeight = MaterialTheme.typography.bodyMedium.lineHeight)
                }
            }
        }

        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(horizontal = 28.dp, vertical = 48.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                pages.indices.forEach { i ->
                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(if (i == currentPage) PrimaryColor else PrimaryColor.copy(0.3f))
                            .size(if (i == currentPage) 10.dp else 8.dp)
                    )
                }
            }

            Surface(
                onClick = {
                    if (currentPage < pages.size - 1) currentPage++ else onFinish()
                },
                modifier = Modifier.fillMaxWidth().height(52.dp),
                shape = RoundedCornerShape(50),
                color = SecondaryColor
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        if (currentPage < pages.size - 1) "Siguiente" else "Comenzar",
                        color = PrimaryColor,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            }
        }
    }
}
