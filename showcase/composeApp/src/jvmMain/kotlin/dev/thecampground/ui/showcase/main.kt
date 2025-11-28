package dev.thecampground.ui.showcase

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
//import org.jetbrains.skiko.DefaultConsoleLogger
//import org.jetbrains.skiko.setupSkikoLoggerFactory

fun main() {
//    setupSkikoLoggerFactory {
//        DefaultConsoleLogger.fromLevel("DEBUG")
//    }
//
//    System.setProperty("skiko.renderApi", "SOFTWARE_FAST")
//    System.setProperty("skiko.vsync.enabled", "FALSE")
//    System.setProperty("skiko.fps.enabled", "TRUE")
//    System.setProperty("skiko.logLevel", "1")


    for (entry in System.getProperties()) {
        println("${entry.key} ${entry.value}")
    }
    application {
        Window(
            onCloseRequest = ::exitApplication,
            title = "showcase",
        ) {

            App()
        }
    }
}

