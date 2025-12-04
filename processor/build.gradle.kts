plugins {
    kotlin("multiplatform")
}

// Versions are declared in 'gradle.properties' file
val kspVersion: String by project

kotlin {
    jvm()

    sourceSets {
        jvmMain {
            dependencies {
                implementation("com.google.devtools.ksp:symbol-processing-api:${kspVersion}")
                implementation(libs.kotlinpoet)
                implementation(project(":annotation"))
            }
            kotlin.srcDir("src/main/kotlin")
            resources.srcDir("src/main/resources")
        }
    }
}

