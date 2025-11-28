package dev.thecampground.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun AccordionRoot(type: AccordionTypes = AccordionTypes.SINGLE, content: @Composable () -> Unit) {
    var visible by remember { mutableStateOf(false) }
    Box() {
        Column {


            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("What is the campground")

                Button(onClick = {
                    visible = !visible
                }, text = "O")
            }



            AnimatedVisibility(visible = visible, enter = fadeIn(spring(stiffness = Spring.StiffnessMedium)) + expandVertically(clip = false, animationSpec = spring(stiffness = Spring.StiffnessMedium)), exit = fadeOut(animationSpec = spring(stiffness = Spring.StiffnessMedium)) + shrinkVertically(clip = false, animationSpec = spring(stiffness = Spring.StiffnessMedium))) {
                Text("Campground is an independent collective where creativity meets code and design.")
            }

            Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(Color.Black))
        }
    }
}