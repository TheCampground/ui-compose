package dev.thecampground.ui.showcase.presentation.documentation

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import org.jetbrains.compose.ui.tooling.preview.Preview
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
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.FadeTransition
import camgroundui.showcase.composeapp.generated.resources.Res
import camgroundui.showcase.composeapp.generated.resources.compass
import camgroundui.showcase.composeapp.generated.resources.paintroller
import camgroundui.showcase.composeapp.generated.resources.sticky_note
import dev.thecampground.showcase.generated.CampgroundComponents
import dev.thecampground.ui.BaseCampgroundTheme
import dev.thecampground.ui.Button
import dev.thecampground.ui.ButtonVariants
import dev.thecampground.ui.Colors
import dev.thecampground.ui.LocalCampgroundTheme
import dev.thecampground.ui.showcase.presentation.documentation.components.ComponentDetailsScreen
import dev.thecampground.ui.showcase.presentation.root.Header
import org.jetbrains.compose.resources.painterResource

class DocumentationScreen : Screen {
    @Composable
    @Preview
    override fun Content() {
        // Header
        val navigationMenuOpen = remember { mutableStateOf(false) }
        val theme = LocalCampgroundTheme.current
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .background(theme.background)
        ) {
            val boxWidth = maxWidth
            Column {
                Header(hamburgerVisible = boxWidth < 800.dp, hamburgerOnClick = {
                    navigationMenuOpen.value = true
                })
                Navigator(IntroductionScreen()) { navigator ->
                    Box {


                        Row(
                            modifier = Modifier.fillMaxWidth().blur(0.dp)
                        ) {
                            AnimatedVisibility(
                                boxWidth >= 800.dp,
                                enter = fadeIn() + slideInHorizontally() + expandHorizontally(),
                                exit = fadeOut() + slideOutHorizontally() + shrinkHorizontally()
                            ) {
                                // Desktop / wide layout
                                NavigationMenu(navigator, theme, onClick = {})
                            }

                            Column {
                                Box(
                                    contentAlignment = Alignment.TopCenter,
                                    modifier = Modifier.fillMaxSize()
                                ) {
                                    FadeTransition(navigator = navigator) { screen ->
                                        screen.Content()
                                    }
                                }
                            }

                        }

                        // Floating navigation overlay for small screens
                        if (boxWidth < 800.dp) {
                            androidx.compose.animation.AnimatedVisibility(
                                navigationMenuOpen.value,
                                enter = fadeIn() + slideInHorizontally(),
                                exit = fadeOut() + slideOutHorizontally()
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        // This transparent clickable box captures outside clicks
                                        .clickable(
                                            // Indication = null prevents ripple effect
                                            indication = null,
                                            interactionSource = remember { MutableInteractionSource() }
                                        ) {
                                            navigationMenuOpen.value = false

                                        }
                                ) {
                                    // Your actual navigation menu
                                    Box(
                                        modifier = Modifier
                                            .align(Alignment.TopStart) // example: right side drawer
                                            .width(300.dp)
                                            .fillMaxHeight()
                                            .background(theme.alternative)
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
                                                navigator,
                                                theme,
                                                onClick = {
                                                    navigationMenuOpen.value = false
                                                },
                                                modifier = Modifier
                                                    .align(Alignment.Center)
                                                    .background(theme.alternative)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @Composable
    private fun NavigationMenu(navigator: Navigator, theme: BaseCampgroundTheme, onClick: () -> Unit, modifier: Modifier = Modifier) {
        val componentDefinitions = CampgroundComponents.components
        val currentScreen = navigator.lastItem

        Row(modifier = Modifier.fillMaxHeight().width(300.dp).background(theme.alternative)) {
            Column(
                modifier = modifier.fillMaxHeight().width(300.dp)
                    .padding(18.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(18.dp), modifier = modifier) {
                    Column {
                        NavigationMenuSubTitle("DOCS", theme)
                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            if (currentScreen is IntroductionScreen) {
                                Button(
                                    onClick = {
                                        navigator.push(IntroductionScreen())
                                        onClick()
                                    },
                                    variant = ButtonVariants.SECONDARY,
                                    text = "Introduction",
                                    modifier = Modifier.fillMaxWidth()
                                ) { tint, size ->
                                    Icon(
                                        painterResource(Res.drawable.sticky_note),
                                        contentDescription = "Sticky Note",
                                        modifier = Modifier.size(size),
                                        tint = tint
                                    )
                                }
                            } else {
                                Button(
                                    onClick = {
                                        navigator.push(IntroductionScreen())
                                        onClick()
                                    },
                                    variant = ButtonVariants.GHOST,
                                    text = "Introduction",
                                    modifier = Modifier.fillMaxWidth()
                                ) { tint, size ->
                                    Icon(
                                        painterResource(Res.drawable.sticky_note),
                                        contentDescription = "Sticky Note",
                                        modifier = Modifier.size(size),
                                        tint = tint
                                    )
                                }
                            }

                            if (currentScreen is GettingStartedScreen) {
                                Button(
                                    onClick = {
                                        navigator.push(IntroductionScreen())
                                        onClick()
                                    },
                                    variant = ButtonVariants.SECONDARY,
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
                            } else {
                                Button(
                                    onClick = {
                                        navigator.push(GettingStartedScreen())
                                        onClick()
                                    },
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
                            }
                            if (currentScreen is StylingScreen) {
                                Button(
                                    onClick = {
                                        navigator.push(StylingScreen())
                                        onClick()
                                    },
                                    variant = ButtonVariants.SECONDARY,
                                    text = "Styling",
                                    modifier = Modifier.fillMaxWidth()
                                ) { tint, size ->
                                    Icon(
                                        painterResource(Res.drawable.paintroller),
                                        contentDescription = "Paint roller",
                                        modifier = Modifier.size(size),
                                        tint = tint
                                    )
                                }
                            } else {
                                Button(
                                    onClick = {
                                        navigator.push(StylingScreen())
                                        onClick()
                                    },
                                    variant = ButtonVariants.GHOST,
                                    text = "Styling",
                                    modifier = Modifier.fillMaxWidth()
                                ) { tint, size ->
                                    Icon(
                                        painterResource(Res.drawable.paintroller),
                                        contentDescription = "Paint roller",
                                        modifier = Modifier.size(size),
                                        tint = tint
                                    )
                                }
                            }
                        }
                    }

                    val verticalScrollComponents = rememberScrollState()
                    Column {
                        NavigationMenuSubTitle("COMPONENTS", theme)

                        Column(modifier = Modifier.verticalScroll(verticalScrollComponents)) {
                            for (compName in componentDefinitions) {
                                NavigationComponentItem(navigator, onClick, compName.key)
                            }
                        }
                    }
                }
            }

            VerticalDivider(color = theme.inverse.copy(alpha = 0.1f))
        }
    }

    @Composable
    private fun NavigationComponentItem(navigator: Navigator, onClick: () -> Unit, text: String) {
        val currentScreen = navigator.lastItem

        if (currentScreen is ComponentDetailsScreen && currentScreen.component == text) {
            Button(variant = ButtonVariants.SECONDARY, modifier = Modifier.fillMaxWidth(), onClick = {
                navigator.push(
                    ComponentDetailsScreen(text)
                )
                onClick()
            }
            ) {
                Text(text, fontSize = 14.sp, color = it)
            }
        } else {
            Button(variant = ButtonVariants.GHOST, modifier = Modifier.fillMaxWidth(), onClick = {
                navigator.push(
                    ComponentDetailsScreen(text)
                )
                onClick()
            }
            ) {
                Text(text, fontSize = 14.sp, color = it)
            }
        }

    }

    @Composable
    private fun NavigationMenuSubTitle(text: String, theme: BaseCampgroundTheme) {
        Text(
            text,
            color = theme.text.secondary,
            letterSpacing = (-0.4).sp,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold
        )
    }

}



@Composable
fun DocumentationRoot(name: String = "Not provided", description: String = "Not provided", theme: BaseCampgroundTheme, content: @Composable () -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(18.dp), modifier = Modifier.padding(14.dp)) {
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(name, fontSize = 32.sp, fontWeight = FontWeight.W700, color = theme.text.default)
            Text(
                description,
                fontSize = 18.sp,
                fontWeight = FontWeight.W400,
                letterSpacing = (-0.4).sp,
                color = theme.text.secondary,
            )
        }

        content()
    }
}