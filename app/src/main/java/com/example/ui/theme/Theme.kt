package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.data.model.AppFontFamily
import com.example.data.model.AppThemeMode
import com.example.data.model.ColorPalettePreset

@Composable
fun ListWidgetAppTheme(
    themeMode: AppThemeMode = AppThemeMode.SYSTEM,
    colorPalette: ColorPalettePreset = ColorPalettePreset.BENTO_PURPLE,
    fontFamily: AppFontFamily = AppFontFamily.CLEAN_SANS,
    content: @Composable () -> Unit
) {
    val isDark = when (themeMode) {
        AppThemeMode.SYSTEM -> isSystemInDarkTheme()
        AppThemeMode.LIGHT -> false
        AppThemeMode.DARK -> true
    }

    val primaryColor = Color(android.graphics.Color.parseColor(colorPalette.primaryHex))
    val secondaryColor = Color(android.graphics.Color.parseColor(colorPalette.secondaryHex))

    val colorScheme = if (isDark) {
        darkColorScheme(
            primary = primaryColor,
            onPrimary = Color.White,
            primaryContainer = BentoDarkTile,
            onPrimaryContainer = Color.White,
            secondary = secondaryColor,
            onSecondary = Color.White,
            background = BackgroundDark,
            onBackground = TextPrimaryDark,
            surface = SurfaceDark,
            onSurface = TextPrimaryDark,
            surfaceVariant = SurfaceDarkElevated,
            onSurfaceVariant = TextSecondaryDark,
            outline = BorderDark
        )
    } else {
        lightColorScheme(
            primary = primaryColor,
            onPrimary = Color.White,
            primaryContainer = BentoPrimaryContainer,
            onPrimaryContainer = BentoOnPrimaryContainer,
            secondary = secondaryColor,
            onSecondary = Color.White,
            background = BackgroundLight,
            onBackground = TextPrimaryLight,
            surface = SurfaceLight,
            onSurface = TextPrimaryLight,
            surfaceVariant = SurfaceLightElevated,
            onSurfaceVariant = TextSecondaryLight,
            outline = BorderLight
        )
    }

    val typography = getTypographyForFont(fontFamily)

    MaterialTheme(
        colorScheme = colorScheme,
        typography = typography,
        content = content
    )
}
