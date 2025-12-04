package dev.thecampground.ui.annotation.model

// I know, the name is funny, but technically accurate.
enum class CampgroundTypeType {
    CLASS,
    FUNCTION
}

data class CampgroundComponent(
    val name: String,
    val description: String = "No description provided",
    val props: List<CampgroundProp> = listOf()
)

data class CampgroundType(
    val name: String,
    val description: String,
    val properties: List<String> = listOf(),
    val type: CampgroundTypeType
)

data class CampgroundProp(
    val name: String,
    val type: String,
    val default: Boolean = false,
    val description: String = "No Description Provided"
)