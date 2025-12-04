package dev.thecampground.ui

class JVMPlatform: Platform {
    override val name: String = "Java ${System.getProperty("kotlin.version")}"
}

actual fun getPlatform(): Platform = JVMPlatform()