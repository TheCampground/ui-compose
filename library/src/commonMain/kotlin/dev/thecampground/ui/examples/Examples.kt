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
import dev.thecampground.ui.DefaultButtonColors
import dev.thecampground.ui.InputSizes
import dev.thecampground.ui.annotation.CampgroundExample

@Composable
@CampgroundExample
internal fun BaseButton() {
    BaseButton(
        onClick = {},
        size = InputSizes.DEFAULT,
        colors = DefaultButtonColors,
        hoverColor = Colors.DEFAULT_BUTTON_HOVERED,
        modifier = Modifier.alpha(1f),
        feedback = HapticFeedbackType.LongPress,
        icon = { tint, size -> },
    ) {
        Text("My Button Contents", fontWeight = FontWeight.Bold)
    }
}

@Composable
@CampgroundExample
internal fun Button() {
    Button(onClick = {}, text = "My text!")
}
@Composable
@CampgroundExample
internal fun ButtonContentSlot() {
    Button(onClick = {}, text = "My text!")
}



@Composable
@CampgroundExample
internal fun BaseAlert() {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Alert(
            variant = AlertVariants.DEFAULT,
            title = "campground/ui",
            content = "A collection of themed UI components for Campground projects.",
            icon = { tint, size -> }
        )
        Alert(
            variant = AlertVariants.SUCCESS,
            title = "campground/ui",
            content = "A collection of themed UI components for Campground projects.",
            icon = { tint, size -> }
        )
        Alert(
            variant = AlertVariants.INFO,
            title = "campground/ui",
            content = "A collection of themed UI components for Campground projects.",
            icon = { tint, size -> }
        )
        Alert(
            variant = AlertVariants.DANGER,
            title = "campground/ui",
            content = "A collection of themed UI components for Campground projects.",
            icon = { tint, size -> }
        )
    }
}

@Composable
@CampgroundExample
internal fun AlertContentSlot() {
    Button(onClick = {}, text = "My text!")
}

@Composable
@CampgroundExample
internal fun Alert() {

}

