package dev.thecampground.ui.showcase.presentation.root

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import camgroundui.showcase.composeapp.generated.resources.Res
import camgroundui.showcase.composeapp.generated.resources.campground
import camgroundui.showcase.composeapp.generated.resources.github_logo
import camgroundui.showcase.composeapp.generated.resources.hamburger
import dev.thecampground.ui.Button
import dev.thecampground.ui.ButtonVariants
import dev.thecampground.ui.Colors
import dev.thecampground.ui.InputSizes
import dev.thecampground.ui.RoundedInputShapePill
import org.jetbrains.compose.resources.painterResource

@Composable
fun Header(hamburgerVisible: Boolean = false, hamburgerOnClick: () -> Unit = {}) {
    Box(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.background(Colors.BG_SECONDARY)
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