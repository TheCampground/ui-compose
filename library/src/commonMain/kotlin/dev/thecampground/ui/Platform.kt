package dev.thecampground.ui

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform