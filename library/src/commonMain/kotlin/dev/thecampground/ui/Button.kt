package dev.thecampground.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
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
import dev.thecampground.ui.annotation.CampgroundType


private const val BUTTON_ICON_SIZE = 18


@Composable
@CampgroundComponent(description = "A custom button components with multiple variations and sizes")
fun BaseButton(
    @CampgroundProp(description = "Fires an event when the button is clicked.")
    onClick: () -> Unit,
    @CampgroundProp(description = "Control the button sizes.")
    size: InputSizes = InputSizes.DEFAULT,
    @CampgroundProp(description = "Set the button colours.")
    colors: ButtonColor,
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

    val targetContainerColor by remember {
        derivedStateOf {
            when {
                isPressed -> colors.hoveredBackground // Use base color when pressed
                isHovered || isPressed -> colors.hoveredBackground
                else -> colors.background
            }
        }
    }

    val buttonScale by remember {
        derivedStateOf {
            return@derivedStateOf when {
                isHovered && !isPressed -> 0.98f
                isPressed -> 0.95f
                else -> 1f
            }
        }
    }
    val containerColorAnimated by animateColorAsState(targetContainerColor)
    val buttonScaleAnimated by animateFloatAsState(buttonScale)

    val containerColor by remember {
        derivedStateOf {
            return@derivedStateOf when (colors.background == Color.Transparent) {
                true -> targetContainerColor
                false -> containerColorAnimated
            }
        }
    }

        Box(
            modifier = modifier.scale(buttonScaleAnimated).clip(RoundedInputShape).background(containerColor)
                .clickable(
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
                icon(colors.foreground, BUTTON_ICON_SIZE.dp)

                if (content != null) content(colors.foreground)
            }
        }
}

@Composable
@CampgroundComponent(description = "A custom button components with multiple variations and sizes")
fun Button(
    onClick: () -> Unit,
    variant: ButtonVariants = ButtonVariants.DEFAULT,
    size: InputSizes = InputSizes.DEFAULT,
    modifier: Modifier = Modifier,
    icon: IconComposable = { _, _ -> },
    content: TextComposable?,
) {
    val theme = LocalCampgroundTheme.current.button

    val colors = when(variant) {
        ButtonVariants.DEFAULT -> theme.default
        ButtonVariants.PRIMARY -> theme.primary
        ButtonVariants.SECONDARY -> theme.secondary
        ButtonVariants.GHOST -> theme.ghost
        else -> theme.ghost
    }

    BaseButton(
        onClick = onClick,
        colors = colors,
        size = size,
        modifier = modifier,
        icon = icon,
        content = content,
    )
}

@Composable
@CampgroundComponent(description = "A custom button components with multiple variations and sizes")
fun Button(
    onClick: () -> Unit,
    variant: ButtonVariants = ButtonVariants.DEFAULT,
    size: InputSizes = InputSizes.DEFAULT,
    modifier: Modifier = Modifier,
    text: String = "Campground",
    icon: IconComposable = { _, _ -> }
) {
    Button(onClick, variant, size, modifier, icon = icon) {
        Text(text, fontWeight = FontWeight.SemiBold, letterSpacing = (-0.4).sp, color = it)
    }
}
