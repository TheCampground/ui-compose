package dev.thecampground.ui

import androidx.compose.ui.graphics.Color

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