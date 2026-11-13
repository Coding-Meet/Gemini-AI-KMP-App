package com.coding.meet.gaminiaikmp.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.Modifier

val LocalThemeMode = compositionLocalOf { AppThemeMode.SYSTEM }

@Composable
fun AppTheme(
    themeMode: AppThemeMode = AppThemeMode.SYSTEM,
    content: @Composable () -> Unit
) {
    val darkTheme = when (themeMode) {
        AppThemeMode.SYSTEM -> isSystemInDarkTheme()
        AppThemeMode.LIGHT -> false
        AppThemeMode.DARK -> true
    }

    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    CompositionLocalProvider(LocalThemeMode provides themeMode) {
        MaterialTheme(
            colorScheme = colorScheme,
        ){
            Surface(
                modifier = Modifier.fillMaxSize(),
                content = content,
                color = MaterialTheme.colorScheme.surface
            )
        }
    }
}

@Composable
fun ComposeThemePreview(
    darkTheme: Boolean = true,
    content: @Composable () -> Unit
) {
    AppTheme(themeMode = if (darkTheme) AppThemeMode.DARK else AppThemeMode.LIGHT, content = content)
}
