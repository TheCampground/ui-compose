package dev.thecampground.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocal
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Stable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color


@Composable
expect fun isDarkTheme(): Boolean

val LocalCampgroundTheme = staticCompositionLocalOf {
    BaseCampgroundTheme.createLight()
}

class CampgroundTheme(
    val light: BaseCampgroundTheme = BaseCampgroundTheme.createLight(),
    val dark: BaseCampgroundTheme = BaseCampgroundTheme.createDark(),
) {
    companion object {

        val colors: BaseCampgroundTheme
            @Composable get() = LocalCampgroundTheme.current
    }
}


@Stable
data class BaseCampgroundTheme(
    // Background colors
    val background: Color = Color.hsl(33f, 1f, .96f),
    val secondary: Color = Color.hsl(35f, .58f, .94f),
    val alternative: Color = Color.hsl(34f, 0.25f, .83f),
    val inverse: Color = Color.hsl(0f, 0f, .08f),

    val text: TextColors = TextColors(
        default = inverse,
        secondary = Color.hsl(0f, 0f, 0.32f)
    ),
    // Brand
    val brand: Color = Color.hsl(31f, .96f, .78f),

    val button: ButtonColors = ButtonColors(),
    val alert: AlertColors = AlertColors()
) {

    companion object {
        fun createLight(): BaseCampgroundTheme {
            val base = BaseCampgroundTheme()

            return BaseCampgroundTheme(
                button = ButtonColors(
                    default = ButtonColor(
                        background = base.inverse,
                        foreground = base.background,
                        hoveredBackground = Color.hsl(.08f, .07f, .24f),
                        outline = null,
                    ),
                    primary = ButtonColor(
                        background = base.brand,
                        foreground = base.inverse,
                        hoveredBackground = Color.hsl(31f, .98f, .82f),
                        outline = null,
                    ),
                    secondary = ButtonColor(
                        background = Color.hsl(86f, .13f, .9f),
                        foreground = base.inverse,
                        hoveredBackground = Color.hsl(111f, .12f, .88f),
                        outline = null,
                    ),
                    danger = ButtonColor(
                        background = Color.hsl(347f, .6f, .5f),
                        foreground = base.inverse,
                        hoveredBackground = Color.hsl(34f, .45f, 0.9f),
                        outline = null,
                    ),
                    ghost = ButtonColor(
                        background = Color.Transparent,
                        foreground = base.inverse,
                        hoveredBackground = Color.hsl(34f, .45f, 0.9f),
                        outline = null,
                    ),
                    link = ButtonColor(
                        background = Color.Transparent,
                        foreground = base.inverse,
                        hoveredBackground = Color.Transparent,
                        outline = null,
                    ),
                ),
                alert = AlertColors(
                    default = AlertColor(
                        background = base.brand.copy(alpha = 0.3f),
                        foreground = base.inverse,
                        outline = base.brand.copy(alpha = 0.6f)
                    ),
                    success = run {
                        val success = Color.hsl(149f, .91f, .67f)

                        AlertColor(
                            background = success.copy(alpha = 0.3f),
                            foreground = success,
                            outline = success.copy(alpha = 0.6f)
                        )
                    },
                    info = run {
                        val info = Color.hsl(205f, .69f, .62f)

                        AlertColor(
                            background = info.copy(alpha = 0.3f),
                            foreground = info,
                            outline = info.copy(alpha = 0.6f)
                        )
                    },
                    secondary = run {
                        val secondary = Color.hsl(86f, .13f, .9f)

                        AlertColor(
                            background = secondary.copy(alpha = 0.3f),
                            foreground = secondary,
                            outline = secondary.copy(alpha = 0.6f)
                        )
                    },
                    danger = run {
                        val danger = Color.hsl(347f, .6f, .5f)

                        AlertColor(
                            background = danger.copy(alpha = 0.3f),
                            foreground = danger,
                            outline = danger.copy(alpha = 0.6f)
                        )
                    },
                )
            )
        }

        fun createDark(): BaseCampgroundTheme {
            val base = createLight()
            val inverse = Color.hsl(0f, 0f, .96f)
            val secondary = Color.hsl(0f, 0f, .1f)

            return BaseCampgroundTheme(
                background = base.inverse,
                secondary = Color.hsl(0f, 0f, .1f),
                alternative = Color.hsl(0f, 0f, .05f),
                inverse = inverse,
                brand = Color.hsl(31f, .96f, .78f),
                text = TextColors(
                    default = Color.hsl(0f, 0f, .87f),
                    secondary = Color.hsl(0f, 0f, .7f)
                ),
                button = ButtonColors(
                    default = ButtonColor(
                        background = inverse,
                        foreground = base.background,
                        hoveredBackground = Color.hsl(.0f, .0f, .85f),
                        outline = null,
                    ),
                    primary = base.button.primary.copy(
                        hoveredBackground = Color.hsl(31f, .59f, .7f)
                    ),
                    secondary = ButtonColor(
                        background = secondary,
                        foreground = inverse,
                        hoveredBackground = Color.hsl(0f, 0f, .12f),
                        outline = null,
                    ),
                    danger = base.button.danger.copy(
                        hoveredBackground = Color.hsl(347f, .6f, .45f)
                    ),
                    ghost = base.button.ghost.copy(
                        foreground = inverse,
                        hoveredBackground = secondary
                    ),
                    link = ButtonColor(
                        background = Color.Transparent,
                        foreground = base.inverse,
                        hoveredBackground = Color.Transparent,
                        outline = null,
                    ),
                ),
                alert = base.alert,
            )
        }
    }
}


