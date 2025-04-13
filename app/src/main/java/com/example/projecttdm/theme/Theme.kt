
// # Gère les styles et la compatibilité avec le mode sombre
package com.example.projecttdm.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.graphics.Color

// Define Colors
private val BluePrimary = Color(0xFF1E88E5) // Primary blue 246BFD
private val BlueSecondary = Color(0xFF42A5F5) // Secondary blue
private val LightBackground = Color(0xFFF5F5F5) // Light background
private val DarkBackground = Color(0xFF121212) // Dark background
private val SurfaceColor = Color(0xFFFFFFFF) // White for surfaces in light mode
private val DarkSurfaceColor = Color(0xFF1E1E1E) // Darker surface for dark mode
private val ErrorColor = Color(0xFFD32F2F) // Red for error messages

// Text & Input Field Colors
private val TextPrimary = Color(0xFF212121) // Dark gray for main text
private val TextSecondary = Color(0xFF757575) // Lighter gray for subtitles
private val TextFieldBackground = Color(0xFFF0F0F0) // Light gray background for text fields
private val TextFieldBorder = Color(0xFFBDBDBD) // Medium gray border for text fields
private val ButtonTextColor = Color.White // Text color for buttons

// Dark theme text colors
private val DarkTextPrimary = Color(0xFFE0E0E0) // Light gray for text in dark mode
private val DarkTextSecondary = Color(0xFFBDBDBD) // Lighter gray for subtitles in dark mode

// Dark Color Scheme
private val DarkColorScheme = darkColorScheme(
    primary = BluePrimary,
    secondary = BlueSecondary,
    background = DarkBackground,
    surface = DarkSurfaceColor,
    onPrimary = Color.White,
    onSecondary = DarkTextPrimary, // Meilleur contraste dans le dark mode
    tertiary=TextSecondary,
    onTertiary=TextFieldBorder,
    onBackground = DarkTextPrimary,
    onSurface = DarkTextPrimary,
    error = ErrorColor
)

// Light Color Scheme
private val LightColorScheme = lightColorScheme(
    primary = BluePrimary,
    secondary = BlueSecondary,
    background = LightBackground,
    surface = SurfaceColor,
    onPrimary = Color.White,
    onSecondary = TextPrimary,
    tertiary=TextSecondary,
    onTertiary=TextFieldBorder,
    onBackground = TextPrimary,
    onSurface = TextPrimary,
    error = ErrorColor
)


@Composable
fun AppTheme(
    useDarkTheme: Boolean = isSystemInDarkTheme(), // Détecte le mode sombre automatiquement
    content: @Composable () -> Unit
) {
    val colors = if (useDarkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colors, // Applique le jeu de couleurs
        typography = Typography, // (Optionnel) Typographie personnalisée
        content = content
    )
}

@Composable
fun ProjectTDMTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}