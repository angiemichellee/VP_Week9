package com.example.week9.soal1.view

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

object PokeColors {
    val Background = Color(0xFF12161C)
    val Surface = Color(0xFF1B2129)
    val SurfaceHigh = Color(0xFF232A33)
    val Border = Color(0xFF2C343E)
    val Red = Color(0xFFE0473C)
    val Green = Color(0xFF4ADE80)
    val Yellow = Color(0xFFFACC15)
    val TextPrimary = Color(0xFFF1F3F5)
    val TextSecondary = Color(0xFF8B95A1)
}

private val PokeColorScheme = darkColorScheme(
    primary = PokeColors.Red,
    onPrimary = Color.White,
    background = PokeColors.Background,
    onBackground = PokeColors.TextPrimary,
    surface = PokeColors.Surface,
    onSurface = PokeColors.TextPrimary,
    surfaceVariant = PokeColors.SurfaceHigh,
    onSurfaceVariant = PokeColors.TextSecondary,
    error = PokeColors.Red,
)

@Composable
fun PokemonTheme(content: @Composable () -> Unit) {
    MaterialTheme(colorScheme = PokeColorScheme, content = content)
}