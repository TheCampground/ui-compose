package dev.thecampground.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import camgroundui.library.generated.resources.Res
import camgroundui.library.generated.resources.caret_up
import dev.thecampground.ui.annotation.CampgroundComponent
import dev.thecampground.ui.annotation.CampgroundType
import org.jetbrains.compose.resources.painterResource
import kotlin.random.Random

@CampgroundType
typealias AccordionActionScope = @Composable (id: String, controller: AccordionController) -> Unit

class AccordionController(
    private val multiOpen: Boolean
) {
    private val itemStates =
        mutableStateMapOf<String, MutableState<Boolean>>().withDefault { mutableStateOf(false) }

    fun registerItem(id: String) {
        itemStates[id] = mutableStateOf(false)
    }

    fun isOpen(id: String): State<Boolean> =
        itemStates.getValue(id)

    fun toggle(id: String) {
        val current = itemStates.getValue(id)

        val newValue = !current.value
        current.value = newValue

        if (!multiOpen && newValue) {
            // close all other open items
            itemStates.forEach { (otherId, state) ->
                if (otherId != id && state.value) {
                    state.value = false
                }
            }
        }
    }
}

val LocalAccordionController = staticCompositionLocalOf<AccordionController> {
    error("AccordionItem must be inside AccordionRoot")
}

@Composable
@CampgroundComponent(description = "A custom button components with multiple variations and sizes")
fun AccordionRoot(
    type: AccordionTypes = AccordionTypes.SINGLE,
    content: @Composable () -> Unit
) {
    val controller = remember { AccordionController(type == AccordionTypes.MULTIPLE) }

    Column(verticalArrangement = Arrangement.spacedBy(18.dp)) {
        CompositionLocalProvider(
            LocalAccordionController provides controller
        ) {
            content()
        }
    }
}

@Composable
@CampgroundComponent(description = "A custom button components with multiple variations and sizes")
fun BaseAccordionItem(
    id: String = remember { Random.nextBytes(24).toString() },
    enabled: Boolean = true,
    action: AccordionActionScope = { id, controller ->
        Button(
            variant = ButtonVariants.GHOST,
            size = InputSizes.ICON,
            onClick = { controller.toggle(id) })
        { tint ->
            val isOpen = LocalAccordionController.current.isOpen(id)
            val rotateState by animateFloatAsState(if (isOpen.value) 180f else 0f)

            Icon(
                painterResource(Res.drawable.caret_up),
                contentDescription = "Caret",
                tint = tint,
                modifier = Modifier.rotate(rotateState).size(18.dp)
            )
        }
    },
    title: @Composable () -> Unit,
    content: @Composable () -> Unit,
) {
    val controller = LocalAccordionController.current
    val theme = LocalCampgroundTheme.current
    val interactionSource = remember { MutableInteractionSource() }

    // Register this item so the controller knows it
    LaunchedEffect(id) {
        controller.registerItem(id)
    }

    val isOpen by controller.isOpen(id)

    Column(
        modifier = Modifier.clickable(
            enabled = enabled,
            interactionSource = interactionSource,
            indication = null,
            role = Role.DropdownList
        ) {
            controller.toggle(id)
        },
//        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            title()

            action(id, controller)
        }

        AnimatedVisibility(
            visible = isOpen,
            enter = fadeIn(spring(stiffness = Spring.StiffnessMedium)) + expandVertically(
                clip = false,
                animationSpec = spring(stiffness = Spring.StiffnessMedium)
            ),
            exit = fadeOut(animationSpec = spring(stiffness = Spring.StiffnessMedium)) + shrinkVertically(
                clip = false,
                animationSpec = spring(stiffness = Spring.StiffnessMedium)
            ),
        ) {
            content()
        }

        Box(
            modifier = Modifier.padding(top = 12.dp).fillMaxWidth().height(1.dp)
                .background(theme.inverse.copy(alpha = 0.2f))
        )
    }
}

@Composable
fun AccordionItem(
    id: String = remember { Random.nextBytes(24).toString() },
    action: AccordionActionScope = { id, controller ->
        Button(
            variant = ButtonVariants.GHOST,
            size = InputSizes.ICON,
            onClick = { controller.toggle(id) })
        { tint ->
            val isOpen = LocalAccordionController.current.isOpen(id)
            val rotateState by animateFloatAsState(if (isOpen.value) 180f else 0f)

            Icon(
                painterResource(Res.drawable.caret_up),
                contentDescription = "Caret",
                tint = tint,
                modifier = Modifier.rotate(rotateState).size(18.dp)
            )
        }
    },
    title: String,
    content: String
) {
    val theme = LocalCampgroundTheme.current

    BaseAccordionItem(
        id,
        action = action,
        title = {
            Text(
                title,
                fontSize = 24.sp,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = (-0.4).sp,
                color = theme.text.default
            )
        },
    ) {
        Text(
            content,
            fontWeight = FontWeight.Normal,
            letterSpacing = (-0.4).sp,
            color = theme.text.secondary
        )
    }
}