package dev.thecampground.ui.showcase.presentation.documentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import dev.thecampground.ui.Colors
import dev.thecampground.ui.showcase.presentation.defaultLink

class IntroductionScreen : Screen {
    @Composable
    override fun Content() {
        DocumentationRoot("Introduction", "A collection of themed UI components.") {
            Text(
                buildAnnotatedString {
                    append("campground/compose-ui is a collection of themed components for ")
                    defaultLink("https://github.com/TheCampground", "@TheCampground")
                    append(", with accessibility in mind.")
                },
                fontSize = 16.sp,
                fontWeight = FontWeight.W400,
                letterSpacing = (-0.4).sp,
                color = Colors.BG_DARK
            )
        }
    }
}