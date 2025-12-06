package dev.thecampground.ui.showcase

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.SlideTransition
import dev.thecampground.ui.CampgroundTheme
import dev.thecampground.ui.Colors
import dev.thecampground.ui.LocalCampgroundTheme
import dev.thecampground.ui.showcase.presentation.HomeScreen
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    val theme = LocalCampgroundTheme.current

    MaterialTheme(typography = spaceGroteskTypography()) {
            Box(Modifier.background(theme.background)) {
                Navigator(HomeScreen()) { nav ->
                    SlideTransition(nav)
                }
            }
        }
}