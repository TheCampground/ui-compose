package dev.thecampground.ui.showcase.internal.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Dp

object ComponentPreview {
    val components = mutableMapOf<String, @Composable (maxWidth: Dp) -> Unit>(
        "Button" to { maxWidth -> ButtonDocumentation(maxWidth) },
        "AccordionRoot" to { maxWidth -> AccordionDocumentation(maxWidth) },
    )
}

