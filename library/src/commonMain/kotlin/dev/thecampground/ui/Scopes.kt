package dev.thecampground.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import dev.thecampground.ui.annotation.CampgroundUIType

@CampgroundUIType
typealias IconComposable = @Composable (tint: Color, size: Dp) -> Unit

@CampgroundUIType
typealias TextComposable = @Composable (tint: Color) -> Unit