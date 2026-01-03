package dev.thecampground.ui.showcase.internal.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import camgroundui.showcase.composeapp.generated.resources.Res
import camgroundui.showcase.composeapp.generated.resources.campground
import dev.snipme.highlights.DefaultHighlightsResultListener
import dev.snipme.highlights.Highlights
import dev.snipme.highlights.model.CodeHighlight
import dev.snipme.highlights.model.SyntaxLanguage
import dev.snipme.highlights.model.SyntaxThemes
import dev.thecampground.ui.Button
import dev.thecampground.ui.ButtonVariants
import dev.thecampground.ui.InputSizes
import dev.thecampground.ui.LocalCampgroundTheme
import dev.thecampground.ui.RoundedPreviewShape
import dev.thecampground.ui.showcase.domain.CampgroundCodeExample
import dev.thecampground.ui.showcase.internal.generateAnnotatedString
import dev.thecampground.ui.showcase.presentation.documentation.DocumentationRoot
import dev.thecampground.ui.showcase.sourceCodeProFamily
import org.jetbrains.compose.resources.painterResource


private val buttonExample = CampgroundCodeExample(
    code = """
    Button(
        onClick = {},
        variant = ButtonVariants.PRIMARY,
        text = "Unlimited"
    )
    """.trimIndent()
) {
    Button(
        onClick = {},
        variant = ButtonVariants.PRIMARY,
        text = "Unlimited"
    )
}

private val LightSyntaxTheme = SyntaxThemes.monokai()

private val DarkSyntaxTheme = SyntaxThemes.monokai(darkMode = true)

@Composable
fun ComponentCodePreview(example: String? = null, content: @Composable () -> Unit) {
    val componentCode = "@Composable\nfun MyComponent() {\n\t${example}\n}"
    val theme = LocalCampgroundTheme.current

    var currentText by remember { mutableStateOf(AnnotatedString("")) }

    if (example != null) {
        val highlights by remember(theme.isDark) {
            mutableStateOf(
                Highlights.Builder(
                    code = componentCode,
                    language = SyntaxLanguage.DEFAULT,
                    theme = if (theme.isDark) DarkSyntaxTheme else LightSyntaxTheme
                ).build()
            )
        }


        LaunchedEffect(highlights) {
            highlights.getHighlightsAsync(object :
                DefaultHighlightsResultListener() {
                override fun onSuccess(result: List<CodeHighlight>) {
                    currentText = result.generateAnnotatedString(componentCode)
                }
            })
        }
    }

    Column(
        modifier = Modifier.fillMaxWidth().clip(RoundedPreviewShape).border(
            1.dp,
            color = theme.border.copy(alpha = 0.2f),
            shape = RoundedPreviewShape
        )
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.background(theme.background)
                .padding(all = 32.dp)
                .fillMaxWidth()
                .defaultMinSize(minHeight = 128.dp)
        ) {
            content()
        }

        if (example != null) {
            Column(
                modifier = Modifier.fillMaxWidth().background(theme.secondary)
            ) {
                Column {
                    HorizontalDivider(color = theme.border.copy(alpha = 0.1f))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth().background(theme.alternative.copy(alpha = 0.4f))
                            .padding(horizontal = 12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Composable.kt", color = theme.text.default)

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
                    HorizontalDivider(color = theme.border.copy(alpha = 0.1f))
                }
                Text(
                    currentText,
                    fontFamily = sourceCodeProFamily(),
                    color = theme.text.default,
                    fontWeight = FontWeight.Normal,
                    modifier = Modifier.padding(
                        horizontal = 12.dp,
                        vertical = 14.dp
                    )
                )
            }
        }
    }
}

@Composable
fun ComponentPreview(
    title: String,
    description: String,
    contentString: String,
    content: @Composable () -> Unit
) {
    val theme = LocalCampgroundTheme.current

    Column(verticalArrangement = Arrangement.spacedBy(18.dp)) {
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(title, fontSize = 28.sp, fontWeight = FontWeight.W700, color = theme.text.default)
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


@Composable
fun ButtonDocumentation(maxWidth: Dp) {
    val theme = LocalCampgroundTheme.current

    DocumentationRoot(
        name = "Button",
        "A custom button components with multiple variations and sizes",
        theme = theme
    ) {
        ComponentCodePreview(buttonExample.code) {
            buttonExample.component()
        }
    }

    DocumentationRoot(
        name = "Button",
        "A custom button components with multiple variations and sizes",
        theme = theme
    ) {
        ComponentCodePreview {
            FlowRow(
                verticalArrangement = Arrangement.spacedBy(24.dp),
                horizontalArrangement = Arrangement.spacedBy(48.dp)
            ) {
                for (variant in ButtonVariants.entries) {
                    Column {
                        Text(variant.name.uppercase(), color = theme.text.secondary)
                        Button(
                            onClick = {},
                            variant = variant,
                            text = "Campground"
                        )
                    }

                }
            }
        }
    }
}