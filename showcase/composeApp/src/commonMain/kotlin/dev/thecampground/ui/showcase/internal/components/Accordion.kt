package dev.thecampground.ui.showcase.internal.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Dp
import dev.thecampground.ui.AccordionItem
import dev.thecampground.ui.AccordionRoot
import dev.thecampground.ui.AccordionTypes
import dev.thecampground.ui.LocalCampgroundTheme
import dev.thecampground.ui.showcase.domain.CampgroundCodeExample
import dev.thecampground.ui.showcase.presentation.documentation.DocumentationRoot

private val accordionExample = CampgroundCodeExample(
    code = """
         AccordionRoot(AccordionTypes.SINGLE) {
               AccordionItem(
                  title = "What is Campground?",
                  content = "Campground is an independent collective where creativity meets code and design."
               )
               AccordionItem(
                 title = "What is The Campsite?", 
                 content = "The Campsite is an ever-growing community on Discord that aims to provide a warm welcome to all new members joining us!"
               )
         }
    """.trimIndent()
) {
    AccordionRoot(type = AccordionTypes.SINGLE) {
        AccordionItem(
            title = "What is Campground?",
            content = "Campground is an independent collective where creativity meets code and design."
        )
        AccordionItem(
            title = "What is The Campsite?",
            content = "The Campsite is an ever-growing community on Discord that aims to provide a warm welcome to all new members joining us!"
        )
    }
}

@Composable
fun AccordionDocumentation(maxWidth: Dp) {
    val theme = LocalCampgroundTheme.current

    DocumentationRoot(
        name = "Accordion",
        "Organizes content into collapsible sections.",
        theme = theme
    ) {
//        ComponentCodePreview(maxWidth, CampgroundUIExamples.ButtonExample1) {
//            ButtonExample1()
//        }
//        ComponentPreview(title ="Variants", description = "The different button variants", contentString = CampgroundUIExamples.ButtonExample1) {
//
//        }

        ComponentCodePreview(accordionExample.code) {
            accordionExample.component()
        }


    }
}