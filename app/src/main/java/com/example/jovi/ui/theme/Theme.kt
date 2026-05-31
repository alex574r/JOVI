package com.example.jovi.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val JoviLightColorScheme = lightColorScheme(
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

private val JoviDarkColorScheme = darkColorScheme(
    primary = PrimaryColor,
    onPrimary = SecondaryColor,
    primaryContainer = SecondaryColor,
    onPrimaryContainer = PrimaryColor,
    secondary = PrimaryColor,
    onSecondary = SecondaryColor,
    background = DarkBackground,
    onBackground = DarkTextPrimary,
    surface = DarkSurface,
    onSurface = DarkTextPrimary,
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = DarkTextSecondary,
    outline = DarkDivider,
    outlineVariant = DarkDivider,
    error = ErrorColor,
    onError = BackgroundColor,
)

@Composable
fun JoviTheme(darkTheme: Boolean = false, content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = if (darkTheme) JoviDarkColorScheme else JoviLightColorScheme,
        typography = Typography,
        content = content,
    )
}
