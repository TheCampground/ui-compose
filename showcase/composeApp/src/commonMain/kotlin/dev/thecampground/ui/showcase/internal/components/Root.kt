package dev.thecampground.ui.showcase.internal.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Dp

object ComponentPreview {
    val components = mutableMapOf<String, @Composable (maxWidth: Dp) -> Unit>(
        "Alert" to { AlertDocumentation() },
        "Accordion" to { maxWidth -> AccordionDocumentation(maxWidth) },

        "Button" to { maxWidth -> ButtonDocumentation(maxWidth) },

        )
}

