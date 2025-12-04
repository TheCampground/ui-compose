package dev.thecampground.ui.annotation

// I know, the name is funny, but technically accurate.
enum class CampgroundDocTypeType {
    CLASS,
    FUNCTION
}

data class CampgroundDocComponent(
    val name: String,
    val description: String = "No description provided",
    val props: List<CampgroundDocComponentProp> = listOf()
)

data class CampgroundDocType(
    val name: String,
    val description: String,
    val properties: List<String> = listOf(),
    val type: CampgroundDocTypeType
)

data class CampgroundDocComponentProp(
    val name: String,
    val type: String,
    val default: Boolean = false,
    val description: String = "No Description Provided"
)