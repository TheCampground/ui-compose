package dev.thecampground.ui.showcase.presentation.documentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.ScreenKey
import dev.thecampground.showcase.generated.CampgroundComponents
import dev.thecampground.ui.LocalCampgroundTheme
import dev.thecampground.ui.RoundedInputShape
import dev.thecampground.ui.annotation.model.CampgroundProp
import dev.thecampground.ui.showcase.internal.components.ComponentPreview


@Composable
private fun PropTextKey(value: String) {
    val theme = LocalCampgroundTheme.current

    Text(
        text = value,
        color = theme.text.default,
        fontWeight = FontWeight.SemiBold,
        fontSize = 18.sp,
    )
}

@Composable
private fun PropsTableHeader() {
    val theme = LocalCampgroundTheme.current

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Text(
            "Property",
            modifier = Modifier.weight(1f),
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            color = theme.text.default
        )
        Text(
            "Type",
            modifier = Modifier.weight(1f),
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            color = theme.text.default
        )
        Text(
            "Description",
            modifier = Modifier.weight(2f),
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            color = theme.text.default
        )
    }
}

@Composable
private fun PropsColumnHeader() {
    Column {
        Text(
            "Property",
            modifier = Modifier.weight(1f),
        )
    }
}


@Composable
private fun PropName(prop: CampgroundProp) {
    val theme = LocalCampgroundTheme.current

    Text(
        prop.name,
        modifier = Modifier
            .border(
                width = 1.dp,
                color = theme.inverse.copy(alpha = 0.08f),
                shape = RoundedCornerShape(4.dp)
            )
            .clip(RoundedCornerShape(4.dp))
            .background(theme.inverse.copy(alpha = 0.05f))
            .padding(horizontal = 4.dp, vertical = 2.dp),
        color = theme.text.default
    )
}

@Composable
fun PropsTableRow(prop: CampgroundProp) {
    val theme = LocalCampgroundTheme.current

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        HorizontalDivider(modifier = Modifier.fillMaxWidth(), color = theme.border)

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            Column(modifier = Modifier.weight(1f)) {
                PropName(prop)
            }

            Text(
                prop.type,
                modifier = Modifier.weight(1f),
                color = theme.text.default
            )
            Text(
                prop.description,
                modifier = Modifier.weight(2f),
                color = theme.text.default
            )
        }
    }
}

@Composable
fun PropsColumn(prop: CampgroundProp) {
    Row(horizontalArrangement = Arrangement.SpaceBetween) {
        Column(modifier = Modifier.weight(1f)) {
            PropName(prop)
        }

        Text("?")
    }
}

@Composable
fun RenderComponent(
    maxWidth: Dp,
    name: String,
    components: List<dev.thecampground.ui.annotation.model.CampgroundComponent>,
) {
    val composable = ComponentPreview.components[name]
    val theme = LocalCampgroundTheme.current
    if (composable != null) {
        Column(
            modifier = Modifier.fillMaxWidth(if (maxWidth < 800.dp) 1f else .8f).padding(14.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(32.dp)) {
                composable(maxWidth)  // invoke the @Composable lambda
            }


            Column(verticalArrangement = Arrangement.spacedBy(18.dp)) {
                Text(
                    "Props",
                    color = theme.text.default,
                    fontSize = 26.sp,
                    fontWeight = FontWeight.SemiBold
                )
                for (component in components) {
                    Column {
                        Text(
                            component.name,
                            fontWeight = FontWeight.Bold,
                            fontSize = 24.sp,
                            color = theme.button.primary.foreground,
                            modifier = Modifier
                                .clip(RoundedInputShape)
                                .border(
                                    width = 1.dp,
                                    color = theme.brand.copy(alpha = 0.8f)
                                )
                                .background(theme.brand)
                                .padding(8.dp)
                        )
                    }

                    BoxWithConstraints {

                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            if (this@BoxWithConstraints.maxWidth > 600.dp) {
                                PropsTableHeader()

                                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                    for (prop in component.props) {
                                        PropsTableRow(prop)
                                    }
                                }
                            } else {
                                Text(
                                    "Property",
                                    color = theme.text.default,
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.SemiBold
                                )

                                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                    for (prop in component.props) {
                                        PropsColumn(prop)
                                    }
                                }
                            }

                            HorizontalDivider(
                                modifier = Modifier.fillMaxWidth().padding(top = 18.dp),
                                color = theme.border,
                                thickness = 2.dp
                            )
                        }
                    }
                }



//                Column(modifier = Modifier.fillMaxWidth()) {
//                    Row(
//                        horizontalArrangement = Arrangement.Start,
//                        modifier = Modifier.fillMaxWidth()
//                    ) {
//                        Column() {
//                            PropTextKey("Property")
//                        }
//
//
//                        Column() {
//                            PropTextKey("Type")
//                        }
//
//                        Column() {
//                            PropTextKey("Description")
//                        }
//                    }
//                    for (prop in component.props) {
//                        Row(
//                            horizontalArrangement = Arrangement.Start,
//                            modifier = Modifier.fillMaxWidth()
//                        ) {
//                            Column(modifier = Modifier) {
//                                Text(prop.name)
//                            }
//                            Column(modifier = Modifier) {
//                                Text(prop.type)
//                            }
//                            Column(modifier = Modifier) {
//                                Text(prop.description)
//                            }
//                        }
//                    }
//                }

            }
        }
    } else {
        Text("Component not found")
    }
}


data class ComponentDetailsScreen(val component: String) : Screen {
    override val key: ScreenKey = component

    @Composable
    override fun Content() {
        val allComponents = CampgroundComponents.components[component]
        val scrollState = rememberScrollState()
        LocalCampgroundTheme.current

        BoxWithConstraints(modifier = Modifier.verticalScroll(scrollState)) {
            if (allComponents == null) {
                Text("A component was mentioned but it's documentation could not be found.")
            } else {
                RenderComponent(this@BoxWithConstraints.maxWidth, component, allComponents)
//                DocumentationRoot(
//                    name = campgroundComponent.name,
//                    description = campgroundComponent.description,
//                    theme,
//                ) {
//
//                    val firstExample = null
//
//                }
                }
            }
        }
}