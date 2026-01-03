package dev.thecampground.ui.showcase.internal.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import camgroundui.showcase.composeapp.generated.resources.Res
import camgroundui.showcase.composeapp.generated.resources.campground
import camgroundui.showcase.composeapp.generated.resources.check_circle
import camgroundui.showcase.composeapp.generated.resources.info
import camgroundui.showcase.composeapp.generated.resources.prohibit
import camgroundui.showcase.composeapp.generated.resources.warning
import dev.thecampground.ui.Alert
import dev.thecampground.ui.AlertVariants
import dev.thecampground.ui.LocalCampgroundTheme
import dev.thecampground.ui.showcase.domain.CampgroundCodeExample
import dev.thecampground.ui.showcase.presentation.documentation.DocumentationRoot
import org.jetbrains.compose.resources.painterResource

private val alertExample = CampgroundCodeExample(
    code = """
    Alert(
        icon = { tint, size ->
            Icon(
                painterResource(Res.drawable.campground),
                contentDescription = "Alert Icon",
                tint = tint,
                modifier = Modifier.size(size)
            )
        },
        title = "campground/ui",
        content = "A collection of themed UI components for Campground projects."
    )        
    """.trimIndent()
) {
    Alert(
        icon = { tint, size ->
            Icon(
                painterResource(Res.drawable.campground),
                contentDescription = "Alert Icon",
                tint = tint,
                modifier = Modifier.size(size)
            )
        },
        title = "campground/ui",
        content = "A collection of themed UI components for Campground projects."
    )
}

@Composable
fun VariantItem(name: String, content: @Composable () -> Unit) {
    val theme = LocalCampgroundTheme.current
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(name, color = theme.text.secondary)
        content()
    }
}

@Composable
fun AlertDocumentation() {
    val theme = LocalCampgroundTheme.current

    DocumentationRoot(
        name = "Alert",
        description = "Displays a callout for user attention",
        theme = theme,
    ) {

        ComponentCodePreview(alertExample.code) {
            alertExample.component()
        }
    }

    DocumentationRoot(
        name = "Variants",
        description = "The different alert variants",
        theme = theme,
    ) {

        ComponentCodePreview {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                VariantItem("Default") {
                    Alert(
                        title = "Wait!",
                        content = "Unsaved changes will be lost. Please save before continuing.",
                        icon = { tint, size ->
                            Icon(
                                painterResource(Res.drawable.warning),
                                contentDescription = "Warning",
                                tint = tint,
                                modifier = Modifier.size(size)
                            )
                        },
                    )
                }


                VariantItem("Success") {
                    Alert(
                        variant = AlertVariants.SUCCESS,
                        title = "Saved",
                        content = "Your updates were applied successfully.",
                        icon = { tint, size ->
                            Icon(
                                painterResource(Res.drawable.check_circle),
                                contentDescription = "Success",
                                tint = tint,
                                modifier = Modifier.size(size)
                            )
                        },
                    )
                }

                VariantItem("Info") {
                    Alert(
                        variant = AlertVariants.INFO,
                        title = "Note",
                        content = "This setting applies to all items.",
                        icon = { tint, size ->
                            Icon(
                                painterResource(Res.drawable.info),
                                contentDescription = "Info",
                                tint = tint,
                                modifier = Modifier.size(size)
                            )
                        },
                    )
                }

                VariantItem("Secondary") {
                    Alert(
                        variant = AlertVariants.SECONDARY,
                        title = null,
                        content = "You can customize this settings later if needed.",
                        icon = { tint, size ->
                            Icon(
                                painterResource(Res.drawable.info),
                                contentDescription = "Secondary Info",
                                tint = tint,
                                modifier = Modifier.size(size)
                            )
                        },
                    )
                }

                VariantItem("Danger") {
                    Alert(
                        variant = AlertVariants.DANGER,
                        title = "Failed",
                        icon = { tint, size ->
                            Icon(
                                painterResource(Res.drawable.prohibit),
                                contentDescription = "Secondary Info",
                                tint = tint,
                                modifier = Modifier.size(size)
                            )
                        },
                    ) { tint ->
                        Text(
                            "Please verify billing information and try again.\n",
                            color = tint,
                            letterSpacing = (-0.5).sp,
                        )

                        Text(
                            "• Make sure your card details are correct",
                            color = tint,
                            letterSpacing = (-0.5).sp,
                        )
                        Text(
                            "• Ensure you have sufficient funds.",
                            color = tint,
                            letterSpacing = (-0.5).sp,
                        )
                        Text("• Verify billing address.", color = tint, letterSpacing = (-0.5).sp)
                    }
                }
            }

        }
    }
}