package com.sintegra.splinter.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable

private val LightSplinterColorPalette = lightColors(
    primary = Teal200,
    primaryVariant = Teal600,
    secondary = White,
    background = Teal800,
    onBackground = Teal1000,
    surface = Teal400,
)

private val DarkSplinterColorPalette = darkColors(
    primary = Teal200,
    primaryVariant = Teal600,
    secondary = White,
    background = Teal800,
    onBackground = Teal1000,
    surface = Teal400,
)

@Composable
fun SplinterTheme(content: @Composable () -> Unit) {


    MaterialTheme(
        colors = if(isSystemInDarkTheme()) DarkSplinterColorPalette else LightSplinterColorPalette,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}