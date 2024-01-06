package com.example.strmpriskalkulator.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable

private val DarkColorPalette = darkColors(
    primary = Purple200,
    primaryVariant = Purple700,
    secondary = Teal200
)

private val LightColorPalette = lightColors(
    primary = ColorSelection,
    primaryVariant = ColorBorder,
    secondary = ColorSecondary,
    secondaryVariant = ColorBar,
    background = ColorBackground,
    surface = ColorSecondBackground,
    onPrimary = ColorText,
    onSecondary = ColorText,
    onBackground = ColorText,
    onSurface = ColorTex,
    error = ColorError,
)

@Composable
fun StromprisKalkulatorTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}

