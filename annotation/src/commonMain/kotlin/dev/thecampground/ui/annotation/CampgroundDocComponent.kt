package dev.thecampground.ui.annotation

data class CampgroundDocComponent(
    val uniqueName: String,
    val name: String,
    val description: String = "No description provided",
    val props: List<CampgroundDocComponentProp> = listOf()
)

data class CampgroundDocType(
    val name: String,
    val description: String,
    val properties: List<String> = listOf()
)

data class CampgroundDocComponentProp(
    val name: String,
    val type: String,
    val default: Boolean = false,
    val description: String = "No Description Provided"
)