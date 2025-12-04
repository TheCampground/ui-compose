package dev.thecampground.ui.annotation



class CampgroundDocComponent(
    val name: String,
    val description: String = "No description provided",
    val examples: List<String> = listOf(),
    val props: List<CampgroundDocComponentProp> = listOf()
) {

}

class CampgroundDocComponentProp(
    val name: String,
    val type: String,
    val default: Boolean = false,
    val description: String = "No Description Provided"
)