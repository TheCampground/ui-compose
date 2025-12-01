package dev.thecampground.ui.internal

import androidx.compose.runtime.Composable
import dev.thecampground.ui.annotation.CampgroundDocComponentProp

data class CampgroundDocComponent(
    val uniqueName: String,
    val name: String,
    val description: String = "No description provided",
    val example: (@Composable () -> Unit)? = null,
    val props: List<CampgroundDocComponentProp> = listOf()
)