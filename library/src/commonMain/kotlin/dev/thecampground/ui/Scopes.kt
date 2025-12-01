package dev.thecampground.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp

typealias IconComposable = @Composable (tint: Color, size: Dp) -> Unit

typealias TextComposable = @Composable (tint: Color) -> Unit