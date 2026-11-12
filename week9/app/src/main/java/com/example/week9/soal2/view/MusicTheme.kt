package com.example.week9.soal2.view

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

object MusicColors {
    val Background = Color(0xFF121417)
    val Surface = Color(0xFF1C1F24)
    val SurfaceHigh = Color(0xFF262A30)
    val Border = Color(0xFF2E333A)
    val Green = Color(0xFF4CC26B)
    val Red = Color(0xFFE5534B)
    val TextPrimary = Color(0xFFF2F3F5)
    val TextSecondary = Color(0xFF9AA0A6)
}

private val MusicColorScheme = darkColorScheme(
    primary = MusicColors.Green,
    onPrimary = Color(0xFF0B1A10),
    background = MusicColors.Background,
    onBackground = MusicColors.TextPrimary,
    surface = MusicColors.Surface,
    onSurface = MusicColors.TextPrimary,
    surfaceVariant = MusicColors.SurfaceHigh,
    onSurfaceVariant = MusicColors.TextSecondary,
    error = MusicColors.Red,
)

@Composable
fun MusicTheme(content: @Composable () -> Unit) {
    MaterialTheme(colorScheme = MusicColorScheme, content = content)
}