package dev.thecampground.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.thecampground.ui.annotation.CampgroundDocComponent
import dev.thecampground.ui.annotation.CampgroundUIComponent
import dev.thecampground.ui.annotation.CampgroundUIComponentProp
import dev.thecampground.ui.annotation.CampgroundUIType

private const val ALERT_ICON_SIZE = 20

@CampgroundUIType
class AlertVariant(
    val background: Color,
    val foreground: Color,
    val border: Color
) {
    companion object {
        val DEFAULT = AlertVariant(
            foreground = Colors.BRAND_FOREGROUND,
            background = Colors.ALERT_DEFAULT_BACKGROUND,
            border = Colors.ALERT_DEFAULT_BORDER
        )
        val SUCCESS = AlertVariant(
            foreground = Colors.BRAND_FOREGROUND,
            background = Colors.ALERT_SUCCESS_BACKGROUND,
            border = Colors.ALERT_SUCCESS_BORDER
        )
        val INFO = AlertVariant(
            foreground = Colors.BRAND_FOREGROUND,
            background = Colors.ALERT_INFO_BACKGROUND,
            border = Colors.ALERT_INFO_BORDER
        )
        val SECONDARY = AlertVariant(
            foreground = Colors.ALERT_SECONDARY_FOREGROUND,
            background = Colors.ALERT_SECONDARY_BACKGROUND,
            border = Color.Transparent
        )
        val DANGER = AlertVariant(
            foreground = Colors.ALERT_DANGER_FOREGROUND,
            background = Colors.ALERT_DANGER_BACKGROUND,
            border = Colors.ALERT_DANGER_BORDER
        )
    }
}

@Composable
@CampgroundUIComponent(uniqueName = "BaseAlert", description = "Displays a callout for user attention.")
fun BaseAlert(
    @CampgroundUIComponentProp(description = "The variant of the alert")
    variant: AlertVariant,
    icon: IconComposable,
    content: @Composable (tint: Color) -> Unit
) {
    Row(
        modifier =
            Modifier
                .width(700.dp)
                .clip(RoundedInputShape)
                .border(
                    width = 1.dp,
                    color = variant.border,
                    shape = RoundedInputShape
                )
                .background(variant.background)
                .padding(12.dp),
        verticalAlignment = Alignment.Top
    ) {

        icon(variant.foreground, ALERT_ICON_SIZE.dp)

        Column(modifier = Modifier.padding(start = 14.dp), verticalArrangement = Arrangement.Center) {
            content(variant.foreground)
        }
    }
}

@Composable
@CampgroundUIComponent(uniqueName = "AlertContentSlot", description = "Displays a callout for user attention.")
fun Alert(
    variant: AlertVariants = AlertVariants.DEFAULT,
    icon: IconComposable,
    content: @Composable (tint: Color) -> Unit
) {
    val colors = when (variant) {
        AlertVariants.DEFAULT -> AlertVariant.DEFAULT
        AlertVariants.SUCCESS -> AlertVariant.SUCCESS
        AlertVariants.INFO -> AlertVariant.INFO
        AlertVariants.SECONDARY -> AlertVariant.SECONDARY
        AlertVariants.DANGER -> AlertVariant.DANGER
    }

    BaseAlert(
        variant = colors,
        icon = icon,
    ) { tint ->
        content(tint)
    }
}

@CampgroundUIComponent(uniqueName = "Alert", description = "Displays a callout for user attention.")
@Composable
fun Alert(
    variant: AlertVariants = AlertVariants.DEFAULT,
    icon: IconComposable,
    title: String? = null,
    content: String
) {
    val colors = when (variant) {
        AlertVariants.DEFAULT -> AlertVariant.DEFAULT
        AlertVariants.SUCCESS -> AlertVariant.SUCCESS
        AlertVariants.INFO -> AlertVariant.INFO
        AlertVariants.SECONDARY -> AlertVariant.SECONDARY
        AlertVariants.DANGER -> AlertVariant.DANGER
    }

    BaseAlert(
        variant = colors,
        icon = icon,
    ) { tint ->
        if (title != null) {
            Text(title, color = tint, fontSize = 18.sp, lineHeight = 24.sp, letterSpacing = (-0.5).sp, fontWeight = FontWeight.W600)
        }
        Text(content, color = tint, fontSize = 16.sp, letterSpacing = (-0.5).sp, fontWeight = FontWeight.W400)
    }
}