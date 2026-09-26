package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val CyberDarkColorScheme = darkColorScheme(
    primary = CyberCyan,
    onPrimary = Color(0xFF00382E),
    primaryContainer = Color(0xFF005143),
    onPrimaryContainer = Color(0xFF70FEE4),
    secondary = CyberGold,
    onSecondary = Color(0xFF432C00),
    secondaryContainer = Color(0xFF614000),
    onSecondaryContainer = Color(0xFFFFDF9E),
    tertiary = CyberPink,
    onTertiary = Color(0xFF5E002B),
    tertiaryContainer = Color(0xFF86003F),
    onTertiaryContainer = Color(0xFFFFD9E2),
    background = CyberBg,
    onBackground = CyberTextPrimary,
    surface = CyberCard,
    onSurface = CyberTextPrimary,
    surfaceVariant = CyberBgElevated,
    onSurfaceVariant = CyberTextSecondary,
    outline = CyberCardBorder,
    outlineVariant = Color(0xFF243354)
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = true, // Force cyber dark theme for esports vibe
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = CyberDarkColorScheme,
        typography = Typography,
        content = content
    )
}
