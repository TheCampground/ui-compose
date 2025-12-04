package dev.thecampground.ui.showcase

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import camgroundui.showcase.composeapp.generated.resources.Res
import camgroundui.showcase.composeapp.generated.resources.compass
import camgroundui.showcase.composeapp.generated.resources.paintroller
import camgroundui.showcase.composeapp.generated.resources.sticky_note
import dev.thecampground.showcase.generated.CampgroundComponents
import dev.thecampground.ui.Button
import dev.thecampground.ui.ButtonVariants
import dev.thecampground.ui.Colors
import dev.thecampground.ui.showcase.presentation.documentation.components.ComponentDetailsScreen
import dev.thecampground.ui.showcase.presentation.defaultLink
import dev.thecampground.ui.showcase.presentation.root.Header
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun Docs() {
    val navigationMenuOpen = remember { mutableStateOf(false) }

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(Colors.BG_ALT)
    ) {
        val boxWidth = maxWidth

        Row(
            modifier = Modifier.fillMaxWidth().blur(0.dp)
        ) {
            AnimatedVisibility(boxWidth >= 800.dp, enter = fadeIn() + slideInHorizontally() + expandHorizontally(), exit = fadeOut() + slideOutHorizontally() + shrinkHorizontally()) {
                // Desktop / wide layout
                NavigationMenu()
            }

            Column {
                Header(hamburgerVisible = boxWidth < 800.dp, hamburgerOnClick = {
                    navigationMenuOpen.value = true
                })

                Column(verticalArrangement = Arrangement.spacedBy(18.dp), modifier = Modifier.padding(14.dp)) {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text("Introduction", fontSize = 32.sp, fontWeight = FontWeight.W700)
                        Text(
                            "A collection of themed UI components.",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.W400,
                            letterSpacing = (-0.4).sp,
                            color = Colors.TEXT_ALT
                        )
                    }

                    Text(
                        buildAnnotatedString {
                            append("campground/compose-ui is a collection of themed components for ")
                            defaultLink("https://github.com/TheCampground", "@TheCampground")
                            append(", with accessibility in mind.")
                        },
                        fontSize = 16.sp,
                        fontWeight = FontWeight.W400,
                        letterSpacing = (-0.4).sp,
                        color = Colors.BG_DARK
                    )
                }

                val compDef = CampgroundComponents.components

                for (comp in compDef) {
                    Text("$comp")
                }
            }

        }

        // Floating navigation overlay for small screens
        if (boxWidth < 800.dp) {
            AnimatedVisibility(visible = navigationMenuOpen.value, enter = fadeIn() + slideInHorizontally(), exit = fadeOut() + slideOutHorizontally()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    // This transparent clickable box captures outside clicks
                    .clickable(
                        // Indication = null prevents ripple effect
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) {
                        @Suppress("UNUSED_VARIABLE")
                        navigationMenuOpen.value = false
                    }
            ) {
                // Your actual navigation menu
                Box(
                    modifier = Modifier
                        .align(Alignment.TopStart) // example: right side drawer
                        .width(300.dp)
                        .fillMaxHeight()
                        .background(Colors.BG_ALT)
                        // Prevent clicks inside the menu from closing it
                        .clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }
                        ) { /* consume click only */ }
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Colors.BG_DARK.copy(alpha = 0.0f))
                    ) {
                        NavigationMenu(
                            modifier = Modifier
                                .align(Alignment.Center)
                                .background(Colors.BG_ALT)
                        )
                    }
                }
            }
            }
        }
    }
}

@Composable
fun NavigationMenu(@Suppress("UNUSED_VARIABLE") modifier: Modifier = Modifier) {
    val componentDefinitions = CampgroundComponents.components
    Column(
        modifier = Modifier.background(Colors.BG).fillMaxHeight().width(300.dp)
            .padding(18.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(18.dp), modifier = modifier) {
            Column {
                NavigationMenuSubTitle("DOCS")
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Button(
                        onClick = {},
                        variant = ButtonVariants.SECONDARY,
                        text = "Introduction",
                        modifier = Modifier.fillMaxWidth()
                    ) { tint, size ->
                        Icon(
                            painterResource(Res.drawable.sticky_note),
                            contentDescription = "Sticky Note",
                            modifier = Modifier.size(size),
                            tint = tint,
                        )
                    }
                    Button(
                        onClick = {},
                        variant = ButtonVariants.GHOST,
                        text = "Getting Started",
                        modifier = Modifier.fillMaxWidth()
                    ) { tint, size ->
                        Icon(
                            painterResource(Res.drawable.compass),
                            contentDescription = "Compass",
                            modifier = Modifier.size(size),
                            tint = tint
                        )
                    }
                    Button(
                        onClick = {},
                        variant = ButtonVariants.GHOST,
                        text = "Styling",
                        modifier = Modifier.fillMaxWidth()
                    ) {  tint, size ->
                        Icon(
                            painterResource(Res.drawable.paintroller),
                            contentDescription = "Paint Roller",
                            modifier = Modifier.size(size),
                            tint = tint
                        )
                    }
                }

            }

            val verticalScrollComponents = rememberScrollState()
            Column {
                NavigationMenuSubTitle("COMPONENTS")

                Column(modifier = Modifier.verticalScroll(verticalScrollComponents)) {
                    for (compName in componentDefinitions) {
                        NavigationComponentItem(compName.key)
                    }
                }
            }
        }
    }
}

@Composable
fun NavigationComponentItem(text: String) {
    val navigator = LocalNavigator.currentOrThrow

    Button(variant = ButtonVariants.GHOST, modifier = Modifier.fillMaxWidth(), onClick = {
        navigator.push(
        ComponentDetailsScreen(text))}
    ) {
        Text(text, fontSize = 14.sp)
    }
}
@Composable
fun NavigationMenuSubTitle(text: String) {
    Text(text, color = Colors.BRAND_FOREGROUND.copy(alpha = 0.7f), letterSpacing = (-0.4).sp, fontSize = 12.sp, fontWeight = FontWeight.Bold)
}
