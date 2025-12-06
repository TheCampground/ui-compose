package dev.thecampground.ui

import androidx.compose.foundation.isSystemInDarkTheme

@androidx.compose.runtime.Composable
actual fun isDarkTheme(): Boolean {
    return isSystemInDarkTheme()
}