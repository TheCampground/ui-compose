package dev.thecampground.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.unit.dp

val InputPaddingValues = PaddingValues(vertical = 7.dp, horizontal = 12.dp)
val InputPaddingValuesSmall = PaddingValues(vertical = 4.dp, horizontal = 8.dp)
val InputPaddingValuesIcon = PaddingValues(8.dp)

val RoundedPreviewShape = RoundedCornerShape(14.dp)

val RoundedInputShape = RoundedCornerShape(10.dp)
val RoundedInputShapePill = RoundedCornerShape(16.dp)
val RoundedInputShapeFull = RoundedCornerShape(32.dp)
enum class ButtonVariants {
    DEFAULT,
    PRIMARY,
    SECONDARY,
    OUTLINE,
    GHOST,
    DANGER,
    LINK
}

enum class InputTouchFeedback {
    LOW,
    MEDIUM,
    HIGH
}

enum class InputSizes {
    DEFAULT,
    SMALL,
    ICON
}

enum class AccordionTypes {
    SINGLE,
    MULTIPLE
}

enum class AlertVariants {
    DEFAULT,
    SUCCESS,
    INFO,
    SECONDARY,
    DANGER
}