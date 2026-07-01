package com.example.calculator.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val AppColorScheme = lightColorScheme(
    primary = AppGreen,
    onPrimary = AppWhite,
    primaryContainer = AppGreenSoft,
    onPrimaryContainer = AppGreenDeep,
    secondary = AppBlack,
    onSecondary = AppWhite,
    background = AppWhite,
    onBackground = AppBlack,
    surface = AppGray,
    onSurface = AppBlack,
    surfaceVariant = AppGray,
    onSurfaceVariant = AppGrayMuted,
    outline = AppGrayBorder,
    outlineVariant = AppGrayBorder,
)

@Composable
fun CalculatorTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = AppColorScheme,
        typography = Typography,
        content = content,
    )
}
