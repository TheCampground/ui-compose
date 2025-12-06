package dev.thecampground.ui.examples

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import dev.thecampground.ui.Alert
import dev.thecampground.ui.AlertVariants
import dev.thecampground.ui.BaseButton
import dev.thecampground.ui.Button
import dev.thecampground.ui.Colors
import dev.thecampground.ui.InputSizes
import dev.thecampground.ui.annotation.CampgroundExample

@Composable
@CampgroundExample
@Suppress("unused")
internal fun BaseButton() {

}

@Composable
@CampgroundExample
@Suppress("unused")
internal fun Button() {
    Button(onClick = {}, text = "My text!")
}
@Composable
@CampgroundExample
@Suppress("unused")
internal fun ButtonContentSlot() {
    Button(onClick = {}, text = "My text!")
}



@Composable
@CampgroundExample
@Suppress("unused")
internal fun BaseAlert() {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Alert(
            variant = AlertVariants.DEFAULT,
            title = "campground/ui",
            content = "A collection of themed UI components for Campground projects.",
            icon = { _, _ -> }
        )
        Alert(
            variant = AlertVariants.SUCCESS,
            title = "campground/ui",
            content = "A collection of themed UI components for Campground projects.",
            icon = { _, _ -> }
        )
        Alert(
            variant = AlertVariants.INFO,
            title = "campground/ui",
            content = "A collection of themed UI components for Campground projects.",
            icon = { _, _ -> }
        )
        Alert(
            variant = AlertVariants.DANGER,
            title = "campground/ui",
            content = "A collection of themed UI components for Campground projects.",
            icon = { _, _ -> }
        )
    }
}

@Composable
@CampgroundExample
@Suppress("unused")
internal fun AlertContentSlot() {
    Button(onClick = {}, text = "My text!")
}

@Composable
@CampgroundExample
@Suppress("unused")
internal fun Alert() {

}

