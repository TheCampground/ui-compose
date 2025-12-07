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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.thecampground.ui.annotation.CampgroundComponent
import dev.thecampground.ui.annotation.CampgroundProp

private const val ALERT_ICON_SIZE = 20

@Composable
@CampgroundComponent(description = "Displays a callout for user attention.")
@Suppress("unused")
fun BaseAlert(
    @CampgroundProp(description = "The variant of the alert")
    color: AlertColor,
    icon: IconComposable,
    content: TextComposable
) {
    Row(
        modifier =
            Modifier
                .width(700.dp)
                .clip(RoundedInputShape)
                .border(
                    width = 1.dp,
                    color = color.outline,
                    shape = RoundedInputShape
                )
                .background(color.background)
                .padding(12.dp),
        verticalAlignment = Alignment.Top
    ) {

        icon(color.foreground, ALERT_ICON_SIZE.dp)

        Column(modifier = Modifier.padding(start = 14.dp), verticalArrangement = Arrangement.Center) {
            content(color.foreground)
        }
    }
}

@Composable
@CampgroundComponent(description = "Displays a callout for user attention.")
@Suppress("unused")
fun Alert(
    variant: AlertVariants = AlertVariants.DEFAULT,
    icon: IconComposable,
    content: TextComposable
) {
    val theme = LocalCampgroundTheme.current.alert
    val colors = when (variant) {
        AlertVariants.DEFAULT -> theme.default
        AlertVariants.SUCCESS -> theme.success
        AlertVariants.INFO -> theme.info
        AlertVariants.SECONDARY -> theme.secondary
        AlertVariants.DANGER -> theme.danger
    }

    BaseAlert(
        color = colors,
        icon = icon,
    ) { tint ->
        content(tint)
    }
}

@CampgroundComponent(description = "Displays a callout for user attention.")
@Composable
@Suppress("unused")
fun Alert(
    variant: AlertVariants = AlertVariants.DEFAULT,
    icon: IconComposable,
    title: String? = null,
    content: String
) {

    Alert(
        variant = variant,
        icon = icon,
    ) { tint ->
        if (title != null) {
            Text(title, color = tint, fontSize = 18.sp, lineHeight = 24.sp, letterSpacing = (-0.5).sp, fontWeight = FontWeight.W600)
        }
        Text(content, color = tint, fontSize = 16.sp, letterSpacing = (-0.5).sp, fontWeight = FontWeight.W400)
    }
}