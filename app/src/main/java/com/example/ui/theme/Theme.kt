package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color

private val CosmicColorScheme = darkColorScheme(
    primary = CyanGlow,
    secondary = GoldGlow,
    background = CosmicBlack,
    surface = DeepSpace,
    onPrimary = CosmicBlack,
    onSecondary = CosmicBlack,
    onBackground = TextPrimary,
    onSurface = TextPrimary,
    error = NeonRed,
    outline = OutlineDark,
    surfaceVariant = DeepSpace,
    onSurfaceVariant = TextSecondary
)

private val ArtisticLightColorScheme = lightColorScheme(
    primary = LightPrimary,
    secondary = LightSecondary,
    background = LightBackground,
    surface = LightSurface,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onBackground = LightTextPrimary,
    onSurface = LightTextPrimary,
    error = LightRed,
    outline = LightOutline,
    surfaceVariant = LightSurface,
    onSurfaceVariant = LightTextSecondary
)

val LocalThemeToggle = compositionLocalOf { { _: Boolean -> } }
val LocalIsDarkTheme = compositionLocalOf { true }

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) CosmicColorScheme else ArtisticLightColorScheme
    
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
