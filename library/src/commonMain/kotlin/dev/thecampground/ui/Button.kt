package dev.thecampground.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults.buttonElevation
import androidx.compose.material3.LocalAbsoluteTonalElevation
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.thecampground.ui.annotation.CampgroundUIComponent
import dev.thecampground.ui.annotation.CampgroundUIComponentProp
import dev.thecampground.ui.annotation.CampgroundUIType


val DefaultButtonColors = ButtonColors(
    containerColor = Colors.BG_DARK,
    contentColor = Colors.BG,
    disabledContainerColor = Colors.BG_DARK.copy(alpha = 0.8f),
    disabledContentColor = Colors.BG.copy(alpha = 0.8f)
)

val PrimaryButtonColors = ButtonColors(
    containerColor = Colors.BRAND,
    contentColor = Colors.BG_DARK,
    disabledContainerColor = Colors.BRAND.copy(alpha = 0.8f),
    disabledContentColor = Colors.BG_DARK.copy(alpha = 0.8f)
)

val SecondaryButtonColors = ButtonColors(
    containerColor = Colors.SECONDARY_BUTTON,
    contentColor = Colors.BG_DARK,
    disabledContainerColor = Colors.SECONDARY_BUTTON.copy(alpha = 0.8f),
    disabledContentColor = Colors.BG_DARK.copy(alpha = 0.8f)
)

val GhostButtonColors = ButtonColors(
    containerColor = Color.Transparent,
    contentColor = Colors.BG_DARK,
    disabledContainerColor = Color.Transparent,
    disabledContentColor = Colors.BG_DARK.copy(alpha = 0.8f)
)

@Composable
internal expect fun ButtonFeedbackTest(feedback: InputTouchFeedback?): Unit


@CampgroundUIType
internal class ButtonVariant(val color: ButtonColors, val hoverColor: Color) {

    companion object {
        val DEFAULT = ButtonVariant(color = DefaultButtonColors, hoverColor = Colors.DEFAULT_BUTTON_HOVERED)
        val PRIMARY = ButtonVariant(color = PrimaryButtonColors, hoverColor = Colors.PRIMARY_BUTTON_HOVERED)
        val SECONDARY = ButtonVariant(color = SecondaryButtonColors, hoverColor = Colors.SECONDARY_BUTTON_HOVERED)
        val GHOST = ButtonVariant(color = GhostButtonColors, hoverColor = Colors.GHOST_BUTTON_HOVERED)
    }
}

@Composable
@CampgroundUIComponent(description = "A custom button components with multiple variations and sizes")
fun BaseButton(
    @CampgroundUIComponentProp(description = "Fires an event when the button is clicked.")
    onClick: () -> Unit,
    @CampgroundUIComponentProp(description = "Control the button sizes.")
    size: InputSizes = InputSizes.DEFAULT,
    @CampgroundUIComponentProp(description = "Set the button colours.")
    colors: ButtonColors = DefaultButtonColors,
    @CampgroundUIComponentProp(description = "Set the hover colour for the button.")
    hoverColor: Color = Colors.DEFAULT_BUTTON_HOVERED,
    @CampgroundUIComponentProp()
    modifier: Modifier = Modifier,
    @CampgroundUIComponentProp(description = "Have custom input feedback.")
    feedback: InputTouchFeedback? = null,
    @CampgroundUIComponentProp(description = "Add any content slot.")
    content: @Composable () -> Unit
) {


    val haptic = LocalHapticFeedback.current
    val paddingValue = when(size) {
        InputSizes.DEFAULT -> InputPaddingValues
        InputSizes.SMALL -> InputPaddingValuesSmall
        InputSizes.ICON -> InputPaddingValuesIcon
    }

    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()
    val isPressed by interactionSource.collectIsPressedAsState()
    val targetContainerColor by remember {
        derivedStateOf {
            when {
                isPressed -> colors.containerColor // Use base color when pressed
                isHovered -> hoverColor
                else -> colors.containerColor
            }
        }
    }

    val buttonScale by remember {
        derivedStateOf {
            return@derivedStateOf when (isPressed) {
                true -> 0.95f
                false -> 1f
            }
        }
    }
    val containerColorAnimated by animateColorAsState(targetContainerColor)
    val buttonScaleAnimated by animateFloatAsState(buttonScale)

    val containerColor by remember {
        derivedStateOf {
            return@derivedStateOf when (colors.containerColor == Color.Transparent) {
                true -> targetContainerColor
                false -> containerColorAnimated
            }
        }
    }
    CompositionLocalProvider(
        LocalContentColor provides colors.contentColor,
    ) {
        Box(
            modifier = modifier.scale(buttonScaleAnimated).clip(RoundedInputShape).background(containerColor)
                .clickable(
                    interactionSource = interactionSource,
                    indication = null,
                    role = Role.Button
                ) {
                    haptic.performHapticFeedback(HapticFeedbackType.Confirm)
                    onClick()
                },
            contentAlignment = Alignment.Center,
            propagateMinConstraints = true,
        ) {
            Row(Modifier.padding(paddingValue)) {
                content()
            }
        }
    }
}

@Composable
@CampgroundUIComponent(description = "A custom button components with multiple variations and sizes")
fun Button(
    onClick: () -> Unit,
    variant: ButtonVariants = ButtonVariants.DEFAULT,
    size: InputSizes = InputSizes.DEFAULT,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    val colors = when(variant) {
        ButtonVariants.DEFAULT -> ButtonVariant.DEFAULT
        ButtonVariants.PRIMARY -> ButtonVariant.PRIMARY
        ButtonVariants.SECONDARY -> ButtonVariant.SECONDARY
        ButtonVariants.GHOST -> ButtonVariant.GHOST
        else -> ButtonVariant.DEFAULT
    }

    BaseButton(
        onClick = onClick,
        colors = colors.color,
        hoverColor = colors.hoverColor,
        size = size,
        modifier = modifier
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
            content()
        }
    }
}

@Composable
@CampgroundUIComponent(description = "A custom button components with multiple variations and sizes")
fun Button(
    onClick: () -> Unit,
    variant: ButtonVariants = ButtonVariants.DEFAULT,
    size: InputSizes = InputSizes.DEFAULT,
    modifier: Modifier = Modifier,
    text: String = "Campground",
    icon: @Composable () -> Unit = {}
) {
    Button(onClick, variant, size, modifier) {
        icon()
        Text(text, fontWeight = FontWeight.SemiBold, letterSpacing = (-0.4).sp)
    }
}
