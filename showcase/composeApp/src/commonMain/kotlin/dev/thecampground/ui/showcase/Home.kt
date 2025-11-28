package dev.thecampground.ui.showcase

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withLink
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import camgroundui.showcase.composeapp.generated.resources.Res
import camgroundui.showcase.composeapp.generated.resources.campground
import camgroundui.showcase.composeapp.generated.resources.compose_multiplatform
import camgroundui.showcase.composeapp.generated.resources.github_logo
import camgroundui.showcase.composeapp.generated.resources.hamburger
import dev.thecampground.ui.Button
import dev.thecampground.ui.ButtonVariants
import dev.thecampground.ui.Colors
import dev.thecampground.ui.InputSizes
import dev.thecampground.ui.RoundedInputShapeFull
import dev.thecampground.ui.RoundedInputShapePill
import org.jetbrains.compose.resources.painterResource


@Composable
fun Header(hamburgerVisible: Boolean = false, hamburgerOnClick: () -> Unit = {}) {
    Box(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.Companion.background(Colors.BG_SECONDARY)
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp), verticalAlignment = Alignment.CenterVertically) {
                if (hamburgerVisible) {
                    Button(onClick = hamburgerOnClick, variant = ButtonVariants.GHOST, size = InputSizes.ICON) {
                        Icon(
                            painterResource(Res.drawable.hamburger),
                            contentDescription = "Hamburger",
                            modifier = Modifier.size(26.dp),
                            tint = Colors.BG_DARK
                        )
                    }
                }
                Icon(
                    painterResource(Res.drawable.campground),
                    contentDescription = "Campground Logo",
                    modifier = Modifier.size(26.dp),
                    tint = Colors.BG_DARK
                )
                BoxWithConstraints {
                    if (maxWidth > 800.dp) {
                        Text(
                            "campground/compose-ui",
                            fontSize = 22.sp,
                            letterSpacing = (-.4).sp,
                            fontWeight = FontWeight.W700
                        )
                    }
                }
            }



            Row() {
                Button(
                    onClick = {},
                    variant = ButtonVariants.GHOST,
                    size = InputSizes.ICON
                ) {
                    Icon(
                        painterResource(Res.drawable.github_logo),
                        contentDescription = "Github Logo",
                        tint = Colors.BG_DARK,
                        modifier = Modifier.size(26.dp)
                    )
                }

            }
        }
        Row(
            modifier = Modifier.align(Alignment.Center),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                "Home",
                fontSize = 14.sp,
                letterSpacing = (-0.4).sp,
                modifier = Modifier.clip(RoundedInputShapePill)
                    .background(Colors.SECONDARY_BUTTON)
                    .padding(vertical = 1.dp, horizontal = 8.dp)
            )
            Text(
                "Docs",
                fontSize = 14.sp,
                letterSpacing = (-0.4).sp,
                modifier = Modifier.padding(vertical = 1.dp, horizontal = 4.dp)
            )
            Text(
                "Components",
                fontSize = 14.sp,
                letterSpacing = (-0.4).sp,
                modifier = Modifier.padding(vertical = 1.dp, horizontal = 4.dp)
            )
        }
    }
}

@Composable
fun Home() {
    Column() {
        Box() {
            Header()

            Box(modifier = Modifier.fillMaxSize().align(Alignment.Center).padding(18.dp)) {
                Column(
                    modifier = Modifier.align(Alignment.Center),
                    verticalArrangement = Arrangement.spacedBy(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {

                    Row(
                        Modifier.clip(RoundedInputShapeFull).background(Colors.BG_DARK)
                            .padding(horizontal = 18.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painterResource(Res.drawable.campground),
                            contentDescription = "Campground Logo",
                            Modifier.size(18.dp),
                            tint = Color.White
                        )
                        Box(
                            modifier = Modifier.height(18.dp).width(1.dp)
                                .background(Color.White.copy(alpha = 0.5f))
                        )
                        Text(buildAnnotatedString {
                            append("campground/compose-ui is now in ")
                            withStyle(style = SpanStyle(fontWeight = FontWeight.W600)) {
                                append("beta")
                            }
                            append("!")

                        }, fontSize = 14.sp, color = Color.White, fontWeight = FontWeight.Light)
                    }

                    Text(
                        "A collection of themed UI components.",
                        fontWeight = FontWeight.W800,
                        fontSize = 64.sp,
                        lineHeight = 64.sp,
                        textAlign = TextAlign.Center,
                        color = Colors.BG_DARK
                    )

                    Text(
                        buildAnnotatedString {
                            append("Simple, accessible, and cozily themed component library built on top of ")
                            defaultLink(
                                url = "https://m3.material.io",
                                text = "Material 3"
                            )
                            append(" for ")
                            defaultLink(
                                url = "https://github.com/TheCampground",
                                text = "@TheCampground"
                            )
                            append(" projects.")
                        },
                        fontSize = 18.sp,
                        color = Colors.TEXT_ALT,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.W400
                    )

                    Button(onClick = {}, variant = ButtonVariants.PRIMARY, text = "Get Started")
                }
            }

        }
    }
}

fun AnnotatedString.Builder.defaultLink(url: String, text: String) {
    return withLink(
        link = LinkAnnotation.Url(
            url = url,
            styles = TextLinkStyles(
                style = SpanStyle(
                    color = Color.Unspecified,
                    textDecoration = TextDecoration.Underline
                )
            )
        )
    ) {
        append(text)
    }
}