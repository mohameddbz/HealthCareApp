package com.example.projecttdm.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color


private val LightColorScheme = lightColorScheme(
    primary = BluePrimary,
    onPrimary = Color.White,
    primaryContainer = Blue02,
    onPrimaryContainer = TextPrimary,

    secondary = Blue01,
    onSecondary = Color.White,
    secondaryContainer = BlueSecondary,
    onSecondaryContainer = TextPrimary,
    inverseOnSurface = Gray03,
    tertiary = Gray01,
    onTertiary = Color.White,
    tertiaryContainer = BlueSecondary,
    onTertiaryContainer = TextPrimary,

    background = LightBackground,
    onBackground = TextPrimary,

    surface = SurfaceColor,
    onSurface = TextPrimary,
    surfaceVariant = BlueSecondary,
    onSurfaceVariant = TextSecondaryLight,

    error = ErrorRed,
    onError = Color.White,
    errorContainer = canceledNotification,
    onErrorContainer = textNotification,

    outline = BorderLight,
    outlineVariant = Gray01,

    surfaceDim = searchBackgroundLight ,

    surfaceBright = Blue01 , // BLUE TO WHITE

    )

private val DarkColorScheme = darkColorScheme(
    primary = BluePrimary,
    onPrimary = Color.White,
    primaryContainer = Gray02,
    onPrimaryContainer = DarkTextPrimary,

    secondary = Blue01,
    onSecondary = Color.White,
    secondaryContainer = Gray02,
    onSecondaryContainer = DarkTextPrimary,
    inverseOnSurface = Gray03,
    tertiary = Gray01,
    onTertiary = Color.White,
    tertiaryContainer = DarkSurfaceColor,
    onTertiaryContainer = DarkTextPrimary,

    background = DarkBackground,
    onBackground = DarkTextPrimary,

    surface = DarkSurfaceColor,
    onSurface = DarkTextPrimary,
    surfaceVariant = Gray02,
    onSurfaceVariant = DarkTextSecondary,

    error = ErrorRed,
    onError = Color.White,
    errorContainer = canceledNotification,
    onErrorContainer = textNotification,

    outline = BorderDark,
    outlineVariant = Gray01,


    surfaceDim = searchBackgroundDark ,

    surfaceBright = Color.White ,


    )

object NotificationColors {
    val canceled = canceledNotification
    val changed = changedNotification
    val success = successNotification
    val yellow = yellowNotification
    val text = textNotification
}

object SemanticColors {
    val success = SuccessGreen
    val error = ErrorRed
}

@Composable
fun ProjectTDMTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colors,
        typography = Typography,
        content = content
    )
}