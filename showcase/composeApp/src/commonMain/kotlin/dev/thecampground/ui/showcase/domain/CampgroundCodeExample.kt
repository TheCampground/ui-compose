package dev.thecampground.ui.showcase.domain

import androidx.compose.runtime.Composable

internal class CampgroundCodeExample(
    val code: String,
    val component: @Composable () -> Unit
)