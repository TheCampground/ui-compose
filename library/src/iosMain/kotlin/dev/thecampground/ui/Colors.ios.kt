package dev.thecampground.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable

@Composable
actual fun isDarkTheme(): Boolean {
    return isSystemInDarkTheme()
}