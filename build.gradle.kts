plugins {
    // this is necessary to avoid the plugins to be loaded multiple times
    // in each subproject's classloader
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.androidMultiplatformLibrary) apply false
    alias(libs.plugins.composeMultiplatform) apply false
    alias(libs.plugins.composeCompiler) apply false
    alias(libs.plugins.kotlinJvm) apply false
    alias(libs.plugins.kotlinMultiplatform) apply false
    alias(libs.plugins.kotlinSerialization) apply false
    alias(libs.plugins.buildkonfig) apply false
    alias(libs.plugins.sqlDelight) apply false
}

if (findProject(":screenshot") != null) {
    tasks.register("exportDevicePreviewScreenshot") {
        group = "documentation"
        description = "Exports realistic framed device screenshots of Gemini AI KMP App."
        dependsOn(":screenshot:exportDeviceScreenshots")
    }
}

