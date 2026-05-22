package com.example.jovi.ui.screens.auth

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.PersonAdd
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.unit.dp
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import com.example.jovi.R
import com.example.jovi.ui.components.*
import com.example.jovi.ui.theme.*

@Composable
fun LoginScreen(
    onLogin: () -> Unit,
    onRegister: () -> Unit,
    onGuest: () -> Unit,
    onRecruiterLogin: () -> Unit,
    onForgotPassword: () -> Unit = {},
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val context = LocalContext.current
    val imageLoader = remember {
        ImageLoader.Builder(context)
            .components { add(SvgDecoder.Factory()) }
            .build()
    }

    var contentVisible by remember { mutableStateOf(false) }
    val contentAlpha by animateFloatAsState(
        targetValue = if (contentVisible) 1f else 0f,
        animationSpec = tween(600, easing = EaseOutCubic),
        label = "content_alpha"
    )

    LaunchedEffect(Unit) { contentVisible = true }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundColor)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .clip(RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp))
                .background(SecondaryColor)
        ) {
            AsyncImage(
                model = ImageRequest.Builder(context)
                    .data("android.resource://${context.packageName}/raw/image_login")
                    .build(),
                imageLoader = imageLoader,
                contentDescription = "Jovi hero illustration",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Fit,
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(SecondaryColor.copy(alpha = 0.15f))
            )
            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(24.dp)
            ) {
                Text(
                    "jovi",
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.ExtraBold,
                    color = PrimaryColor,
                    letterSpacing = androidx.compose.ui.unit.TextUnit(2f, androidx.compose.ui.unit.TextUnitType.Sp)
                )
                Text(
                    "Tu carrera, tu camino",
                    style = MaterialTheme.typography.bodyMedium,
                    color = PrimaryColor.copy(0.75f)
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .alpha(contentAlpha)
                .padding(horizontal = 28.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(Modifier.height(32.dp))

            JoviTextField(
                value = email,
                onValueChange = { email = it },
                placeholder = "Correo electrónico",
                leadingIcon = Icons.Outlined.Email,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            )

            Spacer(Modifier.height(14.dp))

            JoviTextField(
                value = password,
                onValueChange = { password = it },
                placeholder = "Contraseña",
                leadingIcon = Icons.Outlined.Lock,
                isPassword = true,
            )

            Spacer(Modifier.height(8.dp))

            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterEnd) {
                TextButton(onClick = onForgotPassword) {
                    Text("¿Olvidaste la contraseña?", color = PrimaryDark, style = MaterialTheme.typography.bodySmall)
                }
            }

            Spacer(Modifier.height(20.dp))

            JoviPrimaryButton(text = "Ingresar", onClick = onLogin)

            Spacer(Modifier.height(20.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                HorizontalDivider(modifier = Modifier.weight(1f), color = TertiaryColor)
                Text("  ó  ", color = TextSecondary, style = MaterialTheme.typography.bodyMedium)
                HorizontalDivider(modifier = Modifier.weight(1f), color = TertiaryColor)
            }

            Spacer(Modifier.height(20.dp))

            JoviSecondaryButton(
                text = "Crear una cuenta",
                onClick = onRegister,
                leadingIcon = Icons.Outlined.PersonAdd,
            )

            Spacer(Modifier.height(12.dp))

            JoviPrimaryButton(
                text = "Continuar como Invitado",
                onClick = onGuest,
                leadingIcon = Icons.Outlined.Person,
            )

            Spacer(Modifier.height(12.dp))

            TextButton(onClick = onRecruiterLogin) {
                Text("Acceder como Reclutador / Institución", color = SecondaryColor, style = MaterialTheme.typography.bodySmall)
            }

            Spacer(Modifier.height(32.dp))
        }
    }
}
