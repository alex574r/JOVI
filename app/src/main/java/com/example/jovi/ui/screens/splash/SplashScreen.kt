package com.example.jovi.ui.screens.splash

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.jovi.ui.theme.*
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(onFinished: (neverShowAgain: Boolean) -> Unit) {
    var visible by remember { mutableStateOf(false) }
    var showOption by remember { mutableStateOf(false) }
    var neverShowAgain by remember { mutableStateOf(false) }

    val alpha by animateFloatAsState(
        targetValue = if (visible) 1f else 0f,
        animationSpec = tween(700, easing = EaseOutCubic),
        label = "alpha"
    )
    val scale by animateFloatAsState(
        targetValue = if (visible) 1f else 0.7f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow),
        label = "scale"
    )
    val optionAlpha by animateFloatAsState(
        targetValue = if (showOption) 1f else 0f,
        animationSpec = tween(500),
        label = "optionAlpha"
    )

    LaunchedEffect(Unit) {
        visible = true
        delay(1000)
        showOption = true
        delay(1500)
        onFinished(neverShowAgain)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(SecondaryColor),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp),
            modifier = Modifier
                .alpha(alpha)
                .scale(scale)
        ) {
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .background(PrimaryColor),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "J",
                    color = SecondaryColor,
                    fontSize = 52.sp,
                    fontWeight = FontWeight.ExtraBold
                )
            }
            Text(
                "jovi",
                color = PrimaryColor,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 4.sp
            )
            Text(
                "Tu carrera, tu camino",
                color = PrimaryColor.copy(alpha = 0.6f),
                style = MaterialTheme.typography.bodySmall,
                letterSpacing = 1.sp
            )
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .alpha(optionAlpha),
            contentAlignment = Alignment.BottomCenter
        ) {
            Row(
                modifier = Modifier
                    .padding(bottom = 56.dp)
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Checkbox(
                    checked = neverShowAgain,
                    onCheckedChange = { neverShowAgain = it },
                    colors = CheckboxDefaults.colors(
                        checkedColor = PrimaryColor,
                        uncheckedColor = PrimaryColor.copy(alpha = 0.5f),
                        checkmarkColor = SecondaryColor
                    )
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    "No mostrar de nuevo",
                    color = PrimaryColor.copy(alpha = 0.8f),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}
