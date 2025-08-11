enableFeaturePreview("VERSION_CATALOGS")

pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
    }
    plugins {
        id("com.android.application") version "4.1.3"
        id("com.android.library") version "4.1.3" // If you have library modules; otherwise, remove
        id("org.jetbrains.kotlin.android") version "1.4.32"
    }
}

rootProject.name = "GlassGameBoyEmulator"
include(":app")
