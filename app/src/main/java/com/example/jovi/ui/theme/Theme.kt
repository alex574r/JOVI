package com.example.jovi.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val JoviColorScheme = lightColorScheme(
    primary = PrimaryColor,
    onPrimary = SecondaryColor,
    primaryContainer = PrimaryLight,
    onPrimaryContainer = SecondaryColor,
    secondary = SecondaryColor,
    onSecondary = BackgroundColor,
    secondaryContainer = PrimaryLight,
    onSecondaryContainer = SecondaryColor,
    tertiary = TertiaryColor,
    onTertiary = TextPrimary,
    tertiaryContainer = SurfaceColor,
    background = BackgroundColor,
    onBackground = TextPrimary,
    surface = BackgroundColor,
    onSurface = TextPrimary,
    surfaceVariant = SurfaceColor,
    onSurfaceVariant = TextSecondary,
    outline = TertiaryColor,
    outlineVariant = DividerColor,
    error = ErrorColor,
    onError = BackgroundColor,
)

@Composable
fun JoviTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = JoviColorScheme,
        typography = Typography,
        content = content
    )
}