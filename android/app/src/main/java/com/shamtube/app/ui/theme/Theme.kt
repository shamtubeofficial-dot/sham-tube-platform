package com.shamtube.app.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val ShamBurgundy = Color(0xFF8F2D3F)
private val ShamBurgundyDark = Color(0xFFFFB2BD)
private val ShamGold = Color(0xFFD5A64A)
private val ShamSand = Color(0xFFF9F1E7)
private val ShamInk = Color(0xFF241D1E)
private val ShamNight = Color(0xFF171315)

private val LightColors = lightColorScheme(
    primary = ShamBurgundy,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFFFDADF),
    onPrimaryContainer = Color(0xFF3B0713),
    secondary = ShamGold,
    onSecondary = Color(0xFF3B2A00),
    secondaryContainer = Color(0xFFFFE3A9),
    background = ShamSand,
    surface = Color(0xFFFFFBF8),
    surfaceVariant = Color(0xFFF1E2DD),
    onSurface = ShamInk,
)

private val DarkColors = darkColorScheme(
    primary = ShamBurgundyDark,
    onPrimary = Color(0xFF560F22),
    primaryContainer = Color(0xFF741F31),
    onPrimaryContainer = Color(0xFFFFDADF),
    secondary = Color(0xFFEBC36D),
    onSecondary = Color(0xFF3B2A00),
    secondaryContainer = Color(0xFF5B470E),
    background = ShamNight,
    surface = Color(0xFF211B1D),
    surfaceVariant = Color(0xFF49383B),
    onSurface = Color(0xFFF4E9E7),
)

@Composable
fun ShamTubeTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColors else LightColors,
        typography = ShamTypography,
        content = content,
    )
}