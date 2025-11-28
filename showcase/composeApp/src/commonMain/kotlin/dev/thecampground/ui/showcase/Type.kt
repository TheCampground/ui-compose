package dev.thecampground.ui.showcase

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontVariation
import androidx.compose.ui.text.font.FontWeight
import camgroundui.showcase.composeapp.generated.resources.Res
import camgroundui.showcase.composeapp.generated.resources.SpaceGrotesk_Variable
import org.jetbrains.compose.resources.Font


@Composable
fun spaceGroteskFamily() = FontFamily(
    Font(
        Res.font.SpaceGrotesk_Variable, weight = FontWeight.Light, variationSettings = FontVariation.Settings(FontVariation.weight(FontWeight.Light.weight),
        FontVariation.width(1f))),
    Font(Res.font.SpaceGrotesk_Variable, weight = FontWeight.Normal, variationSettings = FontVariation.Settings(FontVariation.weight(FontWeight.Normal.weight))),
    Font(Res.font.SpaceGrotesk_Variable, weight = FontWeight.Medium, variationSettings = FontVariation.Settings(FontVariation.weight(FontWeight.Medium.weight))),
    Font(Res.font.SpaceGrotesk_Variable, weight = FontWeight.SemiBold, variationSettings = FontVariation.Settings(FontVariation.weight(FontWeight.SemiBold.weight))),
    Font(Res.font.SpaceGrotesk_Variable, weight = FontWeight.Bold, variationSettings = FontVariation.Settings( FontVariation.weight(FontWeight.Bold.weight))),
    Font(Res.font.SpaceGrotesk_Variable, weight = FontWeight.ExtraBold, variationSettings = FontVariation.Settings(FontVariation.weight(FontWeight.ExtraBold.weight))),
    Font(Res.font.SpaceGrotesk_Variable, weight = FontWeight.Black, variationSettings = FontVariation.Settings(FontVariation.weight(FontWeight.Black.weight)))
)

@Composable
fun spaceGroteskTypography() = Typography().let {
        val fontFamily = spaceGroteskFamily()

        it.copy(
            displayLarge = it.displayLarge.copy(fontFamily = fontFamily),
            displayMedium = it.displayMedium.copy(fontFamily = fontFamily),
            displaySmall = it.displaySmall.copy(fontFamily = fontFamily),
            headlineLarge = it.headlineLarge.copy(fontFamily = fontFamily),
            headlineMedium = it.headlineMedium.copy(fontFamily = fontFamily),
            headlineSmall = it.headlineSmall.copy(fontFamily = fontFamily),
            titleLarge = it.titleLarge.copy(fontFamily = fontFamily),
            titleMedium = it.titleMedium.copy(fontFamily = fontFamily),
            titleSmall = it.titleSmall.copy(fontFamily = fontFamily),
            bodyLarge = it.bodyLarge.copy(fontFamily = fontFamily),
            bodyMedium = it.bodyMedium.copy(fontFamily = fontFamily),
            bodySmall = it.bodySmall.copy(fontFamily = fontFamily),
            labelLarge = it.labelLarge.copy(fontFamily = fontFamily),
            labelMedium = it.labelMedium.copy(fontFamily = fontFamily),
            labelSmall = it.labelSmall.copy(fontFamily = fontFamily)
        )
}