@Stable
class TextColors(
    val default: Color,
    val secondary: Color,
    val alternative: Color = secondary,
)
@Stable
class ButtonColors(
    val default: ButtonColor = ButtonColor(),
    val primary: ButtonColor = ButtonColor(),
    val secondary: ButtonColor = ButtonColor(),
    val danger: ButtonColor = ButtonColor(),
    val outline: ButtonColor = ButtonColor(),
    val ghost: ButtonColor = ButtonColor(),
    val link: ButtonColor = ButtonColor(),
)

@Stable
class AlertColors(
    val default: AlertColor = AlertColor(),
    val success: AlertColor = AlertColor(),
    val info: AlertColor = AlertColor(),
    val secondary: AlertColor = AlertColor(),
    val danger: AlertColor = AlertColor(),
)


@Stable
class AlertColor(
    val background: Color = Color.Black,
    val foreground: Color = Color.White,
    val outline: Color = Color.Black,
)

@Stable
class ButtonColor(
    val background: Color = Color.Black,
    val foreground: Color = Color.White,
    val outline: Color? = null,

    val hoveredBackground: Color = background,
    val hoveredForeground: Color = foreground,

    val disabledBackground: Color = background.copy(alpha = 0.8f),
    val disabledForeground: Color = background.copy(alpha = 0.8f),
) {
    fun copy(
        background: Color = this.background,
        foreground: Color = this.foreground,
        outline: Color? = this.outline,
        hoveredBackground: Color = this.hoveredBackground,
        hoveredForeground: Color = this.hoveredForeground,
        disabledBackground: Color = this.disabledBackground,
        disabledForeground: Color = this.disabledForeground
    ) = ButtonColor(background, foreground, outline, hoveredBackground, hoveredForeground, disabledBackground, disabledForeground)
}


@Composable
fun CampgroundTheme(
    useDarkTheme: Boolean = isDarkTheme(),
    theme: CampgroundTheme = CampgroundTheme(),
    animate: Boolean = true, // TODO: Support animated theme switch
    content: @Composable () -> Unit,
) {

    val derivedTheme =  if (useDarkTheme) theme.dark else theme.light

        CompositionLocalProvider(
            LocalCampgroundTheme provides derivedTheme
        ) {
            content()
        }
}
object Colors {
    val BG = Color.hsl(33f, 1f, .96f)
    val BG_SECONDARY = Color.hsl(35f, .58f, .94f)
    val BG_ALT = Color.hsl(34f, 0.25f, .83f)
    val BG_DARK = Color.hsl(0f, 0f, .05f)
    val BRAND = Color.hsl(31f, .96f, .78f)
    val SECONDARY_BUTTON = Color.hsl(86f, .13f, .9f)
    val DEFAULT_BUTTON_HOVERED = Color.hsl(.08f, .07f, .24f)
    val PRIMARY_BUTTON_HOVERED = Color.hsl(31f, .98f, .82f)
    val SECONDARY_BUTTON_HOVERED = Color.hsl(111f, .12f, .88f)
    val GHOST_BUTTON_HOVERED = Color.hsl(34f, .45f, 0.9f)
    val BRAND_FOREGROUND = Color.hsl(12f, 0.06f, .15f)
    val BORDER = Color.hsl(240f, 0.06f, .1f)

    val TEXT_ALT = Color.hsl(0f, 0f, 0.32f)
    // Alert default
    val ALERT_DEFAULT_BACKGROUND = BRAND.copy(alpha = 0.3f)
    val ALERT_DEFAULT_BORDER = BRAND.copy(alpha = 0.6f)

    // Alert success
    val SUCCESS = Color.hsl(149f, .91f, .67f)
    val ALERT_SUCCESS_BACKGROUND = SUCCESS.copy(alpha = .30f)
    val ALERT_SUCCESS_BORDER = SUCCESS.copy(alpha = 0.60f)

    // Alert info
    val INFO = Color.hsl(205f, .69f, .62f)
    val ALERT_INFO_BACKGROUND = INFO.copy(alpha = .30f)
    val ALERT_INFO_BORDER = INFO.copy(alpha = 0.60f)

    // Secondary
    val ALERT_SECONDARY_BACKGROUND = SECONDARY_BUTTON.copy(alpha = .30f)
    val ALERT_SECONDARY_FOREGROUND = BRAND_FOREGROUND

    // Alert danger
    val DANGER = Color.hsl(345.6f, .60f, .50f)
    val ALERT_DANGER_BACKGROUND = DANGER.copy(alpha = .20f)
    val ALERT_DANGER_BORDER = DANGER.copy(alpha = 0.60f)
    val ALERT_DANGER_FOREGROUND = DANGER
}