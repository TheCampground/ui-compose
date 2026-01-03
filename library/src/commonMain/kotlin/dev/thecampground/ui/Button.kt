package dev.thecampground.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.thecampground.ui.annotation.CampgroundComponent
import dev.thecampground.ui.annotation.CampgroundProp


private const val BUTTON_ICON_SIZE = 18


@Composable
@CampgroundComponent(
    name = "Button",
    description = "A custom button components with multiple variations and sizes"
)
fun BaseButton(
    @CampgroundProp(description = "Sets if the button should be clickable or not.")
    enabled: Boolean = true,
    @CampgroundProp(description = "Fires an event when the button is clicked.")
    onClick: () -> Unit,
    @CampgroundProp(description = "Control the button sizes.")
    size: InputSizes = InputSizes.DEFAULT,
    @CampgroundProp(description = "Set the button colours.")
    color: ButtonColor,
    @CampgroundProp()
    modifier: Modifier = Modifier,
    @CampgroundProp(description = "Have custom input feedback.")
    feedback: HapticFeedbackType? = HapticFeedbackType.Confirm,
    icon: IconComposable,
    @CampgroundProp(description = "Add any content slot.")
    content: TextComposable?
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

    val targetContainerColor by derivedStateOf {
        when {
            !enabled -> color.disabledBackground
            isHovered || isPressed -> color.hoveredBackground
            else -> color.background
        }
    }

    val buttonScale by derivedStateOf {
        when {
            isPressed -> 0.95f
            isHovered -> 0.98f
            else -> 1f
        }
    }

    val containerColorAnimated by animateColorAsState(targetContainerColor)
    val buttonScaleAnimated by animateFloatAsState(buttonScale)

    val containerColor by derivedStateOf {
        when (color.background == Color.Transparent) {
            true -> targetContainerColor
            false -> containerColorAnimated
        }
    }

        Box(
            modifier = modifier
                .scale(buttonScaleAnimated)
                .clip(RoundedInputShape)
                .border(
                    shape = RoundedInputShape,
                    color = color.outline ?: Color.Transparent,
                    width = 1.dp
                )
                .background(containerColor)
                .clickable(
                    enabled = enabled,
                    interactionSource = interactionSource,
                    indication = null,
                    role = Role.Button
                ) {
                    if (feedback != null) haptic.performHapticFeedback(feedback)
                    onClick()
                },
            contentAlignment = Alignment.Center,
            propagateMinConstraints = true,
        ) {
            Row(Modifier.padding(paddingValue), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                icon(color.foreground, BUTTON_ICON_SIZE.dp)

                if (content != null) content(color.foreground)
            }
        }
}

@Composable
@CampgroundComponent(
    name = "Button",
    description = "A custom button components with multiple variations and sizes"
)
fun Button(
    @CampgroundProp(description = "Sets if the button should be clickable or not.")
    enabled: Boolean = true,
    @CampgroundProp(description = "Fires an event when the button is clicked.")
    onClick: () -> Unit,
    @CampgroundProp(description = "The variant of the default trigger.")
    variant: ButtonVariants = ButtonVariants.DEFAULT,
    @CampgroundProp(description = "The size of the default trigger.")
    size: InputSizes = InputSizes.DEFAULT,
    modifier: Modifier = Modifier,
    @CampgroundProp(description = "A composable function to specify the icon with a set tint, and size.")
    icon: IconComposable = { _, _ -> },
    @CampgroundProp(description = "Content of the composable.")
    content: TextComposable?,
) {
    val theme = LocalCampgroundTheme.current.button

    val colors = when(variant) {
        ButtonVariants.DEFAULT -> theme.default
        ButtonVariants.PRIMARY -> theme.primary
        ButtonVariants.SECONDARY -> theme.secondary
        ButtonVariants.GHOST -> theme.ghost
        ButtonVariants.DANGER -> theme.danger
        ButtonVariants.OUTLINE -> theme.outline
        ButtonVariants.LINK -> theme.link
    }

    BaseButton(
        enabled = enabled,
        onClick = onClick,
        color = colors,
        size = size,
        modifier = modifier,
        icon = icon,
        content = content,
    )
}

@Composable
@CampgroundComponent(
    name = "Button",
    description = "A custom button components with multiple variations and sizes"
)
fun Button(
    enabled: Boolean = true,
    onClick: () -> Unit,
    variant: ButtonVariants = ButtonVariants.DEFAULT,
    size: InputSizes = InputSizes.DEFAULT,
    modifier: Modifier = Modifier,
    text: String = "Campground",
    icon: IconComposable = { _, _ -> },
) {
    Button(
        enabled = enabled,
        onClick = onClick,
        variant = variant,
        size = size,
        modifier = modifier,
        icon = icon
    ) {
        Text(text, fontWeight = FontWeight.SemiBold, letterSpacing = (-0.4).sp, color = it)
    }
}
