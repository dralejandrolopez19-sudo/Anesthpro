package com.example.anesthpro.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val PremiumDarkColorScheme = darkColorScheme(
    primary = BluePrimary,
    secondary = TealAccent,
    tertiary = Gold,
    background = Navy900,
    surface = CardDark,
    onPrimary = White,
    onSecondary = White,
    onBackground = TextPrimary,
    onSurface = TextPrimary
)

@Composable
fun AnesthProTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) PremiumDarkColorScheme else PremiumDarkColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}
