package com.touchpad.cursoroverlay.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = Teal400,
    onPrimary = DarkBackground,
    primaryContainer = Teal700,
    secondary = Blue400,
    onSecondary = DarkBackground,
    background = DarkBackground,
    onBackground = WhiteAlpha70,
    surface = DarkSurface,
    onSurface = WhiteAlpha70,
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = WhiteAlpha50
)

private val LightColorScheme = lightColorScheme(
    primary = Teal500,
    onPrimary = LightBackground,
    primaryContainer = Teal200,
    secondary = Blue600,
    onSecondary = LightBackground,
    background = LightBackground,
    onBackground = DarkBackground,
    surface = LightSurface,
    onSurface = DarkBackground,
    surfaceVariant = LightSurfaceVariant,
    onSurfaceVariant = DarkSurface.copy(alpha = 0.7f)
)

@Composable
fun TouchPadCursorOverlayTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
