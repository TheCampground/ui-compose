package dev.thecampground.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
@CampgroundComponent(name = "Alert", description = "Displays a callout for user attention.")
@Suppress("unused")
fun BaseAlert(
    @CampgroundProp(description = "The variant of the alert")
    color: AlertColor,
    hasTitle: Boolean = true,
    icon: IconComposable,
    content: TextComposable
) {
    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .clip(RoundedInputShape)
                .border(
                    width = 1.dp,
                    color = color.outline,
                    shape = RoundedInputShape
                )
                .background(color.background)
                .padding(12.dp),
        verticalAlignment = if (hasTitle) Alignment.Top else Alignment.CenterVertically
    ) {

        icon(color.foreground, ALERT_ICON_SIZE.dp)

        Column(modifier = Modifier.padding(start = 14.dp), verticalArrangement = Arrangement.Center) {
            content(color.foreground)
        }
    }
}

@Composable
@CampgroundComponent(name = "Alert", description = "Displays a callout for user attention.")
@Suppress("unused")
fun Alert(
    variant: AlertVariants = AlertVariants.DEFAULT,
    hasTitle: Boolean = true,
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
        hasTitle = hasTitle,
    ) { tint ->
        content(tint)
    }
}

@CampgroundComponent(name = "Alert", description = "Displays a callout for user attention.")
@Composable
@Suppress("unused")
fun Alert(
    variant: AlertVariants = AlertVariants.DEFAULT,
    icon: IconComposable,
    title: String? = null,
    content: TextComposable
) {

    Alert(
        variant = variant,
        icon = icon,
        hasTitle = title != null,
    ) { tint ->
        if (title != null) {
            Text(
                title,
                color = tint,
                fontSize = 18.sp,
                lineHeight = 24.sp,
                letterSpacing = (-0.5).sp,
                fontWeight = FontWeight.SemiBold
            )
        }

        content(tint)
    }
}

@CampgroundComponent(name = "Alert", description = "Displays a callout for user attention.")
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
        title = title,
    ) { tint ->
        Text(
            content,
            color = tint,
            fontSize = 16.sp,
            letterSpacing = (-0.5).sp,
            fontWeight = FontWeight.Normal
        )
    }
}