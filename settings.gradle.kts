rootProject.name = "Gemini-AI-KMP-App"
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    repositories {
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
        google()
        gradlePluginPortal()
        mavenCentral()
        // or for snapshot builds
        maven("https://s01.oss.sonatype.org/content/repositories/snapshots/")
    }
}

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
        // or for snapshot builds
        maven("https://s01.oss.sonatype.org/content/repositories/snapshots/")
    }
}

include(":composeApp")