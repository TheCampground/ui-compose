package dev.thecampground.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import dev.thecampground.ui.annotation.CampgroundType

@CampgroundType
@Suppress("unused")
typealias IconComposable = @Composable (tint: Color, size: Dp) -> Unit

@CampgroundType
@Suppress("unused")
typealias TextComposable = @Composable (tint: Color) -> Unit