package dev.thecampground.ui.showcase.internal

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.Navigator
import dev.snipme.highlights.model.BoldHighlight
import dev.snipme.highlights.model.CodeHighlight
import dev.snipme.highlights.model.ColorHighlight


private val screenListeners: MutableList<(Screen) -> Unit> = mutableListOf()


fun Navigator.addOnScreenChangedListener(listener: (Screen) -> Unit) {
    println("Added listener: ${listener}")
    screenListeners += listener
}

fun Navigator.removeOnScreenChangedListener(listener: (Screen) -> Unit) {
    screenListeners -= listener
}

fun Navigator.safePush(screen: Screen) {
    println("Safe pushed: ${screenListeners}")
    push(screen)
    screenListeners.forEach { it.invoke(screen) }
}

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

@Composable
fun Navigator.currentScreenAsState(): State<Screen> {
    // Recompose when navigator.lastItem changes
    val state = remember { mutableStateOf(lastItem) }

    // Voyager NavigatorContent is what triggers recompositions.
    // We hook into it using this trick:
    DisposableEffect(this) {
        val listener: (Screen) -> Unit = { newScreen ->
            state.value = newScreen
        }

        // Watch navigation changes
        this@currentScreenAsState.addOnScreenChangedListener(listener)

        onDispose {
            this@currentScreenAsState.removeOnScreenChangedListener(listener)
        }
    }

    return state
}
