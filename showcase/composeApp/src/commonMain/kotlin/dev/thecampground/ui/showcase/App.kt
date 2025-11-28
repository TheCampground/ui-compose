package dev.thecampground.ui.showcase

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import dev.thecampground.ui.Colors
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    MaterialTheme(typography = spaceGroteskTypography()) {
        Box(Modifier.background(Colors.BG_ALT)) {
//            Home()
            Docs()
        }
    }
}