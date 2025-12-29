import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidKotlinMultiplatformLibrary)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    id("com.google.devtools.ksp") version "2.3.3"
}



kotlin {
    androidLibrary {
        namespace = "dev.thecampground.ui"
        compileSdk = libs.versions.android.compileSdk.get().toInt()
        minSdk = libs.versions.android.minSdk.get().toInt()
        experimentalProperties["android.experimental.kmp.enableAndroidResources"] = true
        androidResources.enable = true
        packaging {
            resources {
                excludes += "/META-INF/{AL2.0,LGPL2.1}"
            }
        }

        compilations.all {
            compileTaskProvider.configure {
                compilerOptions {
                    jvmTarget.set(JvmTarget.JVM_17)
                }
            }
        }
    }


    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }
    
    jvm()
    
//    js {
//        browser()
//        binaries.executable()
//    }
    
    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        browser()
        binaries.executable()
    }
    
    sourceSets {
        val commonMain by getting

        androidMain.dependencies {
            implementation(libs.androidx.activity.compose)
            implementation("org.jetbrains.compose.ui:ui-tooling-preview:1.10.0-rc01")
        }
//        // Intermediate shared source set
//        val jvmIosWasmMain by creating {
//            dependsOn(commonMain)
//        }

        commonMain {
            kotlin.srcDir("build/generated/ksp/metadata/commonMain/kotlin")
            dependencies {
                implementation("org.jetbrains.compose.runtime:runtime:1.10.0-rc01")
                implementation("org.jetbrains.compose.foundation:foundation:1.10.0-rc01")
                implementation("org.jetbrains.compose.material3:material3:1.9.0")
                implementation("org.jetbrains.compose.ui:ui:1.10.0-rc01")
                implementation("org.jetbrains.compose.components:components-resources:1.10.0-rc01")

                implementation(libs.androidx.lifecycle.viewmodelCompose)
                implementation(libs.androidx.lifecycle.runtimeCompose)

                implementation(project(":annotation"))
// https://mvnrepository.com/artifact/androidx.compose.ui/ui-tooling-preview
                implementation("org.jetbrains.compose.ui:ui-tooling-preview:1.10.0-rc01")
            }

        }
        commonTest {
            dependencies {
                implementation(libs.kotlin.test)
            }
        }
        iosMain {
//            dependsOn(jvmIosWasmMain)
        }
        wasmJsMain {
//            dependsOn(jvmIosWasmMain)
        }

        jvmMain {
//            dependsOn(jvmIosWasmMain)
            kotlin.srcDir("src/main/kotlin")
            resources.srcDir("src/main/resources")
            dependencies {
                implementation(compose.desktop.currentOs)
                implementation(libs.kotlinx.coroutinesSwing)
                implementation(project(":processor"))

            }
        }
    }
}

dependencies {
    add("kspCommonMainMetadata", project(":processor"))


//    add("kspJvm", project(":processor"))
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().all {
    if (name != "kspCommonMainKotlinMetadata") {
        dependsOn("kspCommonMainKotlinMetadata")
    }
}

//
//compose.desktop {
//    application {
//        mainClass = "dev.thecampground.ui.MainKt"
//
//        nativeDistributions {
//            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
//            packageName = "dev.thecampground.ui"
//            packageVersion = "1.0.0"
//        }
//    }
//}
compose.resources {
    publicResClass = true
}
