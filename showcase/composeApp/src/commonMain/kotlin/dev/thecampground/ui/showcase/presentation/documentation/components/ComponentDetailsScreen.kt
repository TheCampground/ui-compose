package dev.thecampground.ui.showcase.presentation.documentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.ScreenKey
import camgroundui.showcase.composeapp.generated.resources.Res
import camgroundui.showcase.composeapp.generated.resources.campground
import dev.snipme.highlights.DefaultHighlightsResultListener
import dev.snipme.highlights.Highlights
import dev.snipme.highlights.model.CodeHighlight
import dev.snipme.highlights.model.SyntaxLanguage
import dev.snipme.highlights.model.SyntaxTheme
import dev.thecampground.showcase.generated.CampgroundComponents
import dev.thecampground.ui.Button
import dev.thecampground.ui.ButtonVariants
import dev.thecampground.ui.Colors
import dev.thecampground.ui.InputSizes
import dev.thecampground.ui.LocalCampgroundTheme
import dev.thecampground.ui.RoundedPreviewShape
import dev.thecampground.ui.showcase.internal.generateAnnotatedString
import dev.thecampground.ui.showcase.presentation.documentation.DocumentationRoot
import dev.thecampground.ui.showcase.sourceCodeProFamily
import org.jetbrains.compose.resources.painterResource

data class ComponentDetailsScreen(val component: String) : Screen {
    override val key: ScreenKey = component

    @Composable
    override fun Content() {
        val component = CampgroundComponents.components[component]?.first()
        val scrollState = rememberScrollState()
        val theme = LocalCampgroundTheme.current

        BoxWithConstraints(modifier = Modifier.verticalScroll(scrollState)) {
            if (component == null) {
                Text("A component was mentioned but it's documentation could not be found.")
            } else {
                DocumentationRoot(
                    name = component.name,
                    description = component.description,
                    theme,
                ) {

                    val firstExample = null
                    if (firstExample != null) {

                        val highlights by remember {
                            mutableStateOf(
                                Highlights.Builder(
                                    code = firstExample,
                                    language = SyntaxLanguage.KOTLIN,
                                    theme = SyntaxTheme(
                                        key = "VESPER_LIGHT",
                                        code = 0x00000,
                                        keyword = 0x505050,
                                        string = 0x008080,
                                        literal = 0xFF8C00,
                                        comment = 0x8B8B8B,
                                        metadata = 0xFF8C00,
                                        multilineComment = 0x8B8B8B,
                                        punctuation = 0x505050,
                                        mark = 0xFF8C00,
                                    )
                                ).build()
                            )
                        }
                        var currentText by remember { mutableStateOf(AnnotatedString("")) }

                        LaunchedEffect(highlights) {
                            highlights.getHighlightsAsync(object :
                                DefaultHighlightsResultListener() {
                                override fun onSuccess(result: List<CodeHighlight>) {
                                    currentText = result.generateAnnotatedString(firstExample)
                                }
                            })
                        }

                        Column(
                            modifier = Modifier.fillMaxWidth( if (maxWidth < 800.dp) 1f else .8f).clip(RoundedPreviewShape).border(
                                1.dp,
                                color = Colors.BORDER.copy(alpha = 0.2f),
                                shape = RoundedPreviewShape
                            )
                        ) {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier.background(Colors.BG).padding(32.dp)
                                    .fillMaxWidth()
                                    .defaultMinSize(minHeight = 128.dp)
                            ) {
//                                component.example?.let { it() }
                            }

                            Column(
                                modifier = Modifier.fillMaxWidth().background(Colors.BG_SECONDARY)
                            ) {
                                Column {
                                    HorizontalDivider(color = Colors.BORDER.copy(alpha = 0.1f))
                                    Row(
                                        modifier = Modifier.background(Colors.GHOST_BUTTON_HOVERED)
                                            .fillMaxWidth().padding(horizontal = 12.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text("Composable.kt")

                                        Button(
                                            onClick = {},
                                            variant = ButtonVariants.GHOST,
                                            size = InputSizes.ICON,
                                            icon = { tint, size ->
                                                Icon(
                                                    painterResource(Res.drawable.campground),
                                                    contentDescription = "",
                                                    tint = tint,
                                                    modifier = Modifier.size(size)
                                                )
                                            },
                                            content = null
                                        )
                                    }
                                    HorizontalDivider(color = Colors.BORDER.copy(alpha = 0.1f))
                                }
                                Text(
                                    currentText,
                                    fontFamily = sourceCodeProFamily(),
                                    modifier = Modifier.padding(
                                        horizontal = 12.dp,
                                        vertical = 14.dp
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}