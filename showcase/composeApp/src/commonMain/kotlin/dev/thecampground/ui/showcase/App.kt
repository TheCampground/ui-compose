package dev.thecampground.ui.showcase

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.SlideTransition
import dev.thecampground.ui.LocalCampgroundTheme
import dev.thecampground.ui.Noise
import dev.thecampground.ui.showcase.presentation.HomeScreen
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    val theme = LocalCampgroundTheme.current
    // TODO: Theme animation
    MaterialTheme(typography = spaceGroteskTypography()) {
            Box(Modifier.background(theme.background)) {
                Noise(bg = theme.grain, intensity = 0.2f, animated = true)
                Navigator(HomeScreen()) { nav ->
                    SlideTransition(nav)
                }
            }
        }


}