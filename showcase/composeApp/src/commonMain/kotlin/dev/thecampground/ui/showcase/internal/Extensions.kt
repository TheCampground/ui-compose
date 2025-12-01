package dev.thecampground.ui.showcase.internal

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import dev.snipme.highlights.model.BoldHighlight
import dev.snipme.highlights.model.CodeHighlight
import dev.snipme.highlights.model.ColorHighlight

fun List<CodeHighlight>.generateAnnotatedString(code: String) =
    buildAnnotatedString {
        append(code)

        forEach {
            when (it) {
                is BoldHighlight -> addStyle(
                    SpanStyle(fontWeight = FontWeight.Bold),
                    start = it.location.start,
                    end = it.location.end,
                )

                is ColorHighlight -> addStyle(
                    SpanStyle(color = Color(it.rgb).copy(alpha = 1f)),
                    start = it.location.start,
                    end = it.location.end,
                )
            }
        }
    }
